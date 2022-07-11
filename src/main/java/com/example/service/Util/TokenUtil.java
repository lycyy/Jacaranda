package com.example.service.Util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.service.Bean.In.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class TokenUtil {
    public TokenUtil() {
    }
    Algorithm algorithm = Algorithm.HMAC256("secret");

    //生成Token
    public String generateToken(User user){
        Date start = new Date();
        long currentTime = System.currentTimeMillis() + 60 * 60 * 1000;//一小时
        Date end = new Date();
        String token;
        token = JWT.create()
                .withAudience(user.getEmail())
                .withIssuedAt(start)
                .withExpiresAt(end)
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

//    public Boolean getTokens(String token){
//        JWTVerifier verifier = JWT.require(algorithm)
//                .build();
//        DecodedJWT jwt = verifier.verify(token);
//        System.out.println(jwt);
//        return true;
//
//    }
}
