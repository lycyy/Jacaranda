package com.example.service.Bean.In;

public class CompanyInfo {
    private String CompanyID;
    private String Picture;
    private String C_name;
    private String C_Mobile;
    private String C_address;

    public String getC_address() {
        return C_address;
    }

    public void setC_address(String c_address) {
        C_address = c_address;
    }

    private String email;
    private String Balance;

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }

    public String getCompanyID() {
        return CompanyID;
    }

    public void setCompanyID(String companyID) {
        CompanyID = companyID;
    }

    public String getPicture() {
        return Picture;
    }

    public void setPicture(String picture) {
        Picture = picture;
    }

    public String getC_name() {
        return C_name;
    }

    public void setC_name(String c_name) {
        C_name = c_name;
    }

    public String getC_Mobile() {
        return C_Mobile;
    }

    public void setC_Mobile(String c_Mobile) {
        C_Mobile = c_Mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}