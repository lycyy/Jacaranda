package com.example.service.Bean.Out;

import java.sql.Timestamp;

public class Bill {
    private String type;

    private String payUser;
    private String payColor;
    private String payUsername;
    private String receiveUser;
    private String receiveColor;
    private String receiveUsername;
    private String Amount;
    private Timestamp Date;
    private String Receipt;



    public String getPayColor() {
        return payColor;
    }

    public void setPayColor(String payColor) {
        this.payColor = payColor;
    }

    public String getReceiveColor() {
        return receiveColor;
    }

    public void setReceiveColor(String receiveColor) {
        this.receiveColor = receiveColor;
    }

    public String getReceipt() {
        return Receipt;
    }

    public void setReceipt(String receipt) {
        Receipt = receipt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReceiveUser() {
        return receiveUser;
    }

    public void setReceiveUser(String receiveUser) {
        this.receiveUser = receiveUser;
    }

    public String getReceiveUsername() {
        return receiveUsername;
    }

    public void setReceiveUsername(String receiveUsername) {
        this.receiveUsername = receiveUsername;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public Timestamp getDate() {
        return Date;
    }

    public void setDate(Timestamp date) {
        Date = date;
    }

    public String getPayUser() {
        return payUser;
    }

    public void setPayUser(String payUser) {
        this.payUser = payUser;
    }

    public String getPayUsername() {
        return payUsername;
    }

    public void setPayUsername(String payUsername) {
        this.payUsername = payUsername;
    }
}
