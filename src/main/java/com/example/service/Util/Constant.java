package com.example.service.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class Constant {
    @Autowired
    ConfigurationUtil configurationUtil;

    private static String Stripe_apiKey = "sk_test_51LImNKAOiNy9BWzWHWwXKchph0iHIA8eySN5eDNYtxL9LLrNzFISXmTOHrjrpEnvbF1pKyRCaG9BcqnWXjcNNPnf00vUzWYyjr";
    private static String Password_salt = "3jVTK2E*)6OMLK6^LuR";
    private static String Token_key = "e7vj!1NBnoSrbF2wvJ";
    private static String RSA_seed = "DZqsRin_cdS2h+)g%U";


    public ConfigurationUtil getConfigurationUtil() {
        return configurationUtil;
    }

    public void setConfigurationUtil(ConfigurationUtil configurationUtil) {
        this.configurationUtil = configurationUtil;
    }

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

    public void setRSA_seed(String RSA_seed) {
        this.RSA_seed = RSA_seed;
    }


}
