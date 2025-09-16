package com.example.blog.DTOs.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// DTO for login request
public class LoginRequest {
    private String username;
    private String password;
}
