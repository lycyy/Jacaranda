package com.example.service.Thread;

import com.example.service.Service.DingoMailService;
import com.example.service.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class EmailThread extends Thread {

    private EmailService emailService;
    private DingoMailService dingoMailService;
    private String email;
    private String subject;
    private String random;

    private String operation;


    public EmailThread(String email, String subject, String operation , String random,DingoMailService dingoMailService) {
        this.email = email;
        this.subject = subject;
        this.random = random;
        this.operation = operation;
        this.dingoMailService = dingoMailService;
    }

    @Override
    public void run() {
//        emailService.sendMail(email, subject, random);
//        emailService.sendHtmlMail(email, subject,operation, random);
        dingoMailService.CreateEmail(email,subject,operation,random);

    }
}
