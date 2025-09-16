package com.example.blog.DTOs.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// DTO for user response
public class UserResponse {
    private Long id;
    private String username;
    private String roleName;
}
