package com.sanjo.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sanjo.ecommerce.domain.User_Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Name is required")
    private String fullName;

    @NotBlank(message = "Phone Number is required")
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Column(unique = true)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    //By default Customer
    private User_Role role = User_Role.ROLE_CUSTOMER;

    private Set<Address> address = new HashSet<Address>();

    private Set<Coupon> usedCoupons = new HashSet<Coupon>();

}
