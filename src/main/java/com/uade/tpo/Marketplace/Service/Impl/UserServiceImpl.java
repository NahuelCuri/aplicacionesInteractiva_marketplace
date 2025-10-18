package com.uade.tpo.Marketplace.Service.Impl;

import com.uade.tpo.Marketplace.DTOs.Mapper.UserMapper;
import com.uade.tpo.Marketplace.DTOs.UserDetailDTO;
import com.uade.tpo.Marketplace.DTOs.UserRegistrationDTO;
import com.uade.tpo.Marketplace.DTOs.UserUpdateDTO;
import com.uade.tpo.Marketplace.Entity.Role;
import com.uade.tpo.Marketplace.Entity.User;
import com.uade.tpo.Marketplace.Repository.RoleRepository;
import com.uade.tpo.Marketplace.Repository.UserRepository;
import com.uade.tpo.Marketplace.Service.UserService;
import com.uade.tpo.Marketplace.Config.JwtService;
import com.uade.tpo.Marketplace.Config.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtService jwtService;

    @Override
    public UserDetailDTO createUser(UserRegistrationDTO userRegistrationDTO) {
        if (userRepository.existsByUsername(userRegistrationDTO.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(userRegistrationDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.setUsername(userRegistrationDTO.getUsername());
        user.setEmail(userRegistrationDTO.getEmail());
        
        user.setPassword(userRegistrationDTO.getPassword());

        Role buyerRole = roleRepository.findByName("BUYER")
                    .orElseThrow(() -> new RuntimeException("Error: Default role 'ROLE_BUYER' not found in database."));
        user.setRoles(Collections.singletonList(buyerRole));
        User savedUser = userRepository.save(user);
        return UserMapper.toUserDetailDTO(savedUser);
    }

    @Override
    public UserDetailDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id " + id));
        return UserMapper.toUserDetailDTO(user);
    }

    @Override
    public UserDetailDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found with username " + username));
        return UserMapper.toUserDetailDTO(user);
    }

    @Override
    public List<UserDetailDTO> searchUsersByUsername(String username) {
        List<User> users = userRepository.findByUsernameContainingIgnoreCase(username);
        
        return users.stream()
                .map(UserMapper::toUserDetailDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDetailDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDetailDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDetailDTO updateUser(Long id, UserUpdateDTO userUpdateDTO) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id " + id));
        
        if (userUpdateDTO.getEmail() != null && !userUpdateDTO.getEmail().isEmpty()) {
            existing.setEmail(userUpdateDTO.getEmail());
        }
        if (userUpdateDTO.getUsername() != null && !userUpdateDTO.getUsername().isEmpty()) {
            existing.setUsername(userUpdateDTO.getUsername());
        }

        if (userUpdateDTO.getRoles() != null) {
            List<Role> roles = userUpdateDTO.getRoles().stream()
                    .map(roleName -> roleRepository.findByName(roleName)
                            .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                    .collect(Collectors.toList());
            existing.setRoles(roles);
        }

        User updatedUser = userRepository.save(existing);
        return UserMapper.toUserDetailDTO(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("User not found with id " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public AuthenticationResponse becomeSeller() {
        org.springframework.security.core.Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role sellerRole = roleRepository.findByName("SELLER")
                .orElseThrow(() -> new RuntimeException("Error: Role 'SELLER' not found in database."));

        user.getRoles().add(sellerRole);
        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .user(UserMapper.toAuthUserDTO(user))
                .build();
    }
}