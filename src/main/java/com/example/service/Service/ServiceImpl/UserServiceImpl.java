package com.example.service.Service.ServiceImpl;

import com.example.service.Bean.Code;
import com.example.service.Bean.User;
import com.example.service.Bean.UserInfo;
import com.example.service.Mapper.UserMapper;
import com.example.service.Service.EmailService;
import com.example.service.Service.RedisService;
import com.example.service.Service.UserService;
import com.example.service.Thread.EmailThread;
import com.example.service.VerCodeGenerateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    EmailService emailService;
    @Autowired
    RedisService redisService;

    VerCodeGenerateUtil verCodeGenerateUtil;

    @Override
    public int findUser(User user) {
        String email = user.getEmail();
        String password = user.getPassword();
        User user1 = userMapper.findUser(email);
        if (user1 == null) {
            String text = verCodeGenerateUtil.generateVerCode();
            redisService.set(email, password);
            redisService.set(text, email);
            new EmailThread(user.getEmail(), "subject", text, emailService).start();
            return 1;
        } else {
            return 0;
        }


    }

    @Override
    public int verifyUser(Code code) {
        String email = code.getEmail();
        String codes = code.getCode();
        String realemails = redisService.get(codes);

        if (email.equals(realemails)) {
            User user = new User();
            user.setEmail(email);
            user.setPassword(redisService.get(email));
            userMapper.addUser(user);
            return 1;
        } else {
            return 0;
        }
    }


    @Override
    public int info(UserInfo userInfo) {

        String UserID = verCodeGenerateUtil.generateUserID();
        if (userMapper.findUserID(UserID) == null) {
            userInfo.setUserID(UserID);
            userMapper.addUserInfo(userInfo);
        } else {
            String UserIDS = verCodeGenerateUtil.generateUserID();
            userInfo.setUserID(UserIDS);
            userMapper.addUserInfo(userInfo);
        }

        return 1;
    }
}
