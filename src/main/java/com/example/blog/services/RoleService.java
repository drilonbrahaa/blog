package com.example.blog.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.blog.entities.Role;
import com.example.blog.repositories.RoleRepository;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Role createRole(Role role) {
        if (role.getName() == null || role.getName().isEmpty()) {
            throw new RuntimeException("No role specified!");
        }
        return roleRepository.save(role);
    }

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    public Role getRole(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    public Role updateRole(Long id, Role role) {
        Role updatedRole = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found!"));
        if (role.getName() != null && !role.getName().isEmpty()) {
            updatedRole.setName(role.getName());
        }
        return roleRepository.save(updatedRole);
    }

    public boolean deleteRole(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        if (role.isPresent()) {
            roleRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
