package com.sanjo.ecommerce.controller;

import com.sanjo.ecommerce.model.User;
import com.sanjo.ecommerce.service.repo.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/profile")
    public ResponseEntity<User> createUserHandler(
            @RequestHeader("Authorization") String jwt
            ) throws Exception {
        User user =userService.findUserByJwtToken(jwt);
        return ResponseEntity.ok(user);
    }
}
