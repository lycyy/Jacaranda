package com.example.service.Service.ServiceImpl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.service.Bean.In.Code;
import com.example.service.Bean.CreatePaymentResponse;
import com.example.service.Bean.In.*;
import com.example.service.Bean.Out.Balance;
import com.example.service.Bean.Out.Bill;
import com.example.service.Bean.Out.Company;
import com.example.service.Mapper.CompanyUserMapper;
import com.example.service.Mapper.UserMapper;
import com.example.service.Service.EmailService;
import com.example.service.Service.RedisService;
import com.example.service.Service.UserService;
import com.example.service.Util.ConfigurationUtil;
import com.example.service.Util.TokenUtil;
import com.example.service.Util.VerCodeGenerateUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.JsonSyntaxException;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.net.ApiResource;
import com.stripe.net.RequestOptions;
import com.stripe.net.Webhook;
import com.stripe.param.EphemeralKeyCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserInfo userInfo;
    @Autowired
    EmailService emailService;
    @Autowired
    RedisService redisService;
    @Autowired
    public TokenUtil tokenUtil;
    @Autowired
    VerCodeGenerateUtil verCodeGenerateUtil;
    @Autowired
    WebSocketService webSocketService;
    @Autowired
    CompanyUserMapper companyUserMapper;

    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    ConfigurationUtil configurationUtil;
    String salt;
    String json;
    String Stripe_apikey;

    @PostConstruct
    public void init() {
        salt = this.configurationUtil.getToken_key();
        Stripe_apikey = this.configurationUtil.getStripe_apiKey();
    }

    //注册登录
    @Override
    public int findUser(User user) {
        String email = user.getEmail();
        String password = user.getPassword();
        String username = user.getUsername();
        int num = userMapper.findUser(email);
        if (num == 0) {
            String text = verCodeGenerateUtil.generateVerCode();
            redisService.set(email + "p", password);
            redisService.set(email + "u", username);
            redisService.set(text, email);
            System.out.println(text);
//            new EmailThread(user.getEmail(), "Verification code", text, emailService).start();
            return 1;
        } else {
            return 0;
        }


    }

    @Override
    public int verifyUser(Code code) {


        String email = code.getEmail();
        String codes = code.getCode();
        StringBuffer UserID = verCodeGenerateUtil.generateUserID();
        while (userMapper.findUserID(String.valueOf(UserID)) == 1) {
            UserID = verCodeGenerateUtil.generateUserID();
        }


        String realemails = redisService.get(codes);
        if (email.equals(realemails)) {
            userInfo.setPassword(redisService.get(email + "p") + salt);
            userInfo.setUserID(String.valueOf(UserID));
            userInfo.setUsername(redisService.get(email + "u"));
            userInfo.setEmail(email);

            userMapper.addUserInfo(userInfo);
            return 1;
        } else {
            return 0;
        }

    }

    @Override
    public int info(UserInfo userInfo, String token) {
        Stripe.apiKey = Stripe_apikey;

        String UserEmail = tokenUtil.getValue(token);

        //获取customerid
        Map<String, Object> params = new HashMap<>();
        params.put("email", UserEmail);
        Customer customer = null;
        try {
            customer = Customer.create(params);
        } catch (StripeException e) {
            e.printStackTrace();
        }
        String cid = customer.getId();
        //注入属性

        userInfo.setPin(userInfo.getPin() + salt);
        userInfo.setCustomerId(cid);
        userInfo.setBalance("0");
        userInfo.setPictureName("R.png");
        userMapper.updateUserInfo(userInfo.getMobile(), userInfo.getPin(), userInfo.getBalance(), userInfo.getCustomerId(), userInfo.getPictureName(), UserEmail);

        return 1;
    }

    @Override
    public String checkUser(User user) {
        user.setPassword(user.getPassword() + salt);
        String RefreshToken = tokenUtil.generateToken(user);
        String AccessToken = tokenUtil.generateaccessToken(user.getEmail());
        Map<String, Object> map = new HashMap<>();
        int a = userMapper.checkUser(user);
        String b = userMapper.checkUserInfo(user.getEmail());
        if (a != 0) {

            UserInfo userInfo = userMapper.getUserInfo(user.getEmail());
            map.put("UserID", userInfo.getUserID());
            map.put("UserName", userInfo.getUsername());
            map.put("RefreshToken", RefreshToken);
            map.put("AccessToken", AccessToken);
            if (b != null) {
                map.put("info", "1");
            } else {
                map.put("info", "0");
            }
            try {
                json = objectMapper.writeValueAsString(map);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return json;
        } else {
            return "查询错误";
        }
    }

    @Override
    public String testToken(String token) {
        String userEmail = tokenUtil.getValue(token);
        int num = userMapper.findUser(userEmail);
        if (num == 0) {
            return "用户不存在";
        }

        DecodedJWT decodedJWT = tokenUtil.verify(token);
        int a = tokenUtil.getExpiresAt(decodedJWT);
        if (a == 1) {
            String email = tokenUtil.getValue(token);
            User users = new User();
            users.setEmail(email);
            String newToken = tokenUtil.generateToken(users);

            String json = null;
            try {
                json = objectMapper.writeValueAsString(newToken);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return json;
        } else {
            return "验证成功";
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

            String Receiveusername = userMapper.selectUserName(bill.getReceiveUser());
            bill.setReceiveUsername(Receiveusername);
            String Payusername = userMapper.selectUserName(bill.getPayUser());
            bill.setPayUsername(Payusername);
            if (bill.getReceiveUser().equals(bill.getPayUser())) {
                bill.setType("top-up");
            } else if (bill.getReceiveUser().equals(userId)) {
                bill.setType("receive");
            } else {
                bill.setType("pay");
            }
        }

        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        objectMapper.setDateFormat(sdf);
        try {
            json = objectMapper.writeValueAsString(billList).replace("date", "dateString");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    @Override
    public String selectBalanceOf(String token) {
        //从Token中获取信息
        String userEmail = tokenUtil.getValue(token);
        String userId = userMapper.getUserId(userEmail);
        String balanceOf = userMapper.selectBalance(userId);
        Balance balance = new Balance();
        balance.setBalanceof(balanceOf);
        try {
            json = objectMapper.writeValueAsString(balance);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    //发送付款请求
    @Override
    public int Verify(UserID userId, String token) {

        long t = 0;
        String value = new String();
        //获取时间
        Date before = new Date();

        String payUser = userId.getUserID();

        webSocketService.sendMessage("Verify Paypassword", Long.parseLong(payUser));

        while (t <= 90) {
            Date now = new Date();
            t = (now.getTime() - before.getTime()) / 1000;
            value = redisService.get(payUser);
            if (value != null) {
                break;
            }
        }
        if (value == null) {
            return -1;
        }

        int a = transfer(userId, token);
        redisService.remove(payUser);
        if (a == 1) {
            return 1;
        } else {
            return 0;
        }

    }

    //完成转账流程
    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public int transfer(UserID userId, String token) {
        //获取时间
        Date date = new Date();
        Timestamp time = new Timestamp(date.getTime());
        Transaction transaction = new Transaction();

        String payUser = userId.getUserID();
        float amount = Float.parseFloat(userId.getAmount());
        String receiveEmail = tokenUtil.getValue(token);
        String receiveUser = companyUserMapper.selectUserID(receiveEmail);
        float payBalance = Float.parseFloat(userMapper.selectBalance(payUser));
        float receiveBalance = Float.parseFloat(companyUserMapper.selectBalance(receiveUser));

        if (payBalance < amount) {
            return 0;
        }
        float payBalances = payBalance - amount;
        userMapper.updateBalance(String.valueOf(payBalances), payUser);
        float receiveBalances = receiveBalance + amount;
        companyUserMapper.updateBalances(String.valueOf(receiveBalances), receiveUser);

        //存入数据库
        transaction.setPayUser(payUser);
        transaction.setReceiveUser(receiveUser);
        transaction.setAmount(String.valueOf(amount));
        transaction.setDate(time);
        userMapper.transferTo_Company(transaction);
        return 1;
    }

    @Override
    public int checkPayPswd(String PayPswd, String token) {
        String payEmail = tokenUtil.getValue(token);
        String payUser = userMapper.getUserId(payEmail);
        int num = userMapper.checkPayPswd(payEmail, PayPswd + salt);
        if (num == 1) {
            redisService.set(payUser, "true");
            return 1;
        } else {
            return 0;
        }
    }

    //个人转账
    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public String checkId(UserID userId, String token) {
        int a = userMapper.findUserID(userId.getUserID());
        String payEmail = tokenUtil.getValue(token);
        String payUser = userMapper.getUserId(payEmail);
        String receiveUser = userId.getUserID();
        if (a == 0) {
            return "0";
        } else if (payUser.equals(receiveUser)) {
            return "-1";
        } else {
            String username = userMapper.selectUserName(userId.getUserID());
            return username;
        }
    }

    @Override
    public int transferTo(UserID userId, String token) {
        int a = userMapper.findUserID(userId.getUserID());
        String payEmail = tokenUtil.getValue(token);
        String payUser = userMapper.getUserId(payEmail);
        String receiveUser = userId.getUserID();
        if (a == 0 || payUser.equals(receiveUser)) {
            return -1;
        }
        Date date = new Date();
        Timestamp time = new Timestamp(date.getTime());
        Transaction transaction = new Transaction();

        float amount = Float.parseFloat(userId.getAmount());
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

    //充值
    @Override
    public String createPaymentIntent(Amount Amounts, String token) {
        Map<String, Object> map = new HashMap<>();
        Stripe.apiKey = "sk_test_51LImNKAOiNy9BWzWHWwXKchph0iHIA8eySN5eDNYtxL9LLrNzFISXmTOHrjrpEnvbF1pKyRCaG9BcqnWXjcNNPnf00vUzWYyjr";
        String receiveEmail = tokenUtil.getValue(token);
        String cid = userMapper.getCustomerId(receiveEmail);

        EphemeralKeyCreateParams ephemeralKeyParams =
                EphemeralKeyCreateParams.builder()
                        .setCustomer(cid)
                        .build();

        RequestOptions ephemeralKeyOptions =
                RequestOptions.builder()
                        .setStripeVersionOverride("2020-08-27")
                        .build();

        EphemeralKey ephemeralKey = null;
        try {
            ephemeralKey = EphemeralKey.create(
                    ephemeralKeyParams,
                    ephemeralKeyOptions);
        } catch (StripeException e) {
            e.printStackTrace();
        }

        int realAmount = Integer.parseInt(Amounts.getAmounts()) * 100;
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount((long) realAmount)
                        .setCurrency("eur")
                        .setCustomer(cid)
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
        map.put("paymentIntent", paymentIntent.getClientSecret());
        map.put("ephemeralKey", ephemeralKey.getSecret());
        map.put("customer", cid);
        map.put("publishableKey", "pk_test_51LImNKAOiNy9BWzWmrjh6L2oHrjbNtPDxaBkavZ4yJnFqy6bDUutFcvZLUFfC5enOPGNDIuTLHISMUes2m5mc0yJ00H7FnRIcj");

        try {
            json = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    @Override
    public String webhook(String payload, String sigHeader) {
        Event event = null;

        String amount;

        String endpointSecret = "whsec_3Ea8HDJjKnVXx1OWLQqYgLUH16EUwAEg";

        try {
            event = ApiResource.GSON.fromJson(payload, Event.class);
        } catch (JsonSyntaxException e) {
            // Invalid payload
            System.out.println("️  Webhook error while parsing basic request.");
            return "";
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
                return "";
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
                String cid = paymentIntent.getCustomer();
                long a = paymentIntent.getAmount() / 100;
                System.out.println("Payment for " + a + " succeeded.");
                System.out.println("cid:" + cid + " succeeded.");
                amount = String.valueOf(a);
                addBalance(amount, cid);
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

        return "success";
    }

    @Override
    public String addBalance(String amount, String cid) {
        String balance = userMapper.getBalance(cid);
        //获取时间
        Date date = new Date();
        Timestamp time = new Timestamp(date.getTime());
        //计算新余额
        float a = Float.parseFloat(amount);
        float b = Float.parseFloat(balance);
        float balances = a + b;
        userMapper.updateBalances(String.valueOf(balances), cid);

        //添加充值记录
        String userid = userMapper.getUserIdby_cid(cid);
        Transaction transaction = new Transaction();
        transaction.setPayUser(userid);
        transaction.setReceiveUser(userid);
        transaction.setAmount(String.valueOf(a));
        transaction.setDate(time);
        userMapper.transferTo(transaction);
        return "success";
    }

    //信息修改
    @Override
    public int changePswd(UserPswd userPswd, String token) {
        User user = new User();
        String receiveEmail = tokenUtil.getValue(token);
        user.setEmail(receiveEmail);
        user.setPassword(userPswd.getOldPswd() + salt);
        int a = userMapper.checkUser(user);
        if (a != 0) {
            userMapper.changePswd(userPswd.getNewPswd() + salt, receiveEmail);
            return 1;
        } else {
            return 0;
        }

    }

    @Override
    public int changePayPswd(UserPin userPin, String token) {
        String receiveEmail = tokenUtil.getValue(token);
        int num = userMapper.checkPayPswd(receiveEmail, userPin.getOldPin() + salt);
        if (num != 0) {
            userMapper.changePayPswd(userPin.getNewPin() + salt, receiveEmail);
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int changeUsername(Usernames usernames, String token) {
        String receiveEmail = tokenUtil.getValue(token);
        try {
            userMapper.changeUsername(usernames.getUsername(), receiveEmail);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    //忘记密码

    @Override
    public int sendEmail(User user) {
        String email = user.getEmail();
        String text = verCodeGenerateUtil.generateVerCode();
        redisService.set(text, email);
        System.out.println(text);
//          new EmailThread(user.getEmail(), "subject", text, emailService).start();
        return 1;
    }
    //验证码

    @Override
    public int testcode(Code code) {
        String email = code.getEmail();
        String codes = code.getCode();
        String realemails = redisService.get(codes);
        if (email.equals(realemails)) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int setPswd(User user) {
        String email = user.getEmail();
        String pswd = user.getPassword() + salt;
        if (userMapper.findUser(email) != 0) {
            userMapper.changePswd(pswd, email);
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public String getInfo(String token) {
        String receiveEmail = tokenUtil.getValue(token);
        UserInfo userInfo = userMapper.getUserInfo(receiveEmail);
        Map<String, Object> map = new HashMap<>();
        map.put("UserID", userInfo.getUserID());
        map.put("Email", userInfo.getEmail());
        map.put("UserName", userInfo.getUsername());
        map.put("Mobile", userInfo.getMobile());
        try {
            json = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    //公司
    @Override
    public String selectCompany() {
        List<Company> companyList = userMapper.selectCompany();

        try {
            json = objectMapper.writeValueAsString(companyList).replace("c_name", "name").replace("c_address", "address").replace("c_Mobile", "mobile");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    @Override
    public String getAccessToken(String token) {
        String email = tokenUtil.getValue(token);
        String AccessToken = tokenUtil.generateaccessToken(email);
        Map<String, Object> map = new HashMap<>();
        map.put("AccessToken", AccessToken);
        try {
            json = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }
}


