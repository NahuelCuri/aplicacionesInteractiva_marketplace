package com.uade.tpo.Marketplace.Service;

import com.uade.tpo.Marketplace.Entity.Role;
import java.util.List;

public interface RoleService {
    Role createRole(Role role);
    List<Role> getAllRoles();
}