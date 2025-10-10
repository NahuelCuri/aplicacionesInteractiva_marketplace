package com.uade.tpo.Marketplace.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AuthUserDTO {
    private String username;
    private List<String> roles;
}
