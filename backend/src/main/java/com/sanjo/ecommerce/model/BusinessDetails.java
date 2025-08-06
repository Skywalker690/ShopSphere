package com.sanjo.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessDetails {

    private String businessName;
    private String businessEmail;
    private String businessMobile;
    private String businessAddress;
    private String logo;
    private String banner;
}