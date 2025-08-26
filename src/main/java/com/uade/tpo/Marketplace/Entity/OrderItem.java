package com.uade.tpo.Marketplace.Entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Order
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    // Product
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;

    private Double priceAtPurchase;
}
