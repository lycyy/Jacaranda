package com.example.service.Service;

import com.example.service.Bean.Code;
import com.example.service.Bean.User;
import com.example.service.Bean.UserInfo;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public int findUser(User user);
    public int verifyUser(Code code);
    public int info(UserInfo userInfo);
}
