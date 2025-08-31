package com.uade.tpo.Marketplace.Controller;

import com.uade.tpo.Marketplace.DTOs.UserDetailDTO;
import com.uade.tpo.Marketplace.DTOs.UserRegistrationDTO;
import com.uade.tpo.Marketplace.DTOs.UserUpdateDTO;
import com.uade.tpo.Marketplace.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserDetailDTO> createUser(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        return ResponseEntity.ok(userService.createUser(userRegistrationDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserDetailDTO> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDetailDTO>> searchUsers(@RequestParam String username) {
        return ResponseEntity.ok(userService.searchUsersByUsername(username));
    }

    @GetMapping
    public ResponseEntity<List<UserDetailDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDetailDTO> updateUser(@PathVariable Long id, @RequestBody UserUpdateDTO userUpdateDTO) {
        return ResponseEntity.ok(userService.updateUser(id, userUpdateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}