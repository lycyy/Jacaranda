package com.example.service.Thread;

import com.example.service.Bean.User;
import com.example.service.Service.EmailService;
import com.example.service.VerCodeGenerateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


public class EmailThread extends Thread {

    private EmailService emailService;
    private String email;
    private String subject;
    private String random;


    public EmailThread(String email, String subject, String random,EmailService emailService) {
        this.email = email;
        this.subject = subject;
        this.random = random;
        this.emailService = emailService;
    }

    @Override
    public void run() {
        System.out.println(random); 
        emailService.sendMail(email, subject, random);

    }
}
