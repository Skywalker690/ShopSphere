package com.sanjo.ecommerce.service.repo;

import com.sanjo.ecommerce.response.SignupRequest;

public interface AuthService {

    String createUser(SignupRequest request);
}
