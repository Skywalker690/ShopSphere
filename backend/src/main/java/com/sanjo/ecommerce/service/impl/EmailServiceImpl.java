package com.sanjo.ecommerce.service.impl;

import com.sanjo.ecommerce.service.repo.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private  String fromAddress;

    public void sendEmail(String email, String otp, String subject, String text) throws MessagingException {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setSubject(subject);
            helper.setText(text, true); // true = HTML content
            helper.setTo(email);
            helper.setFrom(fromAddress);
            javaMailSender.send(mimeMessage);
        } catch (MailException e) {
            throw new MailSendException("OTP Failed to send", e);
        }
    }

}

