package com.sanjo.ecommerce.service.repo;

import com.sanjo.ecommerce.response.SignupRequest;
import jakarta.mail.MessagingException;

public interface AuthService {

    String createUser(SignupRequest request) throws Exception;
    void sentOtp(String email) throws MessagingException;
}
