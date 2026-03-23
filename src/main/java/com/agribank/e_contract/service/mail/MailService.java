package com.agribank.e_contract.service.mail;

import org.springframework.stereotype.Service;

@Service
public interface MailService {
    void sendMail(String toEmail, String subject, String content);
}
