package com.example.service.Bean;

public class UserInfo {
    private String UserID;
    private String email;
    private String username;
    private String Mnumber;
    private String paypassword;

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

    public String getMnumber() {
        return Mnumber;
    }

    public void setMnumber(String mnumber) {
        Mnumber = mnumber;
    }

    public String getPaypassword() {
        return paypassword;
    }

    public void setPaypassword(String paypassword) {
        this.paypassword = paypassword;
    }
}
