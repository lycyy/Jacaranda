package com.example.service.Controller;

import com.example.service.Bean.RemoteOperation;
import com.example.service.Service.ServiceImpl.WebSocketService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSocketController {
    ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(value = "/operation/{vmc}/{cmd}")
    public String remote(@PathVariable("vmc") long vmc, @PathVariable("cmd") String cmd){
        System.out.print("remote");
        RemoteOperation operation = new RemoteOperation();
        operation.setVmc_no(vmc);
        operation.setOperation(cmd);

        String message = null;
        try {
            message = objectMapper.writeValueAsString(operation);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println("message in json is :"+message);
        return WebSocketService.sendMessage(message,vmc);
    }
    @RequestMapping(value = "/test")
    public String test(){
        System.out.print("test");
        return "hello world";
    }
}
