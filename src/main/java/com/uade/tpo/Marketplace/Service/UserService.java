package com.uade.tpo.Marketplace.Service;

import com.uade.tpo.Marketplace.DTOs.UserDetailDTO;
import com.uade.tpo.Marketplace.DTOs.UserRegistrationDTO;
import com.uade.tpo.Marketplace.DTOs.UserUpdateDTO;
import java.util.List;

public interface UserService {
    UserDetailDTO createUser(UserRegistrationDTO userRegistrationDTO);
    UserDetailDTO getUserById(Long id);
    UserDetailDTO getUserByUsername(String username);
    List<UserDetailDTO> getAllUsers();
    UserDetailDTO updateUser(Long id, UserUpdateDTO userUpdateDTO);
    void deleteUser(Long id);
    List<UserDetailDTO> searchUsersByUsername(String username);
    com.uade.tpo.Marketplace.Config.AuthenticationResponse becomeSeller();
}