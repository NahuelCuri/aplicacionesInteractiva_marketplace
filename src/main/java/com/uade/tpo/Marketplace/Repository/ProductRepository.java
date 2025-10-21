package com.uade.tpo.Marketplace.Repository;

import com.uade.tpo.Marketplace.Entity.Product;
import com.uade.tpo.Marketplace.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryIdAndDeletedFalse(Long categoryId);
    List<Product> findBySellerIdAndDeletedFalse(Long sellerId);
    List<Product> findByNameContainingIgnoreCaseAndDeletedFalse(String name);
    List<Product> findByNameContainingIgnoreCaseAndSellerAndDeletedFalse(String name, User seller);
    List<Product> findAllByDeletedFalse();
}