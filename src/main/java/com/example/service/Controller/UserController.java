package com.example.service.Controller;

import com.example.service.Bean.*;
import com.example.service.Bean.In.*;
import com.example.service.Service.UserService;
import com.example.service.Util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@ResponseBody
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    TokenUtil tokenUtil;

    //注册登录
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
    public Result info(@RequestBody UserInfo userInfo) {
        int num = userService.info(userInfo);
        if (num == 1) {
            return Result.success("注册成功");
        } else {
            return Result.fail("验证码错误");
        }
    }

    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        int num = userService.checkUser(user);
        if (num == 1) {
            String token = tokenUtil.generateToken(user);
            Map<String,Object> map = new HashMap<>();
            map.put("token",token);
            return Result.success("登录成功", map);
        } else {
            return Result.fail("登录失败");
        }
    }


    //账单支付
    @PostMapping("/bill")
    public Result selectBill(@RequestHeader(value = "token") String token) {

        String bill = userService.selectBill(token);

        return Result.success("查询成功",bill);
    }

    @PostMapping("/transfer")
    public Result transferTo(@RequestBody UserID userID, @RequestHeader(value = "token") String token) {
        Result result = new Result();
        int num = userService.transferTo(userID,token);
        if (num == 0) {
            result = result.fail("余额不足");
        }
        if (num == 1) {
            result = result.success("支付成功");
        }
        return result;
    }

    @PostMapping("/checkPayPswd")
    public Result checkPayPswd(@RequestBody PayPswd payPswd, @RequestHeader(value = "token") String token){
        Result result = new Result();
        int num = userService.checkPayPswd(payPswd.getPayPswd(), token);
        if (num == 1) {
            result = result.success("密码正确");
        }else {
            result = result.fail("密码错误");
        }
        return result;
    }

    //充值
    @PostMapping("/create-payment-intent")
    public Result createPaymentIntent(@RequestBody Amount Amounts){
        String json = userService.createPaymentIntent(Amounts);
        Result result = new Result();
        result = result.success("success",json);
        return result;
    }

    @PostMapping("/webhook")
    public Result webhook(@RequestBody String payload,@RequestHeader(value = "Stripe-Signature") String sigHeader){
        userService.webhook(payload, sigHeader);
        return null;
    }

    //用户信息
    @PostMapping("/changePswd")
    public Result changePswd(@RequestBody UserPswd userPswd,@RequestHeader(value = "token") String token){
        int num = userService.changePswd(userPswd, token);
        Result result = new Result();
        if (num == 1) {
             result = result.success("修改成功");
        }
        if (num == 0) {
             result = result.fail("修改失败");
        }
        return result;

    };

    @PostMapping("/changePayPswd")
    public Result changePayPswd(@RequestBody UserPayPswd userPayPswd, @RequestHeader(value = "token") String token){
        int num = userService.changePayPswd(userPayPswd, token);
        Result result = new Result();

        if (num == 1) {
            result = result.success("修改成功");
        }
        if (num == 0) {
            result = result.fail("修改失败");
        }
        return result;

    };










}

