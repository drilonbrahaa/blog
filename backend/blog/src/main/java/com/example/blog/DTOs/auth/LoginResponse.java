package com.example.blog.DTOs.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// DTO for login response
public class LoginResponse {
    private String token;
    private String role;

    public LoginResponse(String token, String role) {
        this.token = token;
        this.role = role;
    }
}
