package org.example.controllers;

import org.example.entities.Role;
import org.example.repositories.RoleRepository;
import org.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "*")
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")

    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        if (roleRepository.existsByTitle(role.getTitle())) {
            return ResponseEntity.status(409).build(); // Conflict
        }
        Role savedRole = roleRepository.save(role);
        return ResponseEntity.status(201).body(savedRole);
    }

    @GetMapping
    @PreAuthorize("hasRole('MANAGER')")

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")

    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        Optional<Role> role = roleRepository.findById(id);
        return role.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")

    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @RequestBody Role roleDetails) {
        Optional<Role> optionalRole = roleRepository.findById(id);
        if (optionalRole.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Role role = optionalRole.get();
        Optional.ofNullable(roleDetails.getTitle()).ifPresent(role::setTitle);
        Optional.ofNullable(roleDetails.getDescription()).ifPresent(role::setDescription);

        Role updatedRole = roleRepository.save(role);
        return ResponseEntity.ok(updatedRole);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        if (!roleRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        roleRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-title/{title}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Role> getRoleByTitle(@PathVariable String title) {
        Role role = roleRepository.findByTitle(title);
        return role != null ? ResponseEntity.ok(role) : ResponseEntity.notFound().build();
    }
}
// membre et chef d'equipe foncionnel