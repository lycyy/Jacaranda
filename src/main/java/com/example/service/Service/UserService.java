package com.example.service.Service;

import com.example.service.Bean.Code;
import com.example.service.Bean.In.*;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public int findUser(User user);
    public int verifyUser(Code code);
    public int info(UserInfo userInfo);
    public int checkUser(User user);
    public String selectBill(String token);
    public int transferTo(UserID userId,String token);
    public int changePswd(UserPswd userPswd,String token);
    public int changePayPswd(UserPayPswd userPayPswd, String token);
    public int checkPayPswd(String PayPswd, String token);
    public String createPaymentIntent(Amount Amount);
    public void webhook(String payload,String sigHeader);
}
