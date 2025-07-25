package com.example.blog.services;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.blog.entities.Role;
import com.example.blog.entities.User;
import com.example.blog.payload.JwtResponse;
import com.example.blog.payload.LoginRequest;
import com.example.blog.payload.SignupRequest;
import com.example.blog.repositories.RoleRepository;
import com.example.blog.repositories.UserRepository;
import com.example.blog.services.user.UserService;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    public JwtResponse authenticateUser(LoginRequest loginRequest) {

        if (loginRequest.getUsername() == null || loginRequest.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        UserDetails userDetails = userService.loadUserByUsername(loginRequest.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtService.generateToken(userDetails);

        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(String::toUpperCase)
                .collect(Collectors.toSet());

        return new JwtResponse(jwt, loginRequest.getUsername(), roles);
    }

    public boolean registerUser(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new IllegalArgumentException("Error: Username is already taken!");
        }
        if (signUpRequest.getUsername() == null || signUpRequest.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (signUpRequest.getPassword() == null || signUpRequest.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        Role role;
        if (signUpRequest.getRole() == null) {
            role = roleRepository.findByName("READER")
                    .orElseThrow(() -> new RuntimeException("Role READER not found!"));
        } else {
            role = roleRepository.findByName(signUpRequest.getRole())
                    .orElseThrow(() -> new RuntimeException("Role not found!"));
        }

        User user = new User();
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setRoles(roles);
        userRepository.save(user);
        return true;
    }
}
