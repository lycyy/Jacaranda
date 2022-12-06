package com.example.service.Service;

import com.example.service.Bean.In.Code;
import com.example.service.Bean.In.*;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    int findUser(User user);

    int verifyUser(Code code);

    int username(User user);

    int info(UserInfo userInfo, String token);

    String checkUser(User user);

    String testRefreshToken(String token);

    String testAccessToken(String token);

    String selectBill(Time time , String token);

    String selectBalanceOf(String token);

    String addBalance(String amount, String cid);

    int Verify(UserID userId, String token);

    int transferTo(UserID userId, String token);

    String checkId(UserID userId, String token);

    int changePswd(UserPswd userPswd, String token);

    int changePayPswd(UserPin userPin, String token);

    int checkPayPswd(String PayPswd, String token);

    int sendEmail(User user);

    int testcode(Code code);

    int setPswd(User user);

    public int setPin(PIN pin , String token);

    String createPaymentIntent(Amount Amount, String token);

    String webhook(String payload, String sigHeader);

    int changeUsername(Usernames usernames, String token);

    String getInfo(String token);

    int transfer(UserID userId, String token);

    String selectCompany();

    String getAccessToken(String token);

    String Get_Promotion(String token);

}
