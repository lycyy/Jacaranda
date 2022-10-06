package com.example.service.Service.ServiceImpl;

import com.example.service.Bean.In.*;
import com.example.service.Bean.Out.Balance;
import com.example.service.Bean.Out.Bill;
import com.example.service.Bean.Out.CompanyBill;
import com.example.service.Mapper.CompanyUserMapper;
import com.example.service.Mapper.UserMapper;
import com.example.service.Service.CompanyUserService;
import com.example.service.Service.EmailService;
import com.example.service.Service.RedisService;
import com.example.service.Service.UserService;
import com.example.service.Util.TokenUtil;
import com.example.service.Util.VerCodeGenerateUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CompanyUserServiceImpl implements CompanyUserService {
    static String SIGN = "0960543";
    @Autowired
    CompanyUserMapper companyUserMapper;
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
    CompanyInfo companyInfo;

    ObjectMapper objectMapper = new ObjectMapper();

    String json;

    @Override
    public int Companyregister(User user) {
        String email = user.getEmail();
        String password = user.getPassword();
        String username = user.getUsername();

        int num = companyUserMapper.findUser(email);
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
        StringBuffer UserID = verCodeGenerateUtil.generateUserID();
        while (companyUserMapper.findUserID(String.valueOf(UserID)) == 1) {
            UserID = verCodeGenerateUtil.generateUserID();
        }
        String email = code.getEmail();
        String codes = code.getCode();
        String username = redisService.get(email + "u");
        String realemails = redisService.get(codes);
        if (email.equals(realemails)) {
            companyInfo.setPassword(redisService.get(email + "p") + SIGN);
            companyInfo.setCompanyID(String.valueOf(UserID));
            companyInfo.setEmail(email);
            companyInfo.setC_name(username);
            companyUserMapper.addUserInfo(companyInfo);

            return 1;
        } else {
            return 0;
        }
    }


    @Override
    public int info(CompanyInfo companyInfo, String token) {

        String UserEmail = tokenUtil.getValue(token);
        //注入属性
        companyInfo.setEmail(UserEmail);
        companyInfo.setPicture("R.png");
        companyInfo.setBalance("0");
        String type = companyInfo.getType();

        companyUserMapper.updateUserInfo(companyInfo.getC_Mobile(),companyInfo.getC_address(),companyInfo.getBalance(),companyInfo.getPicture(), type,UserEmail);
        return 1;
    }


    @Override
    public String checkUser(User user) {
        String password = user.getPassword() + SIGN;
        user.setPassword(password);
        int a = companyUserMapper.checkUser(user);
        String b = companyUserMapper.checkUserInfo(user.getEmail());
        String RefreshToken = tokenUtil.generateToken(user);
        String AccessToken = tokenUtil.generateaccessToken(user.getEmail());
        Map<String, Object> map = new HashMap<>();
        if (a != 0) {

            CompanyInfo companyInfo = companyUserMapper.getUserInfo(user.getEmail());

            map.put("UserID", companyInfo.getCompanyID());
            map.put("UserName", companyInfo.getC_name());
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
    public String selectBalanceOf(String token) {
        //从Token中获取信息
        String userEmail = tokenUtil.getValue(token);
        String userId = companyUserMapper.getUserId(userEmail);
        String balanceOf = companyUserMapper.selectBalance(userId);
        Balance balance = new Balance();
        balance.setBalanceof(balanceOf);
        try {
            json = objectMapper.writeValueAsString(balance);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    //账单支付
    @Override
    public String selectBill(String token) {
        //从Token中获取信息
        String userEmail = tokenUtil.getValue(token);
        String userId = companyUserMapper.getUserId(userEmail);
        //查询用户账单
        List<CompanyBill> billList = companyUserMapper.selectBill(userId);


        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        objectMapper.setDateFormat(sdf);
        try {
            json = objectMapper.writeValueAsString(billList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    @Override
    public int changePswd(UserPswd userPswd, String token) {
        User user = new User();
        String receiveEmail = tokenUtil.getValue(token);
        user.setEmail(receiveEmail);
        user.setPassword(userPswd.getOldPswd() + SIGN);
        int a = companyUserMapper.checkUser(user);
        if (a != 0) {
            companyUserMapper.changePswd(userPswd.getNewPswd() + SIGN, receiveEmail);
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int setPswd(User user) {
        String email = user.getEmail();
        String pswd = user.getPassword() + SIGN;
        if (companyUserMapper.findUser(email) != 0) {
            companyUserMapper.changePswd(pswd, email);
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int changeUsername(Usernames usernames, String token) {
        String receiveEmail = tokenUtil.getValue(token);
        try {
            companyUserMapper.changeUsername(usernames.getUsername(), receiveEmail);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }


}
