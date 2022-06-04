package com.example.service.Controller;

import com.example.service.Bean.Code;
import com.example.service.Bean.Result;
import com.example.service.Bean.User;
import com.example.service.Bean.UserInfo;
import com.example.service.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@ResponseBody
public class UserController {
    @Autowired
    UserService userService;


    @RequestMapping("/")
    public String index() {
        return "hello";
    }


    @PostMapping("/email")
    public Result email(@RequestBody User user) {
        int num = userService.findUser(user);
        if (num == 1) {

            return Result.success("请输入验证码", user);
        } else {
            return Result.fail("邮箱已注册");
        }
    }

    @PostMapping("/code")
    public Result code(@RequestBody Code code) {
        int num = userService.verifyUser(code);
        if (num == 1) {
            return Result.success("注册成功");
        } else {
            return Result.fail("验证码错误");
        }
    }
    @PostMapping("/info")
    public Result info(@RequestBody UserInfo userInfo){
        int num =userService.info(userInfo);
        if (num == 1) {
            return Result.success("注册成功");
        } else {
            return Result.fail("验证码错误");
        }
    }

    }

