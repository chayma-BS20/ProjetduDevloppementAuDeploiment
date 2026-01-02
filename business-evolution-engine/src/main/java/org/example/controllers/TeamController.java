package org.example.controllers;

import org.example.entities.Team;
import org.example.entities.User;
import org.example.repositories.TeamRepository;
import org.example.repositories.UserRepository;
import org.example.services.TeamService;
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

    @Autowired private TeamService teamService;

    @PreAuthorize("hasAnyRole('MANAGER', 'CHEF D EQUIPE')")
    @PostMapping public ResponseEntity<Team> createTeam(@RequestBody Team team) {
        return ResponseEntity.status(201).body(teamService.createTeam(team));
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'CHEF D EQUIPE')")
    @GetMapping public List<Team> getAllTeams() {
        return teamService.getAllTeams();
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/{id}") public ResponseEntity<Team> getTeamById(@PathVariable Long id) {
        return teamService.findTeamById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PatchMapping("/{id}") public ResponseEntity<Team> updateTeam(@PathVariable Long id, @RequestBody Team teamDetails) {
        return ResponseEntity.ok(teamService.updateTeam(id, teamDetails));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{id}") public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        if (!teamService.existsById(id)) return ResponseEntity.notFound().build();
        teamService.deleteTeam(id); // exceptions gérées service
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/by-name/{name}") public ResponseEntity<Team> getTeamByName(@PathVariable String name) {
        Team team = teamService.findByName(name);
        return team != null ? ResponseEntity.ok(team) : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'CHEF D EQUIPE')")
    @GetMapping("/{id}/members") public List<?> getTeamMembers(@PathVariable Long id) {
        return teamService.findMembersByTeamId(id);
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'CHEF D EQUIPE', 'MEMBRE')")
    @GetMapping("/my-team") public List<User> getMyTeamMembers() {
        return teamService.getMyTeamMembers();
    }

    @PreAuthorize("hasAnyRole( 'CHEF D EQUIPE', 'MEMBRE')")
    @GetMapping("/team/{teamId}") public List<User> getUsersByTeam(@PathVariable Long teamId) {
        return teamService.getUsersByTeam(teamId);
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/{teamId}/add-member/{userId}") public ResponseEntity<User> addTeamMember(@PathVariable Long teamId, @PathVariable Long userId) {
        return ResponseEntity.ok(teamService.addTeamMember(teamId, userId));
    }
}
