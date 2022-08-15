package com.example.service.Controller;

import com.example.service.Bean.*;
import com.example.service.Bean.In.*;
import com.example.service.Mapper.UserMapper;
import com.example.service.Service.ServiceImpl.WebSocketService;
import com.example.service.Service.UserService;
import com.example.service.Util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;


import javax.websocket.server.PathParam;
import java.util.Date;

@RestController
@ResponseBody
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    TokenUtil tokenUtil;
    @Autowired
    UserMapper userMapper;

    @Autowired
    WebSocketService webSocketService;

    //注册登录
    @RequestMapping("/")
    public String index() {
        return "hello";
    }

    @PostMapping("/email")
    public Result email(@RequestBody User user) {

        System.out.println("email:"+new Date(System.currentTimeMillis()));
        System.out.println("-----------------------------------------------------------------------------------------------");
        int num = userService.findUser(user);
        if (num == 1) {
            return Result.success("请输入验证码", user);
        } else {
            return Result.fail("邮箱已注册");
        }
    }

    @PostMapping("/code")
    public Result code(@RequestBody Code code) {
        System.out.println("code:"+new Date(System.currentTimeMillis()));
        System.out.println("-----------------------------------------------------------------------------------------------");
        int num = userService.verifyUser(code);
        if (num == 1) {
            return Result.success("验证成功");
        } else {
            return Result.fail("验证码错误");
        }
    }

    @PostMapping("/info")
    public Result info(@RequestBody UserInfo userInfo) {
        System.out.println("info:"+new Date(System.currentTimeMillis()));
        System.out.println("-----------------------------------------------------------------------------------------------");
        int num = userService.info(userInfo);
        if (num == 1) {
            return Result.success("注册成功");
        } else {
            return Result.fail("验证码错误");
        }
    }

    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        System.out.println("login:"+new Date(System.currentTimeMillis()));
        System.out.println("-----------------------------------------------------------------------------------------------");
        String json = userService.checkUser(user);
        if (json.equals("查询错误")) {

            return Result.fail("登录失败");

        } else {

            return Result.success("登录成功", json);

        }
    }

    @PostMapping("/testcode")
    public Result testcode(@RequestBody Code code) {
        System.out.println("testcode:"+new Date(System.currentTimeMillis()));
        System.out.println("-----------------------------------------------------------------------------------------------");
        int num = userService.testcode(code);
        Result result = new Result();
        if (num == 1) {
            result = result.success("验证成功");
        } else {
            result = result.fail("验证失败");
        }
        return result;
    }

    @PostMapping("/testToken")
    public Result testToken(@RequestHeader(value = "token") String token) {
        System.out.println("testToken:"+new Date(System.currentTimeMillis()));
        System.out.println("-----------------------------------------------------------------------------------------------");
        String json = userService.testToken(token);
        Result result = new Result();
        if (json.equals("用户不存在")) {
            result = result.fail("验证失败");
        } else if (json.equals("验证成功")) {
            result = result.success("验证成功");
        } else {
            result = result.success("更新Token", token);
        }
        return result;
    }


    //账单支付
    @PostMapping("/bill")
    public Result selectBill(@RequestHeader(value = "token") String token) {
        System.out.println("bill:"+new Date(System.currentTimeMillis()));
        System.out.println("-----------------------------------------------------------------------------------------------");

        String bill = userService.selectBill(token);

        return Result.success("查询成功", bill);
    }

    @PostMapping("/balanceOf")
    public Result selectbalanceOf(@RequestHeader(value = "token") String token) {
        System.out.println("balanceOf:"+new Date(System.currentTimeMillis()));
        System.out.println("-----------------------------------------------------------------------------------------------");
        String balanceOf = userService.selectBalanceOf(token);


        return Result.success("查询成功", balanceOf);
    }

    @PostMapping("/transfer")
    public Result transfer(@RequestBody UserID userID, @RequestHeader(value = "token") String token) {
        System.out.println("transfer:"+new Date(System.currentTimeMillis()));
        System.out.println("-----------------------------------------------------------------------------------------------");
        Result result = new Result();
        int num = userService.Verify(userID,token);
        if (num == 1) {
            result = result.success("支付成功");
        }else if (num == 0) {
            result = result.fail("余额不足");
        } else if (num == -1) {
            result = result.fail("连接超时");
        }
        return result;
    }

    @PostMapping("/checkPayPswd")
    public Result checkPayPswd(@RequestBody PayPswd payPswd, @RequestHeader(value = "token") String token) {
        System.out.println("checkPayPswd:"+new Date(System.currentTimeMillis()));
        System.out.println("-----------------------------------------------------------------------------------------------");
        Result result = new Result();
        int num = userService.checkPayPswd(payPswd.getPayPswd(), token);
        if (num == 1) {
            result = result.success("密码正确");
        } else {
            result = result.fail("密码错误");
        }
        return result;
    }

    @PostMapping("/test")
    public Result test(@RequestHeader(value = "token") String token){
        String receiveEmail = tokenUtil.getValue(token);
        String receiveUser = userMapper.getUserId(receiveEmail);
        webSocketService.sendMessage("success", Long.parseLong(receiveUser));
        return null;
    }

    @PostMapping("/transferTo")
    public Result transferTo(@RequestBody UserID userID, @RequestHeader(value = "token") String token) {
        System.out.println("transferTo:"+new Date(System.currentTimeMillis()));
        System.out.println("-----------------------------------------------------------------------------------------------");
        Result result = new Result();
        int num = userService.transferTo(userID,token);
        if (num == 1) {
            result = result.success("支付成功");
        }else if (num == 0) {
            result = result.fail("余额不足");
        } else if (num == -1) {
            result = result.fail("用户错误");
        }
        return result;
    }

    @PostMapping("/checkID")
    public Result checkID(@RequestBody UserID userID,@RequestHeader(value = "token")String token){
        Result result = new Result();
        int a = userService.checkId(userID,token);
        if (a == 0) {
            result = result.fail("没有该用户");
        }else if (a == -1){
            result = result.fail("转账用户与本用户相同");
        }else {
            result = result.success("有该用户");
        }
        return result;
    }

    //充值
    @PostMapping("/create-payment-intent")
    public Result createPaymentIntent(@RequestBody Amount Amounts, @RequestHeader(value = "token") String token) {
        System.out.println("create-payment-intent:"+new Date(System.currentTimeMillis()));
        System.out.println("-----------------------------------------------------------------------------------------------");
        String json = userService.createPaymentIntent(Amounts, token);
        Result result = new Result();
        result = result.success("success", json);
        return result;
    }

    @PostMapping("/webhook")
    public Result webhook(@RequestBody String payload, @RequestHeader(value = "Stripe-Signature") String sigHeader) {
        System.out.println("webhook:"+new Date(System.currentTimeMillis()));
        System.out.println("-----------------------------------------------------------------------------------------------");
        userService.webhook(payload, sigHeader);
        return null;
    }


    //用户信息
    @PostMapping("/getInfo")
    public Result getInfo(@RequestHeader(value = "token") String token) {
        System.out.println("getInfo:"+new Date(System.currentTimeMillis()));
        System.out.println("-----------------------------------------------------------------------------------------------");
        String json = userService.getInfo(token);

        return Result.success("查询成功", json);

    }

    @PostMapping("/changePswd")
    public Result changePswd(@RequestBody UserPswd userPswd, @RequestHeader(value = "token") String token) {
        System.out.println("changePswd:"+new Date(System.currentTimeMillis()));
        System.out.println("-----------------------------------------------------------------------------------------------");
        int num = userService.changePswd(userPswd, token);
        Result result = new Result();
        if (num == 1) {
            result = result.success("修改成功");
        }
        if (num == 0) {
            result = result.fail("修改失败");
        }
        return result;

    }

    @PostMapping("/changePayPswd")
    public Result changePayPswd(@RequestBody UserPayPswd userPayPswd, @RequestHeader(value = "token") String token) {
        System.out.println("changePayPswd:"+new Date(System.currentTimeMillis()));
        System.out.println("-----------------------------------------------------------------------------------------------");
        int num = userService.changePayPswd(userPayPswd, token);
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
    public Result changeUsername(@RequestBody Username username, @RequestHeader(value = "token") String token) {
        System.out.println("changeUsername:"+new Date(System.currentTimeMillis()));
        System.out.println("-----------------------------------------------------------------------------------------------");
        int num = userService.changeUsername(username, token);
        Result result = new Result();
        if (num == 1) {
            result = result.success("修改成功");
        }
        if (num == 0) {
            result = result.fail("修改失败");
        }
        return result;
    }

    @PostMapping("/sendCode")
    public Result forgotPswd(@RequestBody User user) {
        System.out.println("sendCode:"+new Date(System.currentTimeMillis()));
        System.out.println("-----------------------------------------------------------------------------------------------");
        int num = userService.sendEmail(user);
        Result result = new Result();
        if (num == 1) {
            result = result.success("验证码已发送");
        }
        return result;
    }

    @PostMapping("/setPswd")
    public Result setPswd(@RequestBody User user) {
        System.out.println("setPswd:"+new Date(System.currentTimeMillis()));
        System.out.println("-----------------------------------------------------------------------------------------------");
        int num = userService.setPswd(user);
        Result result = new Result();
        if (num == 1) {
            result = result.success("修改成功");
        } else {
            result = result.success("修改失败");
        }
        return result;
    }


}







