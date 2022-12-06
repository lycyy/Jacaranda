package com.example.service.Service.ServiceImpl;

import com.mailgun.api.v3.MailgunMessagesApi;
import com.mailgun.client.MailgunClient;
import com.mailgun.model.message.Message;
import com.mailgun.model.message.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

// ...

@Service
public class MailGunServiceImpl {
    @Autowired
    private TemplateEngine templateEngine;

    private static final String API_KEY = "5510a6f9722298bcac9a46da427533c6-bdb2c8b4-57df304d" ;
    private static final String YOUR_DOMAIN_NAME = "sandboxcf89de86af7548eba2e79ebb93ec795c.mailgun.org" ;

    public MessageResponse sendSimpleMessage(String verificationCode,String operation) {
        MailgunMessagesApi mailgunMessagesApi = MailgunClient.config(API_KEY)
                .createApi(MailgunMessagesApi.class);
        Context context = new Context();
        context.setVariable("verificationCode", verificationCode);
        context.setVariable("operation", operation);
        String process = templateEngine.process("VerificationCodeTemplate", context);
        Message message = Message.builder()
                .from("test@gmail.com")
                .to("lyc945009953@gmail.com")
                .subject("Hello")
                .html(process)
                .build();

        return mailgunMessagesApi.sendMessage(YOUR_DOMAIN_NAME, message);
    }
}
