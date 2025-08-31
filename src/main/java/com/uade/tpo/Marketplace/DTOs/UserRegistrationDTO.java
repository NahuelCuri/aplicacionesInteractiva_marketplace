package com.uade.tpo.Marketplace.DTOs;

import lombok.Data;

@Data
public class UserRegistrationDTO {
    private String username;
    private String email;
    private String password;
}