package com.uade.tpo.Marketplace.DTOs;

import lombok.Data;
import java.util.List;

@Data
public class UserUpdateDTO {
    private String email;
    private String username;
    private List<String> roles;
}