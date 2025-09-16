package com.example.blog.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.blog.DTOs.user.UserRequest;
import com.example.blog.DTOs.user.UserResponse;
import com.example.blog.entities.Role;
import com.example.blog.entities.User;
import static com.example.blog.mappers.UserMapper.toCrudEntity;
import static com.example.blog.mappers.UserMapper.toDTO;
import com.example.blog.repositories.CommentRepository;
import com.example.blog.repositories.PostRepository;
import com.example.blog.repositories.UserRepository;

@Service
// Service for managing users
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Set<UserResponse> getAllUsers() {
        Set<User> users = new HashSet<>(userRepository.findAll());
        Set<UserResponse> userResponses = new HashSet<>();
        users.forEach(user -> {
            UserResponse userResponse = toDTO(user);
            if (userResponse != null) {
                userResponses.add(userResponse);
            }
        });
        return userResponses;
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return toDTO(user);
    }

    public UserResponse createUser(UserRequest userRequest) {
        if (userRepository.findByUsername(userRequest.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        Role role = roleService.getRoleByName(userRequest.getRoleName());
        User user = toCrudEntity(userRequest, role);
        userRepository.save(user);
        return toDTO(user);
    }

    public UserResponse updateUser(Long userId, UserRequest userRequest) {
        User existingUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        if (userRequest.getUsername() != null && !userRequest.getUsername().isEmpty()) {
            existingUser.setUsername(userRequest.getUsername());
        }
        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
            existingUser.setPassword(getPasswordEncoder().encode(userRequest.getPassword()));
        }
        if (userRequest.getRoleName() != null) {
            Role role = roleService.getRoleByName(userRequest.getRoleName());
            existingUser.setRole(role);
        }
        userRepository.save(existingUser);
        return toDTO(existingUser);
    }

    @Transactional
    public void deleteUser(Long userId) {
        if (userRepository.existsById(userId)) {
            commentRepository.deleteByAuthorId(userId);
            postRepository.deleteByAuthorId(userId);
            userRepository.deleteById(userId);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new CustomUserDetails(user);
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

}
