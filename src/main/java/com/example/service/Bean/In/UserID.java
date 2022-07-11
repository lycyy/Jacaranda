package com.example.service.Bean.In;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

@Component
public class UserID {
    private String UserID;
    private String Amount;
    @JsonProperty(value = "UserID")
    public String getUserID() {
        return UserID;
    }
    @JsonProperty(value = "UserID")
    public void setUserID(String userID) {
        UserID = userID;
    }
    @JsonProperty(value = "Amount")
    public String getAmount() {
        return Amount;
    }
    @JsonProperty(value = "Amount")
    public void setAmount(String amount) {
        Amount = amount;
    }
}
