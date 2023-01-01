package com.example.service.Service.ServiceImpl;

import com.example.service.Bean.In.*;
import com.example.service.Bean.Out.Balance;
import com.example.service.Bean.Out.Bill;
import com.example.service.Bean.Out.CompanyBill;
import com.example.service.Bean.Out.Refund;
import com.example.service.Mapper.CompanyUserMapper;
import com.example.service.Mapper.UserMapper;
import com.example.service.Service.*;
import com.example.service.Thread.EmailThread;
import com.example.service.Util.ConfigurationUtil;
import com.example.service.Util.TokenUtil;
import com.example.service.Util.VerCodeGenerateUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.zaxxer.hikari.pool.HikariProxyCallableStatement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CompanyUserServiceImpl implements CompanyUserService {

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
    UserService userService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    CompanyInfo companyInfo;
    @Autowired
    ConfigurationUtil configurationUtil;
    @Autowired
    DingoMailService dingoMailService;

    ObjectMapper objectMapper = new ObjectMapper();
    String salt;
    String json;

    @PostConstruct
    public void init() {
        salt = this.configurationUtil.getPassword_salt();
    }
    @Override
    public int Companyregister(User user) {
        String email = user.getEmail();
        String password = user.getPassword();
        String username = user.getUsername();

        int num = companyUserMapper.findUser(email);
        if (num == 0) {
            String text1 = verCodeGenerateUtil.generateVerCode();
            String text2 = verCodeGenerateUtil.generateVerCode();

            redisService.set(email + "v", text1);
            redisService.setJacarandaCode(email + "JAC", text2);
            redisService.set(email + "p", password);
            redisService.set(email + "u", username);
            System.out.println(text1);
            System.out.println(text2);
            new EmailThread(user.getEmail(), "Verification code", "Register", text1, dingoMailService).start();
            new EmailThread("945009953@qq.com", "Jacaranda Authorization code", "Verification", text2, dingoMailService).start();
            return 1;
        } else {
            return 0;
        }
    }


    @Override
    public int verifyUser(CompanyCode companyCode) {
        StringBuffer UserID = verCodeGenerateUtil.generateUserID();
        while (companyUserMapper.findUserID(String.valueOf(UserID)) == 1) {
            UserID = verCodeGenerateUtil.generateUserID();
        }
        String email = companyCode.getEmail();
        String vcodes = companyCode.getVerification_code();
        String acodes = companyCode.getAuthorization_code();
        String username = redisService.get(email + "u");
        String vcodex = redisService.get(email + "v");
        String acodex = redisService.get(email + "JAC");
        if (!vcodes.equals(vcodex)) {
            return 0;
        }
        if (!acodes.equals(acodex)) {
            return 0;
        }
        companyInfo.setPassword(redisService.get(email + "p") + salt);
        companyInfo.setCompanyID(String.valueOf(UserID));
        companyInfo.setEmail(email);
        companyInfo.setC_name(username);
        companyUserMapper.addUserInfo(companyInfo);
        return 1;
    }


    @Override
    public int info(CompanyInfo companyInfo, String token) {

        String UserEmail = tokenUtil.getValue(token);
        //注入属性
        companyInfo.setEmail(UserEmail);
        companyInfo.setPicture("R.png");
        companyInfo.setBalance("0");


        companyUserMapper.updateUserInfo(companyInfo.getC_Mobile(), companyInfo.getC_address(), companyInfo.getBalance(), companyInfo.getPicture(),  UserEmail);
        return 1;
    }


    @Override
    public String checkUser(User user) {
        String password = user.getPassword() + salt;
        user.setPassword(password);
        int a = companyUserMapper.checkUser(user);
        String b = companyUserMapper.checkUserInfo(user.getEmail());
        String RefreshToken = tokenUtil.generateRefreshToken(user);
        String AccessToken = tokenUtil.generateAccessToken(user.getEmail());
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
    public String selectBill(Time time, String token) {
        //从Token中获取信息
        String times = time.getTime();
        String userEmail = tokenUtil.getValue(token);
        String userId = companyUserMapper.getUserId(userEmail);
        //查询用户账单
        List<CompanyBill> billList = companyUserMapper.selectBill(times, userId);

        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        objectMapper.setDateFormat(sdf);

        for (CompanyBill bill : billList) {
            String Payusername = userMapper.selectUserName(bill.getPayUser());
            bill.setPayUsername(Payusername);
            String PayuserColor = userMapper.selectUserImage(Payusername);
            bill.setPayColor(PayuserColor);
        }

        try {
            json = objectMapper.writeValueAsString(billList).replace("date", "dateString");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    @Override
    public String selectBill_before(Time time, String token) {
        //从Token中获取信息
        String times = time.getTime();
        String userEmail = tokenUtil.getValue(token);
        String userId = companyUserMapper.getUserId(userEmail);
        //查询用户账单
        List<CompanyBill> billList = companyUserMapper.selectBill_before(times, userId);

        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        objectMapper.setDateFormat(sdf);

        for (CompanyBill bill : billList) {
            String Payusername = userMapper.selectUserName(bill.getPayUser());
            bill.setPayUsername(Payusername);
            String PayuserColor = userMapper.selectUserImage(Payusername);
            bill.setPayColor(PayuserColor);
        }

        try {
            json = objectMapper.writeValueAsString(billList).replace("date", "dateString");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    @Override
    public int createCollection(UserID userId, String token) {

        Map map = new HashMap<>();
        FlowBill flowBill = new FlowBill();
        String receiveEmail = tokenUtil.getValue(token);
        String receiveUser = userMapper.getCUserId(receiveEmail);
        flowBill.setReceiveUser(receiveUser);
        flowBill.setPayUser(userId.getUserID());
        flowBill.setAmount(userId.getAmount());
        String fid = verCodeGenerateUtil.generateReceipt_number();
        try {
            json = objectMapper.writeValueAsString(flowBill);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        redisService.set(fid, json);
        map.put("fid", fid);
        map.put("amount", userId.getAmount());

        try {
            json = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        webSocketService.sendMessage(json, Long.parseLong(userId.getUserID()));
        System.out.println(json);
        return 1;
    }

    @Override
    public int changePswd(UserPswd userPswd, String token) {
        User user = new User();
        String receiveEmail = tokenUtil.getValue(token);
        user.setEmail(receiveEmail);
        user.setPassword(userPswd.getOldPswd() + salt);
        int a = companyUserMapper.checkUser(user);
        if (a != 0) {
            companyUserMapper.changePswd(userPswd.getNewPswd() + salt, receiveEmail);
            return 1;
        } else {
            return 0;
        }

    }


    @Override
    public int testPswdcode(Code code) {
        String email = code.getEmail();
        String codes = code.getCode();
        String codex = redisService.get(email + "v");
        if (!codes.equals(codex)) {
            return 0;
        }
        String pswd = redisService.get(email + "pswd") + salt;
        if (companyUserMapper.findUser(email) != 0) {
            companyUserMapper.changePswd(pswd, email);
            return 1;
        } else {
            return 0;
        }
    }


    @Override
    public int setPswd(User user) {
        String email = user.getEmail();
        String text = verCodeGenerateUtil.generateVerCode();
        redisService.set(email + "v", text);
        redisService.set(email + "pswd", user.getPassword());
        new EmailThread(user.getEmail(), "Verification code", "Changing your password", text, dingoMailService).start();
        return 1;
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

    @Override
    public int Publish_Promotion(Promotion promotion) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dates = sdf.format(date);
        promotion.setDate(dates);
        int a = companyUserMapper.Publish_Promotion(promotion);
        if (a == 1) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int refund(Receipt receipt, String token) {
        String userEmail = tokenUtil.getValue(token);
        String receiveUser = companyUserMapper.getUserId(userEmail);
        String receipts = receipt.getReceipt();
        Refund refund = companyUserMapper.selectReceipt(receipts);
        int num = transferTo(receiveUser, refund.getPayUser(), refund.getAmount());
        if (num == 1) {
            companyUserMapper.deletebill(receipts);
        }
        return num;

    }

    @Override
    public int transferTo(String payUser, String receiveUser, String Amount) {

        Date date = new Date();

        float amount = Float.parseFloat(Amount);
        float payBalance = Float.parseFloat(companyUserMapper.selectBalance(payUser));

        float receiveBalance = Float.parseFloat(userMapper.selectBalance(receiveUser));

        if (payBalance < amount) {
            return 0;//余额不足
        }
        float payBalances = payBalance - amount;
        companyUserMapper.updateCompanyBalance(String.valueOf(payBalances), payUser);
        float receiveBalances = receiveBalance + amount;
        userMapper.updateBalance(String.valueOf(receiveBalances), receiveUser);

        //存入数据库

        return 1;//成功

    }
}
