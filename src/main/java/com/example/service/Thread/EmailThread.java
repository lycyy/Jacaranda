package com.example.service.Thread;

import com.example.service.Service.EmailService;


public class EmailThread extends Thread {

    private EmailService emailService;
    private String email;
    private String subject;
    private String random;

    private String operation;


    public EmailThread(String email, String subject, String operation , String random,EmailService emailService) {
        this.email = email;
        this.subject = subject;
        this.random = random;
        this.operation = operation;
        this.emailService = emailService;
    }

    @Override
    public void run() {
//        emailService.sendMail(email, subject, random);
        emailService.sendHtmlMail(email, subject,operation, random);

    }
}
