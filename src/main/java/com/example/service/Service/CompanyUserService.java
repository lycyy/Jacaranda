package com.example.service.Service;

import com.example.service.Bean.In.CompanyInfo;
import com.example.service.Bean.In.User;
import com.example.service.Bean.In.UserInfo;
import org.springframework.stereotype.Service;

@Service
public interface CompanyUserService {
    int Companyregister(User user);
    String checkUser(User user);
    int info(CompanyInfo CompanyInfo);
    String selectBalanceOf(String token);
}
