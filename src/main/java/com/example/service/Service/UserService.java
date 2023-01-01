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

    String selectBill_before(Time time, String token);

    String selectBalanceOf(String token);

    String addBalance(String amount, String cid);

    String create_transferTo(UserID userId, String token);

    int transferTo(String payUser,String receiveUser , String Amount);

    int transferToCompany(String payUser,String receiveUser , String Amount);

    String checkId(UserID userId, String token);

    int changePswd(UserPswd userPswd, String token);

    int changePayPswd(UserPin userPin, String token);

    int transferto_start(PIN pin, String token);

    int transfertoCompany_start(PIN pin, String token);

    int sendEmail(User user);

    int testPswdcode(Code code);
    int testPincode(Code code , String token);

    int setPswd(User user);

     int setPin(PIN pin , String token);

    String createPaymentIntent(Amount Amount, String token);

    String webhook(String payload, String sigHeader);

    int changeUsername(Usernames usernames, String token);

    String getInfo(String token);



    String selectCompany();

    String getAccessToken(String token);

    String Get_Promotion(String token);
    int changeColor(Images image , String token);
}
