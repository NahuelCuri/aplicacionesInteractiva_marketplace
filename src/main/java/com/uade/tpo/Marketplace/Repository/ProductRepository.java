package com.uade.tpo.Marketplace.Repository;

import com.uade.tpo.Marketplace.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long categoryId);
    List<Product> findBySellerId(Long sellerId);
    List<Product> findByNameContainingIgnoreCase(String name);
}
