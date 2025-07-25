package com.example.blog.payload;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {
    private String token;
    private String type;
    private String username;
    private Set<String> roles;

    public JwtResponse(String token, String username, Set<String> roles) {
        this.token = token;
        this.type = "Bearer";
        this.username = username;
        this.roles = roles;
    }
}
