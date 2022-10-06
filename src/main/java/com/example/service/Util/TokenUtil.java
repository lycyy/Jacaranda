package com.example.service.Util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.service.Bean.In.User;
import com.example.service.Controller.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;

@Component
public class TokenUtil {




    Constant constant = new Constant();

    public TokenUtil() {
    }
    String sign = constant.getToken_key();
    Algorithm algorithm = Algorithm.HMAC256(sign);

    //生成Token
    public String generateToken(User user){

        Date date = new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);
        String token;
        token = JWT.create()
                .withAudience(user.getEmail())
                .withJWTId("refreshToken")
                .withExpiresAt(date)
                .sign(algorithm);
        return token;
    }

    public String generateaccessToken(String email){


        Date date = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
        String token;
        token = JWT.create()
                .withAudience(email)
                .withJWTId("accessToken")
                .withExpiresAt(date)
                .sign(algorithm);
        return token;
    }

    public String getToken(HttpServletRequest request){
        String token = request.getHeader("token");
        return token;
    }

    public String getValue(String token){
        String value = JWT.decode(token).getAudience().get(0);
        return value;
    }

    public String getJWTId(String token){
        String value = JWT.decode(token).getId();
        return value;
    }

    public int getExpiresAt(DecodedJWT decodedJWT){
        Date date = new Date();
        Date JWTdate = decodedJWT.getExpiresAt();
        Calendar calender1 = Calendar.getInstance();
        Calendar calender2 = Calendar.getInstance();
        calender1.setTime(date);
        calender2.setTime(JWTdate);

        int dates = calender1.get(Calendar.DATE);
        int JWTdates = calender2.get(Calendar.DATE);

        int refresh = JWTdates - dates;
        if(refresh <= 2){
            return 1;
        }else {
            return 0;
        }

    }

    public  DecodedJWT verify(String token){
        return JWT.require(algorithm).build().verify(token);
    }

}
