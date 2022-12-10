package com.example.service.Controller;

import com.example.service.Bean.In.*;
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
@RequestMapping("/company")
public class CompanyUserController {
    @Autowired
    CompanyUserService companyUserService;
    Logger logger = LoggerFactory.getLogger(getClass());

    @PostMapping("/authorizationCode")
    public Result authorization(@RequestBody Verify verify) {
        logger.info("Verify interface is call");
        if (verify.getVerify().equals("WD5AHI")) {
            return Result.success("验证成功");
        } else {
            return Result.fail("验证失败");
        }
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        logger.info("C_register interface is call");
        int num = companyUserService.Companyregister(user);
        if (num == 1) {
            return Result.success("请输入验证码");
        } else {
            return Result.fail("账号已存在");
        }
    }

    @PostMapping("/code")
    public Result code(@RequestBody Code code) {
        logger.info("code interface is call");
        int num = companyUserService.verifyUser(code);
        if (num == 1) {
            return Result.success("验证成功");
        } else {
            return Result.fail("验证码错误");
        }
    }

    @PostMapping("/info")
    public Result info(@RequestBody CompanyInfo companyInfo ,  @RequestHeader(value = "token") String token) {
        logger.info("C_info interface is call");
        int num = companyUserService.info(companyInfo,token);
        if (num == 1) {
            return Result.success("信息填写成功");
        } else {
            return Result.fail("信息填写错误");
        }
    }

    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        logger.info("C_login interface is call");
        String json = companyUserService.checkUser(user);
        if (json.equals("查询错误")) {

            return Result.fail("登录失败");

        } else {

            return Result.success("登录成功", json);

        }
    }

    @PostMapping("/balanceOf")
    public Result selectbalanceOf(@RequestHeader(value = "token") String token) {
        logger.info("balanceOf interface is call");
        String balanceOf = companyUserService.selectBalanceOf(token);
        return Result.success("查询成功", balanceOf);
    }

    @PostMapping("/changePswd")
    public Result changePswd(@RequestBody UserPswd userPswd, @RequestHeader(value = "token") String token) {
        logger.info("changePswd interface is call");
        int num = companyUserService.changePswd(userPswd, token);
        Result result = new Result();
        if (num == 1) {
            result = result.success("修改成功");
        }
        if (num == 0) {
            result = result.fail("修改失败");
        }
        return result;

    }

    @PostMapping("/changeUsername")
    public Result changeUsername(@RequestBody Usernames usernames, @RequestHeader(value = "token") String token) {
        logger.info("changeUsername interface is call");
        int num = companyUserService.changeUsername(usernames, token);
        Result result = new Result();
        if (num == 1) {
            result = result.success("修改成功");
        }
        if (num == 0) {
            result = result.fail("修改失败");
        }
        return result;
    }

    @PostMapping("/setPswd")
    public Result setPswd(@RequestBody User user) {
        logger.info("setPswd interface is call");
        int num = companyUserService.setPswd(user);
        Result result = new Result();
        if (num == 1) {
            result = result.success("修改成功");
        } else {
            result = result.fail("修改失败");
        }
        return result;
    }

    @PostMapping("/bill")
    public Result selectBill(@RequestBody Time time,@RequestHeader(value = "token") String token) {
        logger.info("bill interface is call");

        String bill = companyUserService.selectBill(time ,token);

        return Result.success("查询成功", bill);
    }

    @PostMapping("/Publish_Promotion")
    public Result Publish_Promotion(@RequestBody Promotion promotion ,@RequestHeader(value = "token") String token){
        logger.info("Publish_Promotion interface is call");
        Result result = new Result();
        int num = companyUserService.Publish_Promotion(promotion);
        if (num == 1) {
            result = result.success("新增成功");
        } else {
            result = result.fail("新增失败");
        }
        return result;
    }

    @PostMapping("/Refund")
    public Result Refund(@RequestBody Receipt receipt ,@RequestHeader(value = "token") String token){
        logger.info("Refund interface is call");
        Result result = new Result();
        int num = companyUserService.refund(receipt , token);
        if (num == 0) {
            result = result.fail("余额不足");
        } else {
            result = result.success("退款成功");
        }
        return result;
    }
}
