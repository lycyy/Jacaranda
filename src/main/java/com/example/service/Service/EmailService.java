package com.example.service.Service;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendMail(String to, String subject, String text);
}
