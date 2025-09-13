package com.sanjo.ecommerce.service.impl;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sanjo.ecommerce.config.JwtProvider;
import com.sanjo.ecommerce.domain.USER_ROLE;
import com.sanjo.ecommerce.exception.OurException;
import com.sanjo.ecommerce.model.Cart;
import com.sanjo.ecommerce.model.User;
import com.sanjo.ecommerce.utils.OtpLogic;
import com.sanjo.ecommerce.model.VerificationCode;
import com.sanjo.ecommerce.repository.CartRepository;
import com.sanjo.ecommerce.repository.UserRepository;
import com.sanjo.ecommerce.repository.VerificationCodeRepository;
import com.sanjo.ecommerce.request.LoginRequest;
import com.sanjo.ecommerce.response.AuthResponse;
import com.sanjo.ecommerce.response.SignupRequest;
import com.sanjo.ecommerce.service.repo.AuthService;
import com.sanjo.ecommerce.service.repo.EmailService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final JwtProvider jwtProvider;
    private final EmailService emailService;
    private final CustomUserServiceImpl customUserService;
    private final VerificationCodeRepository verificationCodeRepository;

        @Override
        public String createUser(SignupRequest request) throws OurException {

            VerificationCode verificationCode = verificationCodeRepository.findByEmail(request.getEmail());

            if (verificationCode == null || !verificationCode.getOtp().equals(request.getOtp())) {
                throw new OurException("Wrong OTP Provided");
            }


            User user =userRepository.findByEmail(request.getEmail());
            if (user == null) {
                User createdUser = new User();
                createdUser.setEmail(request.getEmail());
                createdUser.setFullName(request.getFullName());
                createdUser.setRole(USER_ROLE.ROLE_CUSTOMER);
                createdUser.setPhoneNumber("934023239329");
                createdUser.setPassword(passwordEncoder.encode(request.getOtp()));

                userRepository.save(createdUser);

                Cart cart = new Cart();
                cart.setUser(createdUser);
                cartRepository.save(cart);
            }
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));

            Authentication authentication = new UsernamePasswordAuthenticationToken(request.getEmail(),null,authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return jwtProvider.generateToken(authentication);
        }

    @Override
    public void sentOtp(String email) throws OurException, MessagingException {
        String SIGNING_PREFIX="signin_";
        if(email.startsWith(SIGNING_PREFIX)){
            email =email.substring(SIGNING_PREFIX.length());

            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new OurException("User Does not Exist with provided Email");
            }
        }

        VerificationCode isValid =verificationCodeRepository.findByEmail(email);
        if (isValid != null) {
            verificationCodeRepository.delete(isValid);
        }
        String otp = OtpLogic.generateOtp();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(email);
        verificationCodeRepository.save(verificationCode);

        String subject ="ShopSphere OTP";
        String text = "Your OTP : " + otp;

        emailService.sendEmail(email,otp,subject,text);
    }

    @Override
    public AuthResponse signIn(LoginRequest request) {
        String username =request.getEmail();
        String otp =request.getOtp();

        Authentication authentication =authenticate(username,otp);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token =jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Login Successful");

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role =authorities.isEmpty() ?
                null :
                authorities.iterator().next().getAuthority();

        authResponse.setRole(USER_ROLE.valueOf(role));
        return authResponse;
    }

    private Authentication authenticate(String username, String otp) {
        UserDetails userDetails =customUserService.loadUserByUsername(username);
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid UserName ");
        }

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(username);
        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            throw new OurException("Wrong OTP");
        }
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }
}
