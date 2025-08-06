package com.sanjo.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "cart_item")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity =1;

    private String size;

    private Integer mprPrice;

    private Integer sellingPrice;

    private Long userId;

    @ManyToOne
    @JsonIgnore
    private Cart cart;

    @OneToOne
    private Product product;

}
