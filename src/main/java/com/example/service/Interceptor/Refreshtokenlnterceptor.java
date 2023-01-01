package com.example.service.Interceptor;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.service.Bean.In.User;
import com.example.service.Mapper.UserMapper;
import com.example.service.Util.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

//拦截器类

public class Refreshtokenlnterceptor implements HandlerInterceptor {

    @Autowired
    public TokenUtil tokenUtil;
    @Autowired
    public UserMapper userMapper;

    Logger logger = LoggerFactory.getLogger(getClass());


    String token = new String();
    Map<String, Object> map = new HashMap<>();


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("进入拦截器");
        Map<String, Object> headerMap = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        token = tokenUtil.getToken(request);
        Enumeration headerNames = request.getHeaderNames();
        logger.info("Header Name - " + "user-agent" + ", Value - " + request.getHeader("user-agent"));
        logger.info("Header Name - " + "token" + ", Value - " + request.getHeader("token"));

        User user = new User();
        if (StringUtils.isEmpty(token)) {
            map.put("msg", "None token");
            map.put("code", "401");
            String json = objectMapper.writeValueAsString(map);
            response.getOutputStream().print(json);
            return false;
        }
        int num = userMapper.checkallUser(tokenUtil.getValue(token));
        if (num == 0) {
            map.put("msg", "No user");
            map.put("code", "400");
            String json = objectMapper.writeValueAsString(map);
            response.getOutputStream().print(json);
            return false;
        }

        String Id = tokenUtil.getJWTId(token);
        if (!(Id.equals("refreshToken"))) {
            map.put("msg", "Type error");
            map.put("code", "402");
            String json = objectMapper.writeValueAsString(map);
            response.getOutputStream().print(json);
            return false;
        }
        //验签
        try {
            DecodedJWT decodedJWT = tokenUtil.verify(token);
            return true;
        } catch (SignatureVerificationException e) {
            e.printStackTrace();
            map.put("msg", "invalid sign");
            map.put("code", "403");
        } catch (TokenExpiredException e) {
            e.printStackTrace();
            map.put("code", "404");
            map.put("msg", "token timeout");

        } catch (AlgorithmMismatchException e) {
            e.printStackTrace();
            map.put("msg", "token算法不一致");
            map.put("code", "405");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "token无效");
            map.put("code", "406");
        }finally {
            map.put("msg", "Network error");
            map.put("code", "500");
        }

        String json = objectMapper.writeValueAsString(map);
//        response.getWriter().append(json);
        response.getOutputStream().print(json);
        return false;

    }
}
