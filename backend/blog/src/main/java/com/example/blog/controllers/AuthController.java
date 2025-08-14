package com.example.blog.controllers;

import com.example.blog.DTOs.auth.LoginResponse;
import com.example.blog.DTOs.user.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.blog.DTOs.auth.LoginRequest;
import com.example.blog.DTOs.user.UserRequest;
import com.example.blog.services.AuthService;
import com.example.blog.services.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.loginAndGetToken(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody LoginRequest loginRequest) {
        try {
            ;
            return ResponseEntity.ok(authService.register(loginRequest));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
