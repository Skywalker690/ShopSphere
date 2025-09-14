package com.sanjo.ecommerce.repository;

import com.sanjo.ecommerce.domain.AccountStatus;
import com.sanjo.ecommerce.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SellerRepository extends JpaRepository<Seller,Long> {
    Seller findByEmail(String email);
    List<Seller> findByAccountStatus(AccountStatus status);
}
