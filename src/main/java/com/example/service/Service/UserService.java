package com.example.service.Service;

import com.example.service.Bean.Code;
import com.example.service.Bean.In.*;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public int findUser(User user);
    public int verifyUser(Code code);
    public int info(UserInfo userInfo);
    public String checkUser(User user);
    public String testToken(String token);
    public String selectBill(String token);
    public String selectBalanceOf(String token);
    public String addBalance(String amount,String cid);
    public int transferTo(UserID userId,String token);
    public int changePswd(UserPswd userPswd,String token);
    public int changePayPswd(UserPayPswd userPayPswd, String token);
    public int checkPayPswd(String PayPswd, String token);
    public int sendEmail(User user);
    public int testcode(Code code);
    public int setPswd(User user);
    public String createPaymentIntent(Amount Amount,String token);
    public String webhook(String payload,String sigHeader);
    public int changeUsername(Username username, String token);
    public String getInfo(String token);
}
