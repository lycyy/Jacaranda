package com.example.service.Bean;

import org.springframework.stereotype.Component;

@Component
public class User {
    private String Email;
    private String password;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User() {
    }

    public User(String email, String password) {
        this.Email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "Email='" + Email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
