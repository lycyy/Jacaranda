package com.example.service.Controller;

import com.example.service.Bean.In.CompanyInfo;
import com.example.service.Bean.In.User;
import com.example.service.Bean.In.UserInfo;
import com.example.service.Bean.Result;
import com.example.service.Service.CompanyUserService;
import com.example.service.Service.ServiceImpl.CompanyUserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@ResponseBody
public class CompanyUserController {
    @Autowired
    CompanyUserService companyUserService;
    Logger logger = LoggerFactory.getLogger(getClass());

    @PostMapping("/C_register")
    public Result register(@RequestBody User user){
        logger.info("C_register interface is call");
        int num = companyUserService.Companyregister(user);
        if (num == 1) {
            return Result.success("注册成功");
        } else {
            return Result.fail("账号已存在");
        }
    }
    @PostMapping("/C_info")
    public Result info(@RequestBody CompanyInfo companyInfo) {
        logger.info("C_info interface is call");
        int num = companyUserService.info(companyInfo);
        if (num == 1) {
            return Result.success("注册成功");
        } else {
            return Result.fail("验证码错误");
        }
    }

    @PostMapping("/C_login")
    public Result login(@RequestBody User user) {
        logger.info("C_login interface is call");
        String json = companyUserService.checkUser(user);
        if (json.equals("查询错误")) {

            return Result.fail("登录失败");

        } else {

            return Result.success("登录成功", json);

        }
    }

    @PostMapping("/C_balanceOf")
    public Result selectbalanceOf(@RequestHeader(value = "token") String token) {
        logger.info("balanceOf interface is call");
        String balanceOf = companyUserService.selectBalanceOf(token);
        return Result.success("查询成功", balanceOf);
    }
}
