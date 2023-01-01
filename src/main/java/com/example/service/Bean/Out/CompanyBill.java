package com.example.service.Bean.Out;

import java.sql.Timestamp;

public class CompanyBill {
    private String payUser;
    private String payUsername;
    private String payColor;
    private String Amount;
    private Timestamp Date;
    private String Receipt;

    public String getPayUsername() {
        return payUsername;
    }

    public void setPayUsername(String payUsername) {
        this.payUsername = payUsername;
    }

    public String getPayColor() {
        return payColor;
    }

    public void setPayColor(String payColor) {
        this.payColor = payColor;
    }

    public String getReceipt() {
        return Receipt;
    }

    public void setReceipt(String receipt) {
        Receipt = receipt;
    }

    public String getPayUser() {
        return payUser;
    }

    public void setPayUser(String payUser) {
        this.payUser = payUser;
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
}
