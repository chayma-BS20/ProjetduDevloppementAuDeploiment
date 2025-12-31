package org.example.controllers;

import org.example.entities.User;
import org.example.entities.Role;
import org.example.entities.Team;
import org.example.repositories.UserRepository;
import org.example.repositories.RoleRepository;
import org.example.repositories.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TeamRepository teamRepository;

    // ✅ MANAGER UNIQUEMENT (CRUD total)
    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/addUser")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        if (userRepository.existsByEmail(user.getEmail()) ||
                userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Role finalRole = null;
        if (user.getRole() != null && user.getRole().getRoleId() != null) {
            finalRole = roleRepository.findById(user.getRole().getRoleId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
            user.setRole(finalRole);
        }

        if (user.getTeam() != null && user.getTeam().getTeamId() != null) {
            Team team = teamRepository.findById(user.getTeam().getTeamId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found"));
            user.setTeam(team);
        }

        // ✅ LOGIQUE MANAGER : pas d'équipe
        if (finalRole != null && ("Manager".equals(finalRole.getTitle()) || "MANAGER".equals(finalRole.getTitle()))) {
            user.setTeam(null);
        }

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        }

        User savedUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PatchMapping("/{id}")
    public ResponseEntity<User> partialUpdateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (userDetails.getUsername() != null) user.setUsername(userDetails.getUsername());
        if (userDetails.getEmail() != null) user.setEmail(userDetails.getEmail());
        if (userDetails.getPhoneNumber() != null) user.setPhoneNumber(userDetails.getPhoneNumber());
        if (userDetails.getAddress() != null) user.setAddress(userDetails.getAddress());

        if (userDetails.getRole() != null && userDetails.getRole().getRoleId() != null) {
            Role role = roleRepository.findById(userDetails.getRole().getRoleId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
            user.setRole(role);
        }

        if (userDetails.getTeam() != null && userDetails.getTeam().getTeamId() != null) {
            Team team = teamRepository.findById(userDetails.getTeam().getTeamId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found"));
            user.setTeam(team);
        }

        return ResponseEntity.ok(userRepository.save(user));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ TOUS peuvent voir MEMBRES de LEUR équipe
    @PreAuthorize("hasAnyRole('MANAGER', 'CHEF_D_EQUIPE', 'MEMBRE')")
    @GetMapping("/my-team")
    public List<User> getMyTeamMembers() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (currentUser.getTeam() == null) {
            return Collections.emptyList();
        }
        return userRepository.findByTeamTeamId(currentUser.getTeam().getTeamId());
    }

    // ✅ TOUS peuvent voir LEUR équipe (vérif teamId)
    @PreAuthorize("hasAnyRole('MANAGER', 'CHEF_D_EQUIPE', 'MEMBRE')")
    @GetMapping("/team/{teamId}")
    public List<User> getUsersByTeam(@PathVariable Long teamId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (currentUser.getTeam() == null || !currentUser.getTeam().getTeamId().equals(teamId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied to this team");
        }
        return userRepository.findByTeamTeamId(teamId);
    }

    // ✅ Recherches spécifiques (MANAGER only)
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/by-email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/by-username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
