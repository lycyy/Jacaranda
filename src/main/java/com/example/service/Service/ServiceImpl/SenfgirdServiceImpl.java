package com.example.service.Service.ServiceImpl;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import java.io.IOException;
@Service
public class SenfgirdServiceImpl {
    @Autowired
    private TemplateEngine templateEngine;

    public void test()  {
        Email from = new Email("945009953@qq.com");
        String subject = "Sending with SendGrid is Fun";
        Email to = new Email("lyc945009953@gmail.com");
        Content content = new Content("text/plain", "and easy to do anywhere, even with Java");
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid("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");
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
}
