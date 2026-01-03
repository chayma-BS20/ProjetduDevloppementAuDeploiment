package org.example.services;

import org.example.entities.Role;
import org.example.repositories.RoleRepository;
import org.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    // ================= CREATE =================
    public Role createRole(Role role) {
        if (roleRepository.existsByTitle(role.getTitle())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        return roleRepository.save(role);
    }

    // ================= READ =================
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    public Optional<Role> getRoleByTitle(String title) {
        return roleRepository.findByTitle(title);
    }

    // ================= UPDATE =================
    public Role updateRole(Long id, Role roleDetails) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Optional.ofNullable(roleDetails.getTitle()).ifPresent(role::setTitle);
        Optional.ofNullable(roleDetails.getDescription()).ifPresent(role::setDescription);

        return roleRepository.save(role);
    }

    // ================= DELETE =================
    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        roleRepository.deleteById(id);
    }
}
