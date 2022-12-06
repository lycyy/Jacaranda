package com.example.service.Bean.In;

import org.springframework.stereotype.Component;

@Component
public class UserInfo {
    private String UserID;

    private String password;
    private String email;
    private String username;

    private String pin;
    private String Balance;
    private String CustomerId;
    private String PictureName;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPictureName() {
        return PictureName;
    }

    public void setPictureName(String pictureName) {
        PictureName = pictureName;
    }
}
