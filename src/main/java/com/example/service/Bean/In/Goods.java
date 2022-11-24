package com.example.service.Bean.In;

public class Goods {
    private String Goods_ID;
    private String Goods_Name;
    public String price;
    public String quantity;

    public String getGoods_ID() {
        return Goods_ID;
    }

    public void setGoods_ID(String goods_ID) {
        Goods_ID = goods_ID;
    }

    public String getGoods_Name() {
        return Goods_Name;
    }

    public void setGoods_Name(String goods_Name) {
        Goods_Name = goods_Name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
