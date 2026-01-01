package org.example.controllers;

import org.example.entities.Team;
import org.example.entities.User;
import org.example.repositories.TeamRepository;
import org.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/teams")
@CrossOrigin(origins = "*")
public class TeamController {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    // =================== CRUD TEAM ===================

    @PreAuthorize("hasAnyRole('MANAGER', 'CHEF D EQUIPE')")
    @PostMapping
    public ResponseEntity<Team> createTeam(@RequestBody Team team) {
        Team savedTeam = teamRepository.save(team);
        return ResponseEntity.status(201).body(savedTeam);
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'CHEF D EQUIPE')")
    @GetMapping
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeamById(@PathVariable Long id) {
        Optional<Team> team = teamRepository.findById(id);
        return team.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('MANAGER')")
    @PatchMapping("/{id}")
    public ResponseEntity<Team> updateTeam(@PathVariable Long id, @RequestBody Team teamDetails) {
        Optional<Team> optionalTeam = teamRepository.findById(id);
        if (optionalTeam.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Team team = optionalTeam.get();
        Optional.ofNullable(teamDetails.getName()).ifPresent(team::setName);
        Optional.ofNullable(teamDetails.getDescription()).ifPresent(team::setDescription);

        Team updatedTeam = teamRepository.save(team);
        return ResponseEntity.ok(updatedTeam);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        if (!teamRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        teamRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/by-name/{name}")
    public ResponseEntity<Team> getTeamByName(@PathVariable String name) {
        Team team = teamRepository.findByName(name);
        return team != null ? ResponseEntity.ok(team) : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'CHEF D EQUIPE')")
    @GetMapping("/{id}/members")
    public List<?> getTeamMembers(@PathVariable Long id) {
        return teamRepository.findMembersByTeamId(id);
    }


    @PreAuthorize("hasAnyRole('MANAGER', 'CHEF D EQUIPE', 'MEMBRE')")
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

    // ✅ Tous les utilisateurs peuvent voir une équipe spécifique si ils en font partie
    @PreAuthorize("hasAnyRole( 'CHEF D EQUIPE', 'MEMBRE')")
    @GetMapping("/team/{teamId}")
    public List<User> getUsersByTeam(@PathVariable Long teamId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (currentUser.getTeam() == null || !currentUser.getTeam().getTeamId().equals(teamId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied to this team");
        }

        return userRepository.findByTeamTeamId(teamId);
    }


    @PreAuthorize("permitAll()")
    @PostMapping("/{teamId}/add-member/{userId}")
    public ResponseEntity<User> addTeamMember(@PathVariable Long teamId, @PathVariable Long userId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(" ENTRY POINT HIT !");
        System.out.println("JWT Principal: " + auth.getName());
        System.out.println("JWT Authorities: " + auth.getAuthorities());
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        String email = auth.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Current user not found"));

        System.out.println("=== DEBUG ADD MEMBER ===");
        System.out.println("JWT Email: " + email);
        System.out.println("CurrentUser: " + currentUser.getUsername() + " (ID: " + currentUser.getUserId() + ")");
        System.out.println("JWT Role: " + auth.getAuthorities());  // ← RÔLE JWT
        System.out.println("DB Role: " + (currentUser.getRole() != null ? currentUser.getRole().getTitle() : "NULL"));
        System.out.println("Current Team ID: " + (currentUser.getTeam() != null ? currentUser.getTeam().getTeamId() : "NULL"));
        System.out.println("Target Team ID: " + teamId);
        System.out.println("========================");

        String dbRole = currentUser.getRole() != null ? currentUser.getRole().getTitle() : null;

        if ("Manager".equals(dbRole)) {
            System.out.println(" MANAGER : OK partout");
            user.setTeam(team);
            return ResponseEntity.ok(userRepository.save(user));
        }

        if ("CHEF D EQUIPE".equals(dbRole) || "Chef d equipe".equals(dbRole)) {
            System.out.println(" CHEF vérification...");
            Long currentTeamId = currentUser.getTeam() != null ? currentUser.getTeam().getTeamId() : null;
            if (currentTeamId == null || !currentTeamId.equals(teamId)) {
                System.out.println(" CHEF : Équipe différente ! Current: " + currentTeamId + " vs Target: " + teamId);
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "Chef can only add to team " + currentTeamId);
            }
            System.out.println("CHEF : OK même équipe");
            user.setTeam(team);
            return ResponseEntity.ok(userRepository.save(user));
        }

        System.out.println(" Rôle insuffisant: " + dbRole);
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Role '" + dbRole + "' cannot add members");
    }

}