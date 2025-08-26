package com.uade.tpo.Marketplace.Entity;


import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // ADMIN, BUYER, SELLER

    @ManyToMany(mappedBy = "roles")
    private List<User> users;
}
