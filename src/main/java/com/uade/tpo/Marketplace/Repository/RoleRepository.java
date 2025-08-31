package com.uade.tpo.Marketplace.Repository;

import com.uade.tpo.Marketplace.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
}