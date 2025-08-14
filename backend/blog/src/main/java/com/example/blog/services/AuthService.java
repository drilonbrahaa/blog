package com.example.blog.services;

import com.example.blog.DTOs.auth.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.blog.DTOs.auth.LoginRequest;
import com.example.blog.DTOs.user.UserResponse;
import com.example.blog.entities.Role;
import com.example.blog.entities.User;
import com.example.blog.jwt.JwtService;
import static com.example.blog.mappers.UserMapper.toDTO;
import com.example.blog.repositories.UserRepository;

@Service
public class AuthService {

        @Autowired
        private AuthenticationManager authenticationManager;
        @Autowired
        private JwtService jwtService;
        @Autowired
        private RoleService roleService;
        @Autowired
        private PasswordEncoder passwordEncoder;
        @Autowired
        private UserRepository userRepository;

        public LoginResponse loginAndGetToken(LoginRequest loginRequest) {
                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                                                loginRequest.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                User user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(() -> new RuntimeException("User not found!"));
                String role = user.getRole().getName();
                String token = jwtService.generateToken(userDetails);
                return new LoginResponse(token, role);
        }

        public UserResponse register(LoginRequest registerDTO) {
                if (registerDTO.getUsername() == null || registerDTO.getUsername().trim().isEmpty()) {
                        throw new RuntimeException("Username cannot be empty!");
                }
                if (registerDTO.getPassword() == null || registerDTO.getPassword().trim().isEmpty()) {
                        throw new RuntimeException("Password cannot be empty!");
                }
                User user = new User();
                user.setUsername(registerDTO.getUsername());
                user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
                Role role = roleService.getRoleById(3L);
                user.setRole(role);
                userRepository.save(user);
            return toDTO(user);
        }

}
