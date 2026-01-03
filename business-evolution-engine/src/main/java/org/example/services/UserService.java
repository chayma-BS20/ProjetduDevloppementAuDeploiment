package org.example.services;

import org.example.entities.User;
import org.example.entities.Role;
import org.example.entities.Team;
import org.example.repositories.UserRepository;
import org.example.repositories.RoleRepository;
import org.example.repositories.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TeamRepository teamRepository;

    public User createUser(User user) {

        if (userRepository.existsByEmail(user.getEmail()) ||
                userRepository.existsByUsername(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
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

        if (finalRole != null && "MANAGER".equals(finalRole.getTitle())) {
            user.setTeam(null);
        }

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        }

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User partialUpdateUser(Long id, User userDetails) {

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

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        userRepository.deleteById(id);
    }
}
