package com.example.service.Bean.Out;

import java.sql.Timestamp;

public class CompanyBill {
    private String payUser;
    private String Amount;
    private Timestamp Date;

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
