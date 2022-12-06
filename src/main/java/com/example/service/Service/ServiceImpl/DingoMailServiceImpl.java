package com.example.service.Service.ServiceImpl;

import au.com.dingomail.*;
import au.com.dingomail.api.*;
import au.com.dingomail.auth.*;
import au.com.dingomail.model.*;
import com.example.service.Service.DingoMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class DingoMailServiceImpl implements DingoMailService {
    private ApiClient apiClient;
    @Autowired
    private TemplateEngine templateEngine;

    public DingoMailServiceImpl() {
        apiClient = Configuration.getDefaultApiClient();
//        apiClient.setBasePath("https://api.dingomail.com.au");

        Authentication auth;

        auth = apiClient.getAuthentication("Key Secret");
        ((ApiKeyAuth) auth).setApiKey("YQHTTBZFKO2SETURJHX2CUMHCC8C3UTZRAPWPDVYB5YWS");

        auth = apiClient.getAuthentication("Key ID");
        ((ApiKeyAuth) auth).setApiKey("638dd8ac19e883676f3456c4");
    }

    public void CreateEmail(String to, String subject, String operation, String verificationCode) {
        // create NewEmail object
        Context context = new Context();
        context.setVariable("verificationCode", verificationCode);
        context.setVariable("operation", operation);
        String process = templateEngine.process("VerificationCodeTemplate", context);
        NewEmail newEmail = new NewEmail();
        newEmail.setHtml(process);
        newEmail.setRecipient(to);
        newEmail.setSenderEmail("Jacaranda@gmail.com");
        newEmail.setSenderName("Jacaranda");
        newEmail.setSubject(subject);
        newEmail.setText("");
        String keyid = "638dd8ac19e883676f3456c4";  // The ID of the API Key being used

        try {
            EmailApi emailApi = new EmailApi();
            EmailResponse response = emailApi.create(newEmail, keyid);
            System.out.println(response);
        } catch (ApiException e) {
            System.out.printf("ApiException caught: %s\n", e.getMessage());
        }
    }
}

