package com.example.service.Service.ServiceImpl;

import com.example.service.Bean.In.Goods;
import com.example.service.Mapper.GoodsMapper;
import com.example.service.Service.GoodsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {
    String json;

    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    GoodsMapper goodsMapper;
    @Override
    public String allGoods() {
        List<Goods> goodsList = goodsMapper.selectallGoods();
        try {
            json = objectMapper.writeValueAsString(goodsList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
}
