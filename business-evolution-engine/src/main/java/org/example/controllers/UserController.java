package org.example.controllers;

import org.example.entities.User;
import org.example.entities.Role;
import org.example.entities.Team;
import org.example.repositories.UserRepository;
import org.example.repositories.RoleRepository;
import org.example.repositories.TeamRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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


    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {

        // 1. Vérifier doublons
        if (userRepository.existsByEmail(user.getEmail()) ||
                userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // 2. Récupérer ROLE (avec vérif null)
        Role finalRole = null;
        if (user.getRole() != null && user.getRole().getRoleId() != null) {
            finalRole = roleRepository.findById(user.getRole().getRoleId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
            System.out.println("DEBUG: Role récupéré -> id: " + finalRole.getRoleId() + ", title: " + finalRole.getTitle());
            user.setRole(finalRole);
        }

        // 3. Récupérer TEAM (seulement si PAS Manager)
        if (user.getTeam() != null && user.getTeam().getTeamId() != null) {
            Team team = teamRepository.findById(user.getTeam().getTeamId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found"));
            user.setTeam(team);
        }

        // 4. LOGIQUE MANAGER : pas d'équipe (team_id = NULL en BDD)
        if (finalRole != null && "Manager".equals(finalRole.getTitle())) {
            user.setTeam(null);  // Manager = sans équipe (team_id = NULL)
            System.out.println("DEBUG: Manager détecté → team mise à null");
        }

        // 5. HASHER PASSWORD
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        }

        User savedUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }



    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ---------------- Récupérer un utilisateur par ID ----------------
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ---------------- Mise à jour partielle ----------------
    @PatchMapping("/{id}")
    public ResponseEntity<User> partialUpdateUser(
            @PathVariable Long id,
            @RequestBody User userDetails) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (userDetails.getUsername() != null)
            user.setUsername(userDetails.getUsername());

        if (userDetails.getEmail() != null)
            user.setEmail(userDetails.getEmail());

        if (userDetails.getPhoneNumber() != null)
            user.setPhoneNumber(userDetails.getPhoneNumber());

        if (userDetails.getAddress() != null)
            user.setAddress(userDetails.getAddress());

        // Optionnel : mise à jour du rôle
        if (userDetails.getRole() != null && userDetails.getRole().getRoleId() != null) {
            Role role = roleRepository.findById(userDetails.getRole().getRoleId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
            user.setRole(role);
        }

        // Optionnel : mise à jour de l'équipe
        if (userDetails.getTeam() != null && userDetails.getTeam().getTeamId() != null) {
            Team team = teamRepository.findById(userDetails.getTeam().getTeamId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found"));
            user.setTeam(team);
        }

        return ResponseEntity.ok(userRepository.save(user));
    }

    // ---------------- Supprimer un utilisateur ----------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ---------------- Recherches par email / username ----------------
    @GetMapping("/by-email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ---------------- Recherches par rôle ----------------
    @GetMapping("/entrepreneurs")
    public List<User> getEntrepreneurs() {
        return userRepository.findByRoleTitle("ENTREPRENEUR");
    }

    @GetMapping("/chefs-equipe")
    public List<User> getChefsEquipe() {
        return userRepository.findByRoleTitle("CHEF_EQUIPE");
    }

    @GetMapping("/collaborateurs")
    public List<User> getCollaborateurs() {
        return userRepository.findByRoleTitle("COLLABORATEUR");
    }

    // ---------------- Recherches par équipe ----------------
    @GetMapping("/team/{teamId}")
    public List<User> getUsersByTeam(@PathVariable Long teamId) {
        return userRepository.findByTeamTeamId(teamId);
    }
}
