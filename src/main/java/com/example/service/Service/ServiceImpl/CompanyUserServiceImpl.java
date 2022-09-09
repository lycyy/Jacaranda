package com.example.service.Service.ServiceImpl;

import com.example.service.Bean.In.CompanyInfo;
import com.example.service.Bean.In.User;
import com.example.service.Bean.In.UserInfo;
import com.example.service.Bean.Out.Balance;
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
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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

    ObjectMapper objectMapper = new ObjectMapper();

    String json;

    @Override
    public int Companyregister(User user) {
        String email = user.getEmail() ;
        String password = user.getPassword() + SIGN;
        user.setPassword(password);
        int num = companyUserMapper.findUser(email);
        if (num == 0) {
            companyUserMapper.addUser(user);
            return 1;
        }else {
            return 0;
        }
    }


    @Override
    public int info(CompanyInfo companyInfo) {
        String UserID = verCodeGenerateUtil.generateUserID();
        if (companyUserMapper.findUserID(UserID) == 0) {
            //注入属性
            companyInfo.setCompanyID(UserID);
            companyInfo.setPicture("R.png");
            companyInfo.setBalance("0");
            companyUserMapper.addUserInfo(companyInfo);
        } else {
            String UserIDS = verCodeGenerateUtil.generateUserID();
            companyInfo.setCompanyID(UserIDS);
            companyUserMapper.addUserInfo(companyInfo);
        }
        return 1;
    }


    @Override
    public String checkUser(User user) {
        String password = user.getPassword() + SIGN;
        user.setPassword(password);
        User user1 = companyUserMapper.checkUser(user);
        if (user1 != null) {
            CompanyInfo companyInfo = companyUserMapper.getUserInfo(user.getEmail());
            String token = tokenUtil.generateToken(user);
            Map<String, Object> map = new HashMap<>();
            map.put("UserID", companyInfo.getCompanyID());
            map.put("UserName", companyInfo.getC_name());
            map.put("token", token);

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

}
