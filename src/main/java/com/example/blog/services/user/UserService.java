package com.example.blog.services.user;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.blog.entities.Role;
import com.example.blog.entities.User;
import com.example.blog.repositories.RoleRepository;
import com.example.blog.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    public User createUser(User user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new RuntimeException("Error: Username cannot be empty!");
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Error: Username is already taken!");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new RuntimeException("Error: Password cannot be empty!");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        if (user.getRoles().isEmpty() || user.getRoles() == null) {
            Set<Role> roles = new HashSet<>();
            Role defaultRole = roleRepository.findByName("READER")
                    .orElseThrow(() -> new RuntimeException("Default role READER not found!"));
            roles.add(defaultRole);
            user.setRoles(roles);
        }
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User updateUser(Long id, User userDetails) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();

            if (userDetails.getUsername() != null) {
                if (userRepository.existsByUsername(userDetails.getUsername())
                        && !existingUser.getUsername().equals(userDetails.getUsername())) {
                    throw new RuntimeException("Error: Username is already taken!");
                } else if (userDetails.getUsername().trim().isEmpty()) {
                    throw new RuntimeException("Error: Username cannot be empty!");
                }
                existingUser.setUsername(userDetails.getUsername());
            }
            if (userDetails.getPassword() != null) {
                if (userDetails.getPassword().trim().isEmpty()) {
                    throw new RuntimeException("Error: Password cannot be empty!");
                }
                existingUser.setPassword(encoder.encode(userDetails.getPassword()));
            }
            if (userDetails.getRoles() != null && !userDetails.getRoles().isEmpty()) {
                existingUser.setRoles(userDetails.getRoles());
            }
            return userRepository.save(existingUser);
        } else {
            return null;
        }
    }

    public boolean deleteUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new CustomUserDetails(user);
    }
}
