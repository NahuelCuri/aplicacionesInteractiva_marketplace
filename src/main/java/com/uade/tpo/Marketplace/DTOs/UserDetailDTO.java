package com.uade.tpo.Marketplace.DTOs;

import lombok.Data;
import java.util.List;

@Data
public class UserDetailDTO {
private Long id;
private String username;
private String email;
private List<RoleDTO> roles;
}