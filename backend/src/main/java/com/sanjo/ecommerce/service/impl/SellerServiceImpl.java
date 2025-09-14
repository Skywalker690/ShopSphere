package com.sanjo.ecommerce.service.impl;

import com.sanjo.ecommerce.config.JwtProvider;
import com.sanjo.ecommerce.domain.AccountStatus;
import com.sanjo.ecommerce.domain.USER_ROLE;
import com.sanjo.ecommerce.exception.OurException;
import com.sanjo.ecommerce.model.Address;
import com.sanjo.ecommerce.model.Seller;
import com.sanjo.ecommerce.repository.AddressRepository;
import com.sanjo.ecommerce.repository.SellerRepository;
import com.sanjo.ecommerce.service.repo.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;

    @Override
    public Seller getSellerProfile(String jwt) throws OurException {
        String  email =jwtProvider.getEmailFromJwtToken(jwt);
        return sellerRepository.findByEmail(email);
    }


    @Override
    public Seller createSeller(Seller seller) {
        Seller sellerExit =sellerRepository.findByEmail(seller.getEmail());
        if (sellerExit != null) {
            throw new OurException("Seller Already Exist ");
        }
        Address savedAddress = addressRepository.save(seller.getPickupAddress());
        Seller newSeller = new Seller();

        newSeller.setEmail(seller.getEmail());
        newSeller.setPassword(passwordEncoder.encode(seller.getPassword()));
        newSeller.setSellerName(seller.getSellerName());
        newSeller.setPickupAddress(savedAddress);
        newSeller.setGST(seller.getGST());
        newSeller.setRole(USER_ROLE.ROLE_SELLER);
        newSeller.setMobile(seller.getMobile());
        newSeller.setBankDetails(seller.getBankDetails());
        newSeller.setBusinessDetails(seller.getBusinessDetails());

        return sellerRepository.save(newSeller);
    }

    @Override
    public Seller getSellerById(Long id) {
        return sellerRepository.findById(id).orElseThrow(
                () -> new OurException("User Not Found By this id ")
        );
    }

    @Override
    public Seller getSellerByEmail(String email) {
        Seller seller =sellerRepository.findByEmail(email);
        if (seller == null) {
            throw new OurException("Seller Not Found");
        }
        return seller;
    }

    @Override
    public List<Seller> getAllSellers(AccountStatus status) {
        return sellerRepository.findByAccountStatus(status);
    }

    @Override
    public Seller updateSeller(Long id, Seller seller) {
        // Fetch existing seller
        Seller existingSeller = sellerRepository.findById(id)
                .orElseThrow(() -> new OurException("Seller Not Found By this ID"));

        // Update Pickup Address if provided
        if (seller.getPickupAddress() != null) {
            if (existingSeller.getPickupAddress() == null) {
                existingSeller.setPickupAddress(new Address());
            }
            if (seller.getPickupAddress().getAddress() != null) {
                existingSeller.getPickupAddress().setAddress(seller.getPickupAddress().getAddress());
            }
            if (seller.getPickupAddress().getCity() != null) {
                existingSeller.getPickupAddress().setCity(seller.getPickupAddress().getCity());
            }
            if (seller.getPickupAddress().getState() != null) {
                existingSeller.getPickupAddress().setState(seller.getPickupAddress().getState());
            }
            if (seller.getPickupAddress().getMobile() != null) {
                existingSeller.getPickupAddress().setMobile(seller.getPickupAddress().getMobile());
            }
            if (seller.getPickupAddress().getPinCode() != null) {
                existingSeller.getPickupAddress().setPinCode(seller.getPickupAddress().getPinCode());
            }
        }

        // Update GST if provided
        if (seller.getGST() != null && !seller.getGST().isEmpty()) {
            existingSeller.setGST(seller.getGST());
        }

        // Update Bank Details if provided
        if (seller.getBankDetails() != null) {
            if (existingSeller.getBankDetails() == null) {
                existingSeller.setBankDetails(seller.getBankDetails());
            } else {
                if (seller.getBankDetails().getAccountHolderName() != null) {
                    existingSeller.getBankDetails().setAccountHolderName(seller.getBankDetails().getAccountHolderName());
                }
                if (seller.getBankDetails().getAccountNumber() != null) {
                    existingSeller.getBankDetails().setAccountNumber(seller.getBankDetails().getAccountNumber());
                }
                if (seller.getBankDetails().getIfscCode() != null) {
                    existingSeller.getBankDetails().setIfscCode(seller.getBankDetails().getIfscCode());
                }
            }
        }

        // Update other fields
        if (seller.getEmail() != null && !seller.getEmail().isEmpty()) {
            existingSeller.setEmail(seller.getEmail());
        }
        if (seller.getPassword() != null && !seller.getPassword().isEmpty()) {
            existingSeller.setPassword(passwordEncoder.encode(seller.getPassword()));
        }
        if (seller.getSellerName() != null && !seller.getSellerName().isEmpty()) {
            existingSeller.setSellerName(seller.getSellerName());
        }
        if (seller.getMobile() != null && !seller.getMobile().isEmpty()) {
            existingSeller.setMobile(seller.getMobile());
        }
        if (seller.getBusinessDetails() != null) {
            existingSeller.setBusinessDetails(seller.getBusinessDetails());
        }

        // Save updated seller
        return sellerRepository.save(existingSeller);
    }



    @Override
    public void deleteSeller(Long id) {

        if (!sellerRepository.existsById(id)) {
            throw new OurException("Seller Not Found. Deletion Unsuccessful ");
        }
        sellerRepository.deleteById(id);
    }


    @Override
    public Seller verifyEmail(String email, String otp) {
        Seller seller =this.getSellerByEmail(email);
        seller.setEmailVerified(true);
        return sellerRepository.save(seller);
    }

    @Override
    public Seller updateSellerAccountStatus(Long sellerId, AccountStatus status) {
        Seller seller =this.getSellerById(sellerId);
        seller.setAccountStatus(status);
        return sellerRepository.save(seller);
    }
}
