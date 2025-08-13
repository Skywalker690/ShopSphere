package com.sanjo.ecommerce.service.impl;

import com.sanjo.ecommerce.domain.USER_ROLE;
import com.sanjo.ecommerce.model.User;
import com.sanjo.ecommerce.repository.UserRepository;
import com.sanjo.ecommerce.response.SignupRequest;
import com.sanjo.ecommerce.service.repo.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    @Override
    public String createUser(SignupRequest request) {
        User user =userRepository.findByEmail(request.getEmail());
        if (user == null) {
            User createdUser = new User();
            createdUser.setEmail(request.getEmail());
            createdUser.setFullName(request.getFullName());
            createdUser.setRole(USER_ROLE.ROLE_CUSTOMER);
        }
        return "";
    }
}
