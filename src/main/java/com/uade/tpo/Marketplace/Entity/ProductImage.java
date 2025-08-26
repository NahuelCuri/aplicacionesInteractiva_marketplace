package com.uade.tpo.Marketplace.Entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}

