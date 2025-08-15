package com.sanjo.ecommerce.service.impl;

import com.sanjo.ecommerce.config.JwtProvider;
import com.sanjo.ecommerce.domain.USER_ROLE;
import com.sanjo.ecommerce.model.Cart;
import com.sanjo.ecommerce.model.User;
import com.sanjo.ecommerce.repository.CartRepository;
import com.sanjo.ecommerce.repository.UserRepository;
import com.sanjo.ecommerce.response.SignupRequest;
import com.sanjo.ecommerce.service.repo.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final JwtProvider jwtProvider;

        @Override
        public String createUser(SignupRequest request) {
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
}
