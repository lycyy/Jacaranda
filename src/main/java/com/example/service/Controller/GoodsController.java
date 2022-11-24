package com.example.service.Controller;

import com.example.service.Bean.Result;
import com.example.service.Service.GoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@ResponseBody
public class GoodsController {
    @Autowired
    GoodsService goodsService;
    Logger logger = LoggerFactory.getLogger(getClass());
    @PostMapping("/select_allGoods")
    public Result select_allGoods(@RequestHeader(value = "token") String token){
        logger.info("select_allGoods interface is call");
        String allGoods = goodsService.allGoods();
        return Result.success("查询成功",allGoods);
    }
}
