package com.example.blog.DTOs.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// DTO for user request
public class UserRequest {
    private String username;
    private String password;
    private String roleName;
}
