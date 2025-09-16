package com.example.blog.mappers;

import com.example.blog.DTOs.user.UserRequest;
import com.example.blog.DTOs.user.UserResponse;
import com.example.blog.entities.Role;
import com.example.blog.entities.User;

// Mapper for User entity and DTO
public class UserMapper {
    public static UserResponse toDTO(User user) {
        if (user == null) {
            return null;
        }
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setRoleName(user.getRole().getName());
        return userResponse;
    }

    public static User toCrudEntity(UserRequest userRequest, Role role) {
        if (userRequest == null) {
            return null;
        }
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(userRequest.getPassword());
        user.setRole(role);
        return user;
    }

}
