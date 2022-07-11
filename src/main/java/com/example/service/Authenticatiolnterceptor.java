package com.example.service;

import com.example.service.Bean.In.User;
import com.example.service.Mapper.UserMapper;
import com.example.service.Util.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

//拦截器类

public class Authenticatiolnterceptor implements HandlerInterceptor {

    @Autowired
    public TokenUtil tokenUtil ;
    @Autowired
    public UserMapper userMapper;

    String token = new String();
    Map<String, Object> map = new HashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("进入拦截器");
        ObjectMapper objectMapper = new ObjectMapper();
        token = tokenUtil.getToken(request);
        User user = new User();
        if (StringUtils.isEmpty(token)) {
            map.put("code", "401");
            map.put("msg", "None token");
            String json = objectMapper.writeValueAsString(map);
            response.getWriter().append(json);
            return false;
        }
        user = userMapper.findUser(tokenUtil.getValue(token));

        if (user == null) {
            map.put("code", "400");
            map.put("msg", "No user");
            String json = objectMapper.writeValueAsString(map);
            response.getWriter().append(json);
            return false;
        }
        return true;

    }
}
