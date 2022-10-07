package com.example.service.Util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ConfigurationUtil {
    public static ConfigurationUtil configurationUtil;

    public static String RSA_seed;
    @Value("${constant.Stripe_apiKey}")
    private String Stripe_apiKey;
    @Value("${constant.Password_salt}")
    private String Password_salt;
    @Value("${constant.Token_key}")
    private String Token_key;


    public String getStripe_apiKey() {
        return Stripe_apiKey;
    }

    public void setStripe_apiKey(String stripe_apiKey) {
        Stripe_apiKey = stripe_apiKey;
    }

    public String getPassword_salt() {
        return Password_salt;
    }

    public void setPassword_salt(String password_salt) {
        Password_salt = password_salt;
    }

    public String getToken_key() {
        return Token_key;
    }

    public void setToken_key(String token_key) {
        Token_key = token_key;
    }

    public String getRSA_seed() {
        return RSA_seed;
    }

    @Value("${constant.RSA_seed}")
    public void setRSA_seed(String RSA_seed) {
        ConfigurationUtil.RSA_seed = RSA_seed;
    }

    @Override
    public String toString() {
        return "ConfigurationUtil{" +
                "Stripe_apiKey='" + Stripe_apiKey + '\'' +
                ", Password_salt='" + Password_salt + '\'' +
                ", Token_key='" + Token_key + '\'' +
                '}';
    }
}
