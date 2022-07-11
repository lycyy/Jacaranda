package com.example.service.Bean;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.Serializable;
@Component
public class Result implements Serializable {
    private String code;
    private String msg;
    private Object data;



    public static Result success(String msg){
        Result result = new Result();
        result.setCode("200");
        result.setMsg(msg);
        result.setData(null);
        return result;
    }

    public static Result success(String msg ,Object data){
        Result result = new Result();
        result.setCode("200");
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static Result fail(String msg){
        Result result = new Result();
        result.setCode("400");
        result.setMsg(msg);
        result.setData(null);
        return result;
    }

    public static Result fail(String msg,Object data){
        Result result = new Result();
        result.setCode("400");
        result.setMsg(msg);
        result.setData(data);
        return result;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
