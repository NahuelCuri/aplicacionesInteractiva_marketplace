package com.uade.tpo.Marketplace.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AuthUserDTO {
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
}
