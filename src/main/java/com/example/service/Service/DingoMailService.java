package com.example.service.Service;

public interface DingoMailService {
    public void CreateEmail(String to, String subject, String operation, String verificationCode);
}
