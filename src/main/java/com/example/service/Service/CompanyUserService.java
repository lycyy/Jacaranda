package com.example.service.Service;

import com.example.service.Bean.In.*;
import org.springframework.stereotype.Service;

@Service
public interface CompanyUserService {
    int Companyregister(User user);
    String checkUser(User user);
    int info(CompanyInfo CompanyInfo , String token);
    String selectBalanceOf(String token);
    String selectBill(Time time , String token);
    String selectBill_before(Time time, String token);
    int changePswd(UserPswd userPswd, String token);
    int setPswd(User user);
    int testPswdcode (Code code);
    int changeUsername(Usernames usernames, String token);
    int verifyUser(CompanyCode companyCode);
    int Publish_Promotion(Promotion promotion);
    int refund(Receipt receipt,String token);
    int transferTo(String payUser, String receiveUser, String Amount);
    int createCollection(UserID userId, String token);
}
