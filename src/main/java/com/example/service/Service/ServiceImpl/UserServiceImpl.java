package com.example.service.Service.ServiceImpl;

import com.example.service.Bean.Code;
import com.example.service.Bean.CreatePaymentResponse;
import com.example.service.Bean.In.*;
import com.example.service.Bean.Out.Bill;
import com.example.service.Mapper.UserMapper;
import com.example.service.Service.EmailService;
import com.example.service.Service.RedisService;
import com.example.service.Service.UserService;
import com.example.service.Thread.EmailThread;
import com.example.service.Util.TokenUtil;
import com.example.service.Util.VerCodeGenerateUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonSyntaxException;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.net.ApiResource;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    EmailService emailService;
    @Autowired
    RedisService redisService;
    @Autowired
    public TokenUtil tokenUtil;
    @Autowired
    VerCodeGenerateUtil verCodeGenerateUtil;

    ObjectMapper objectMapper = new ObjectMapper();

    String json;

    //注册登录
    @Override
    public int findUser(User user) {
        String email = user.getEmail();
        String password = user.getPassword();
        User user1 = userMapper.findUser(email);
        if (user1 == null) {
            String text = verCodeGenerateUtil.generateVerCode();
            redisService.set(email, password);
            redisService.set(text, email);
            System.out.println(text);
            new EmailThread(user.getEmail(), "subject", text, emailService).start();
            return 1;
        } else {
            return 0;
        }


    }

    @Override
    public int verifyUser(Code code) {
        String email = code.getEmail();
        String codes = code.getCode();
        String realemails = redisService.get(codes);

        if (email.equals(realemails)) {
            User user = new User();
            user.setEmail(email);
            user.setPassword(redisService.get(email));
            userMapper.addUser(user);
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int info(UserInfo userInfo) {

        String UserID = verCodeGenerateUtil.generateUserID();
        if (userMapper.findUserID(UserID) == null) {
            userInfo.setUserID(UserID);
            userInfo.setBalance("0");
            userMapper.addUserInfo(userInfo);
        } else {
            String UserIDS = verCodeGenerateUtil.generateUserID();
            userInfo.setUserID(UserIDS);
            userMapper.addUserInfo(userInfo);
        }
        return 1;
    }

    @Override
    public int checkUser(User user) {
        User user1 = userMapper.checkUser(user);
        if (user1 != null) {
            return 1;
        } else {
            return 0;
        }
    }

    //账单支付
    @Override
    public String selectBill(String token) {
        //从Token中获取信息
        String userEmail = tokenUtil.getValue(token);
        String userId = userMapper.getUserId(userEmail);
        //查询用户账单
        List<Bill> billList = userMapper.selectBill(userId);
        for (Bill bill : billList) {
            if (bill.getReceiveUser().equals(userId)) {
                bill.setType("top-up");
            } else {
                bill.setType("pay");
            }
        }



        try {
            json = objectMapper.writeValueAsString(billList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public int transferTo(UserID userId, String token) {
        //获取时间
        Date date = new Date();
        Timestamp time = new Timestamp(date.getTime());
        Transaction transaction = new Transaction();

        String payUser = userId.getUserID();
        float amount = Float.parseFloat(userId.getAmount());
        String receiveEmail = tokenUtil.getValue(token);
        String receiveUser = userMapper.getUserId(receiveEmail);

        float payBalance = Float.parseFloat(userMapper.selectBalance(payUser));
        float receiveBalance = Float.parseFloat(userMapper.selectBalance(receiveUser));

        if (payBalance < amount) {
            return 0;
        }
        float payBalances = payBalance - amount;
        userMapper.updateBalance(String.valueOf(payBalances), payUser);
        float receiveBalances = receiveBalance + amount;
        userMapper.updateBalance(String.valueOf(receiveBalances), receiveUser);

        //存入数据库
        transaction.setPayUser(payUser);
        transaction.setReceiveUser(receiveUser);
        transaction.setAmount(String.valueOf(amount));
        transaction.setDate(time);
        userMapper.transferTo(transaction);
        return 1;
    }

    @Override
    public int checkPayPswd(String PayPswd, String token) {
        String receiveEmail = tokenUtil.getValue(token);
        int num = userMapper.checkPayPswd(receiveEmail, PayPswd);
        if (num == 1) {
            return 1;
        } else {
            return 0;
        }
    }

    //充值


    @Override
    public String createPaymentIntent(Amount Amounts) {
        Map<String, Object> map = new HashMap<>();
        Stripe.apiKey = "sk_test_51LImNKAOiNy9BWzWHWwXKchph0iHIA8eySN5eDNYtxL9LLrNzFISXmTOHrjrpEnvbF1pKyRCaG9BcqnWXjcNNPnf00vUzWYyjr";
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount((long) Integer.parseInt(Amounts.getAmounts()))
                        .setCurrency("eur")
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams.AutomaticPaymentMethods
                                        .builder()
                                        .setEnabled(true)
                                        .build()
                        )
                        .build();

// Create a PaymentIntent with the order amount and currency
        PaymentIntent paymentIntent = null;
        try {
            paymentIntent = PaymentIntent.create(params);
        } catch (StripeException e) {
            e.printStackTrace();
        }

        CreatePaymentResponse paymentResponse = new CreatePaymentResponse(paymentIntent.getClientSecret());
        map.put("clientSecret", paymentResponse.getClientSecret());
        try {
            json = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    @Override
    public void webhook(String payload,String sigHeader) {
        Event event = null;

        String endpointSecret = "whsec_52374beff821539be93da3f37f381a042274f3ac13515715ac8dbea18cdc9d42";

        try {
            event = ApiResource.GSON.fromJson(payload, Event.class);
        } catch (JsonSyntaxException e) {
            // Invalid payload
            System.out.println("️  Webhook error while parsing basic request.");

        }
        if (endpointSecret != null && sigHeader != null) {
            // Only verify the event if you have an endpoint secret defined.
            // Otherwise use the basic event deserialized with GSON.
            try {
                event = Webhook.constructEvent(
                        payload, sigHeader, endpointSecret
                );
            } catch (SignatureVerificationException e) {
                // Invalid signature
                System.out.println("️  Webhook error while validating signature.");

            }
        }
        // Deserialize the nested object inside the event
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        } else {
            // Deserialization failed, probably due to an API version mismatch.
            // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
            // instructions on how to handle this case, or return an error here.
        }
        // Handle the event
        switch (event.getType()) {
            case "payment_intent.succeeded":
                PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                System.out.println("Payment for " + paymentIntent.getAmount() + " succeeded.");
                // Then define and call a method to handle the successful payment intent.
                // handlePaymentIntentSucceeded(paymentIntent);
                break;
            case "payment_method.attached":
                PaymentMethod paymentMethod = (PaymentMethod) stripeObject;
                // Then define and call a method to handle the successful attachment of a PaymentMethod.
                // handlePaymentMethodAttached(paymentMethod);
                break;
            default:
                System.out.println("Unhandled event type: " + event.getType());
                break;
        }

        return;
    }


    //信息修改
    @Override
    public int changePswd(UserPswd userPswd, String token) {
        User user = new User();
        String receiveEmail = tokenUtil.getValue(token);
        user.setEmail(receiveEmail);
        user.setPassword(userPswd.getOldPswd());
        User user1 = userMapper.checkUser(user);
        if (user1 != null) {
            userMapper.changePswd(userPswd.getNewPswd(), receiveEmail);
            return 1;
        } else {
            return 0;
        }

    }

    @Override
    public int changePayPswd(UserPayPswd userPayPswd, String token) {
        String receiveEmail = tokenUtil.getValue(token);
        int num = userMapper.checkPayPswd(receiveEmail, userPayPswd.getOldPayPswd());
        if (num != 0) {
            userMapper.changePayPswd(userPayPswd.getNewPayPswd(), receiveEmail);
            return 1;
        } else {
            return 0;
        }
    }
}
