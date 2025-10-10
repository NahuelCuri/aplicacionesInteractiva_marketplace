package com.uade.tpo.Marketplace.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uade.tpo.Marketplace.Config.AuthenticationRequest;
import com.uade.tpo.Marketplace.Config.AuthenticationResponse;
import com.uade.tpo.Marketplace.Config.JwtService;
import com.uade.tpo.Marketplace.Config.RegisterRequest;
import com.uade.tpo.Marketplace.DTOs.AuthUserDTO;
import com.uade.tpo.Marketplace.Entity.User;
import com.uade.tpo.Marketplace.Repository.RoleRepository;
import com.uade.tpo.Marketplace.Repository.UserRepository;

import com.uade.tpo.Marketplace.Entity.Role;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
        @Autowired
        private UserRepository repository;
        @Autowired
        private RoleRepository roleRepository;
        @Autowired
        private PasswordEncoder passwordEncoder;
        @Autowired
        private JwtService jwtService;
        @Autowired
        private AuthenticationManager authenticationManager;

        @Transactional
        public AuthenticationResponse register(RegisterRequest request) {
                Role role = roleRepository.findByName(request.getRole())
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

                User user = User.builder()
                                .username(request.getUsername())
                                .email(request.getEmail())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .roles(List.of(role))
                                .build();

                var savedUser = repository.save(user);
                var jwtToken = jwtService.generateToken(user);
                
                List<String> roles = savedUser.getRoles().stream()
                                .map(Role::getName)
                                .collect(Collectors.toList());

                AuthUserDTO authUserDTO = new AuthUserDTO(savedUser.getUsername(), roles);

                return AuthenticationResponse.builder()
                                .accessToken(jwtToken)
                                .user(authUserDTO)
                                .build();
        }

        public AuthenticationResponse authenticate(AuthenticationRequest request) {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getEmail(),
                                                request.getPassword()));
                var user = repository.findByEmail(request.getEmail())
                                .orElseThrow();
                var jwtToken = jwtService.generateToken(user);
                
                List<String> roles = user.getRoles().stream()
                                .map(Role::getName)
                                .collect(Collectors.toList());

                AuthUserDTO authUserDTO = new AuthUserDTO(user.getUsername(), roles);

                return AuthenticationResponse.builder()
                                .accessToken(jwtToken)
                                .user(authUserDTO)
                                .build();
        }
}
