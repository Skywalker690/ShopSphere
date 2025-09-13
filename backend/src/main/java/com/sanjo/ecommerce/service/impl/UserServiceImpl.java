package com.sanjo.ecommerce.service.impl;

import com.sanjo.ecommerce.config.JwtProvider;
import com.sanjo.ecommerce.exception.OurException;
import com.sanjo.ecommerce.model.User;
import com.sanjo.ecommerce.repository.UserRepository;
import com.sanjo.ecommerce.service.repo.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Override
    public User findUserByJwtToken(String token) {
        String email =jwtProvider.getEmailFromJwtToken(token);
        return this.findUserByEmail(email);
    }

    @Override
    public User findUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new OurException("User Not Found with : "+email);
        }
        return user;
    }
}
