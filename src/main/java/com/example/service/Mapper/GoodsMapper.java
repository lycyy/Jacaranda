package com.example.service.Mapper;

import com.example.service.Bean.In.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface GoodsMapper {
    @Select("select Goods_id,Goods_Name,Price,quantity from goods_inventory")
    List<Goods> selectallGoods();

}
