package com.agribank.e_contract.controller;

import com.agribank.e_contract.service.mail.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
public class MailController {
    public final MailService mailService;
    @PostMapping("/sendMail")
    public void sendMail() {
        mailService.sendMail("dangquang251203@gmail.com", "Test Mail", "This is a test mail from Spring Boot");
    }
}
