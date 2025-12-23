package org.example.controllers;

import org.example.entities.User;
import org.example.entities.Role;
import org.example.repositories.UserRepository;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.example.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        // Vérifier si email/username existe déjà
        if (userRepository.existsByEmail(user.getEmail()) ||
                userRepository.existsByUsername(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }
        User savedUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

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

        return ResponseEntity.ok(userRepository.save(user));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

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

    @GetMapping("/entrepreneurs")
    public List<User> getEntrepreneurs() {
        return userRepository.findByRoleTitle("Entrepreneur");
    }

    @GetMapping("/chefs-equipe")
    public List<User> getChefsEquipe() {
        return userRepository.findByRoleTitle("Chef d'équipe");
    }

    @GetMapping("/collaborateurs")
    public List<User> getCollaborateurs() {
        return userRepository.findByRoleTitle("Collaborateur");
    }

    @GetMapping("/team/{teamId}")
    public List<User> getUsersByTeam(@PathVariable Long teamId) {
        return userRepository.findByTeamTeamId(teamId);
    }
}
