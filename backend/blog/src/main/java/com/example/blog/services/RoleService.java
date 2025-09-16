package com.example.blog.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.blog.entities.Role;
import com.example.blog.repositories.RoleRepository;

@Service
// Service for managing user roles
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Role getRoleByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Role not found"));
    }

    public Set<Role> getAllRoles() {
        return new HashSet<>(roleRepository.findAll());
    }

    public Role getRoleById(Long roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));
    }

    public Role createRole(Role role) {
        if (roleRepository.findByName(role.getName()).isPresent()) {
            throw new RuntimeException("Role already exists");
        }
        return roleRepository.save(role);
    }

    public Role updateRole(Long id, String name) {
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found!"));
        if (name != null && !name.trim().isEmpty()) {
            existingRole.setName(name);
        }
        roleRepository.save(existingRole);
        return existingRole;
    }

    public boolean deleteRole(Long roleId) {
        if (!roleRepository.existsById(roleId)) {
            throw new RuntimeException("Role not found");
        }
        roleRepository.deleteById(roleId);
        return true;
    }
}
