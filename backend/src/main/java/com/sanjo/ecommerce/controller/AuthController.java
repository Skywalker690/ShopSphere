package com.sanjo.ecommerce.controller;

import com.sanjo.ecommerce.domain.USER_ROLE;
import com.sanjo.ecommerce.model.VerificationCode;
import com.sanjo.ecommerce.repository.UserRepository;
import com.sanjo.ecommerce.request.LoginRequest;
import com.sanjo.ecommerce.response.ApiResponse;
import com.sanjo.ecommerce.response.AuthResponse;
import com.sanjo.ecommerce.response.SignupRequest;
import com.sanjo.ecommerce.service.repo.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignupRequest request) throws Exception {
        String jwt = authService.createUser(request);

        AuthResponse response = new AuthResponse();
        response.setJwt(jwt);
        response.setMessage("Register Successful ");
        response.setRole(USER_ROLE.ROLE_CUSTOMER);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/sent/login-signup-otp")
    public ResponseEntity<ApiResponse> sentOtpHandler(@RequestBody VerificationCode code) throws Exception {

        authService.sentOtp(code.getEmail());
        ApiResponse response = new ApiResponse();
        response.setMessage("OTP Sent Successfully ");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest request) throws Exception {

        AuthResponse response =authService.signIn(request);
        return ResponseEntity.ok(response);
    }

}
