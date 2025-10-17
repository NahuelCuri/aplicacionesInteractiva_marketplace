package com.uade.tpo.Marketplace.DTOs.Mapper;

import com.uade.tpo.Marketplace.DTOs.RoleDTO;
import com.uade.tpo.Marketplace.DTOs.UserDetailDTO;
import com.uade.tpo.Marketplace.DTOs.AuthUserDTO;
import com.uade.tpo.Marketplace.Entity.Role;
import com.uade.tpo.Marketplace.Entity.User;

import java.util.stream.Collectors;

public class UserMapper {

    private UserMapper() {}

    public static UserDetailDTO toUserDetailDTO(User user) {
        UserDetailDTO dto = new UserDetailDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());

        if (user.getRoles() != null) {
            dto.setRoles(user.getRoles().stream()
                .map(UserMapper::toRoleDTO)
                .collect(Collectors.toList()));
        }

        return dto;
    }

    private static RoleDTO toRoleDTO(Role role) {
        RoleDTO dto = new RoleDTO();
        dto.setId(role.getId());
        dto.setName(role.getName());
        return dto;
    }

    public static AuthUserDTO toAuthUserDTO(User user) {
        return new AuthUserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles().stream().map(Role::getName).collect(Collectors.toList())
        );
    }
}