package com.example.blog.controllers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.blog.DTOs.user.UserRequest;
import com.example.blog.DTOs.user.UserResponse;
import com.example.blog.services.UserService;

@RestController
@RequestMapping("/api/admin/users")
@CrossOrigin(origins = "http://localhost:3000")
// Controller for managing users
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Set<UserResponse>> getAllUsers() {
        Set<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/name")
    public ResponseEntity<UserResponse> getUserByName(@RequestParam String username) {
        UserResponse userResponse = userService.getUserByUsername(username);
        if (userResponse != null) {
            return ResponseEntity.ok(userResponse);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) {
        try {
            UserResponse createdUser = userService.createUser(userRequest);
            return ResponseEntity.status(201).body(createdUser);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserRequest userRequest) {
        try {
            UserResponse updatedUser = userService.updateUser(id, userRequest);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }

}
