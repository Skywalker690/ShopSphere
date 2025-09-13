package com.sanjo.ecommerce.service.repo;

import jakarta.mail.MessagingException;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailService {
    void sendEmail(String email, String otp, String subject, String text) throws MessagingException;
}
