package com.uade.tpo.Marketplace.Repository;

import com.uade.tpo.Marketplace.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
    Category findByName(String name);
}
