package com.sanjo.ecommerce.service.repo;

import com.sanjo.ecommerce.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserService {

    User findUserByJwtToken(String token);
    User findUserByEmail(String email);
}
