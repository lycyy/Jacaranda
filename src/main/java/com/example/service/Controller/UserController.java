package com.example.service.Controller;

import com.example.service.Bean.*;
import com.example.service.Bean.In.*;
import com.example.service.Enum.Color;
import com.example.service.Mapper.UserMapper;
import com.example.service.Service.EmailService;
import com.example.service.Service.ServiceImpl.DingoMailServiceImpl;
import com.example.service.Service.ServiceImpl.WebSocketService;
import com.example.service.Service.UserService;
import com.example.service.Util.ConfigurationUtil;
import com.example.service.Util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Random;

@RestController
@ResponseBody
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    DingoMailServiceImpl dingoMail;
    @Autowired
    EmailService emailService;
    @Autowired
    TokenUtil tokenUtil;
    @Autowired
    UserMapper userMapper;
    @Autowired
    WebSocketService webSocketService;
    @Autowired
    ConfigurationUtil configuration;

    Logger logger = LoggerFactory.getLogger(getClass());

    Date date = new Date();

    //注册登录
    @RequestMapping("/")
    public String index() {
        return "hello";
    }


    @PostMapping("/email")
    public Result email(@RequestBody User user) {
        logger.info("email interface is call");
        int num = userService.findUser(user);
        if (num == 1) {
            return Result.success("请输入验证码", user);
        } else {
            return Result.fail("邮箱已注册");
        }
    }


    @PostMapping("/code")
    public Result code(@RequestBody Code code) {
        logger.info("code interface is call");
        int num = userService.verifyUser(code);
        if (num == 1) {
            return Result.success("验证成功");
        } else {
            return Result.fail("验证码错误");
        }
    }

    @PostMapping("/info")
    public Result info(@RequestBody UserInfo userInfo , @RequestHeader(value = "token") String token) {
        logger.info("info interface is call");
        int num = userService.info(userInfo,token);
        if (num == 1) {
            return Result.success("信息填写成功");
        } else {
            return Result.fail("信息填写错误");
        }
    }

    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        logger.info("login interface is call");

        String json = userService.checkUser(user);
        if (json.equals("查询错误")) {

            return Result.fail("登录失败");

        } else {

            return Result.success("登录成功", json);

        }
    }

    @PostMapping("/verify_pswdcode")
    public Result testpswdcode(@RequestBody Code code) {
        logger.info("verify_pswdcode interface is call");
        int num = userService.testPswdcode(code);
        Result result = new Result();
        if (num == 1) {
            result = result.success("验证成功");
        } else {
            result = result.fail("验证失败");
        }
        return result;
    }

    @PostMapping("/verify_pincode")
    public Result testpincode(@RequestBody Code code , @RequestHeader(value = "token") String token) {
        logger.info("verify_pincode interface is call");
        int num = userService.testPincode(code,token);
        Result result = new Result();
        if (num == 1) {
            result = result.success("验证成功");
        } else {
            result = result.fail("验证失败");
        }
        return result;
    }

    @PostMapping("/testRefreshToken")
    public Result testRefreshToken(@RequestHeader(value = "token") String token) {
        logger.info("testRefreshToken interface is call");
        String json = userService.testRefreshToken(token);
        Result result = new Result();
        if (json.equals("用户不存在")) {
            result = result.fail("验证失败");
        } else if (json.equals("验证成功")) {
            result = result.success("验证成功");
        } else {
            result = result.success("更新Token", json);
        }
        return result;
    }

    @PostMapping("/testAccessToken")
    public Result testAccessToken(@RequestHeader(value = "token") String token) {
        logger.info("testAccessToken interface is call");
        String json = userService.testAccessToken(token);
        Result result = new Result();
        if (json.equals("用户不存在")) {
            result = result.fail("验证失败");
        } else if (json.equals("验证成功")) {
            result = result.success("验证成功");
        } else {
            result = result.success("更新Token", json);
        }
        return result;
    }


    //账单支付
    @PostMapping("/bill")
    public Result selectBill(@RequestBody Time time , @RequestHeader(value = "token") String token) {
        logger.info("bill interface is call");

        String bill = userService.selectBill(time , token);

        return Result.success("查询成功", bill);
    }

    @PostMapping("/bill_before")
    public Result selectBill_before(@RequestBody Time time , @RequestHeader(value = "token") String token) {
        logger.info("bill_before interface is call");

        String bill = userService.selectBill_before(time , token);

        return Result.success("查询成功", bill);
    }

    @PostMapping("/balanceOf")
    public Result selectbalanceOf(@RequestHeader(value = "token") String token) {
        logger.info("balanceOf interface is call");
        String balanceOf = userService.selectBalanceOf(token);


        return Result.success("查询成功", balanceOf);
    }

    @PostMapping("/transfer")
    public Result transfer(@RequestBody UserID userID, @RequestHeader(value = "token") String token) {
        logger.info("transfer interface is call");
        Result result = new Result();
        int num = userService.Verify(userID, token);
        if (num == 1) {
            result = result.success("等待输入密码");
        }
        return result;
    }

    @PostMapping("/checkTransferTo")
    public Result checkPayPswd(@RequestBody PIN pin, @RequestHeader(value = "token") String token) {
        logger.info("checkTransferTo interface is call");
        Result result = new Result();
        int num = userService.transferto_start(pin, token);
        if (num == 1) {
            result = result.success("支付成功");
        } else if (num == 0) {
            result = result.fail("余额不足");
        } else if (num == -1) {
            result = result.fail("密码错误");
        }else {
            result = result.fail("网络错误");
        }
        return result;
    }

    @PostMapping("/checkTransferTo_Company")
    public Result checkPayPswdC(@RequestBody PIN pin, @RequestHeader(value = "token") String token) {
        logger.info("checkTransferTo interface is call");
        Result result = new Result();
        int num = userService.transfertoCompany_start(pin, token);
        if (num == 1) {
            result = result.success("支付成功");
        } else if (num == 0) {
            result = result.fail("余额不足");
        } else if (num == -1) {
            result = result.fail("密码错误");
        }else {
            result = result.fail("网络错误");
        }
        return result;
    }

    @PostMapping("/test")
    public Result test(@RequestHeader(value = "token") String token) {
        String receiveEmail = tokenUtil.getValue(token);
        String receiveUser = userMapper.getUserId(receiveEmail);
        webSocketService.sendMessage("success", Long.parseLong(receiveUser));
        return null;
    }

    @PostMapping("/transferTo")
    public Result transferTo(@RequestBody UserID userID, @RequestHeader(value = "token") String token) {
        logger.info("transferTo interface is call");
        Result result = new Result();
        String text = userService.create_transferTo(userID, token);
            result = result.success("请求成功" , text );
        return result;
    }

    @PostMapping("/checkID")
    public Result checkID(@RequestBody UserID userID, @RequestHeader(value = "token") String token) {
        logger.info("checkID interface is call");
        Result result = new Result();
        String a = userService.checkId(userID, token);
        if (a.equals("0")) {
            result = result.fail("没有该用户");
        } else if (a.equals("-1")) {
            result = result.fail("转账用户与本用户相同");
        } else {
            result = result.success("用户存在",  a);
        }
        return result;
    }

    //充值
    @PostMapping("/create-payment-intent")
    public Result createPaymentIntent(@RequestBody Amount Amounts, @RequestHeader(value = "token") String token) {
        logger.info("create-payment-intent interface is call");
        String json = userService.createPaymentIntent(Amounts, token);
        Result result = new Result();
        result = result.success("success", json);
        return result;
    }

    @PostMapping("/webhook")
    public Result webhook(@RequestBody String payload, @RequestHeader(value = "Stripe-Signature") String sigHeader) {
        logger.info("webhook interface is call");
        userService.webhook(payload, sigHeader);
        return null;
    }

    //公司信息
    @PostMapping("/selectCompany")
    public Result selectCompany() {
        logger.info("selectCompany interface is call");
        String company = userService.selectCompany();
        return Result.success("查询成功", company);
    }

    //用户信息
    @PostMapping("/getInfo")
    public Result getInfo(@RequestHeader(value = "token") String token) {
        logger.info("getInfo interface is call");
        String json = userService.getInfo(token);

        return Result.success("查询成功", json);

    }

    @PostMapping("/changePswd")
    public Result changePswd(@RequestBody UserPswd userPswd, @RequestHeader(value = "token") String token) {
        logger.info("changePswd interface is call");
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

    @PostMapping("/changePin")
    public Result changePayPswd(@RequestBody UserPin userPin, @RequestHeader(value = "token") String token) {
        logger.info("changePin interface is call");
        int num = userService.changePayPswd(userPin, token);
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
        int num = userService.changeUsername(usernames, token);
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
        logger.info("sendCode interface is call");
        int num = userService.sendEmail(user);
        Result result = new Result();
        if (num == 1) {
            result = result.success("验证码已发送");
        }
        return result;
    }

    @PostMapping("/setPswd")
    public Result setPswd(@RequestBody User user) {
        logger.info("setPswd interface is call");
        int num = userService.setPswd(user);
        Result result = new Result();
        if (num == 1) {
            result = result.success("发送验证码");
        } else {
            result = result.success("发送失败");
        }
        return result;
    }

    @PostMapping("/setPin")
    public Result setPswd(@RequestBody PIN pin, @RequestHeader(value = "token") String token) {
        logger.info("setPin interface is call");
        int num = userService.setPin(pin,token);
        Result result = new Result();
        if (num == 1) {
            result = result.success("发送验证码");
        } else {
            result = result.success("发送失败");
        }
        return result;
    }
    @PostMapping("/getAccesstoken")
    public Result setPswd(@RequestHeader(value = "token") String token) {
        logger.info("getAccesstoken interface is call");
        String AccessToken = userService.getAccessToken(token);
        Result result = new Result();
        result = result.success("GET AccessToken",AccessToken);
        return result;
    }

    @PostMapping("/Get_Promotion")
    public Result Get_Promotion(@RequestHeader(value = "token") String token){
        String json = userService.Get_Promotion(token);
        return Result.success("查询成功",json);
    }
    @PostMapping("/dingomail")
    public void dingomail() {
        int pick = new Random().nextInt(Color.values().length);
        String color = Color.values()[pick].getValue();
        System.out.println(color);
    }
    @PostMapping("/get")
    public Result get(){
        return Result.success(configuration.getRSA_seed()+"||"+configuration.getPassword_salt()+"||"+configuration.getStripe_apiKey()+"||"+configuration.getToken_key());
    }

    @PostMapping("/updateImage")
    public Result updateImage(@RequestBody Images image , @RequestHeader(value = "token") String token ){
        int num = userService.changeColor(image,token);
        Result result = new Result();
        if (num == 1) {
            result = result.success("更新成功");
        }else {
            result = result.fail("更新失败");
        }
        return result;
    }


    


}







