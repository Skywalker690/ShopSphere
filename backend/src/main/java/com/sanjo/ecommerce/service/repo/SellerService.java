package com.sanjo.ecommerce.service.repo;

import com.sanjo.ecommerce.domain.AccountStatus;
import com.sanjo.ecommerce.model.Seller;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellerService {
    Seller getSellerProfile(String jwt);
    Seller createSeller(Seller seller);
    Seller getSellerById(Long id);
    Seller getSellerByEmail(String email);
    List<Seller> getAllSellers(AccountStatus status);
    Seller updateSeller(Long id, Seller seller);
    void deleteSeller(Long id);
    Seller verifyEmail(String email, String otp);
    Seller updateSellerAccountStatus(Long sellerId, AccountStatus status);

}
