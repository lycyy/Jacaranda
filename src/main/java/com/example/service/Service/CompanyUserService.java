package com.example.service.Service;

import com.example.service.Bean.In.*;
import org.springframework.stereotype.Service;

@Service
public interface CompanyUserService {
    int Companyregister(User user);
    String checkUser(User user);
    int info(CompanyInfo CompanyInfo , String token);
    String selectBalanceOf(String token);
    String selectBill(String token);
    int changePswd(UserPswd userPswd, String token);
    public int setPswd(User user);
    int changeUsername(Usernames usernames, String token);
    int verifyUser(Code code);
    int Publish_Promotion(Promotion promotion);
}
