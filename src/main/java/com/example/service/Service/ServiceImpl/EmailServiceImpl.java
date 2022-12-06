package com.example.service.Service.ServiceImpl;

import com.example.service.Service.EmailService;
import com.example.service.Util.VerCodeGenerateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import java.io.File;
import java.util.*;

import au.com.dingomail.*;
import au.com.dingomail.api.*;
import au.com.dingomail.auth.*;
import au.com.dingomail.model.*;

@Service
public class EmailServiceImpl implements EmailService {
    @Resource
    private JavaMailSender javaMailSender;
    @Autowired
    private TemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    public void sendHtmlMail(String to, String subject, String operation, String verificationCode) {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {
            mimeMessageHelper = new MimeMessageHelper(message, true);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(to);
            message.setSubject(subject);

            Context context = new Context();
            context.setVariable("verificationCode", verificationCode);
            context.setVariable("operation", operation);

            String process = templateEngine.process("VerificationCodeTemplate", context);
            mimeMessageHelper.setText(process, true);
            javaMailSender.send(message);

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
