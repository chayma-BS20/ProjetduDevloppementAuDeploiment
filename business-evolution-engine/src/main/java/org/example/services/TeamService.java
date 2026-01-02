package org.example.services;

import org.example.entities.Team;
import org.example.entities.User;
import org.example.repositories.TeamRepository;
import org.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    @Autowired private TeamRepository teamRepository;
    @Autowired private UserRepository userRepository;

    public Team createTeam(Team team) {
        return teamRepository.save(team);
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public Optional<Team> findTeamById(Long id) {
        return teamRepository.findById(id);
    }

    public Team updateTeam(Long id, Team teamDetails) {
        Optional<Team> optionalTeam = teamRepository.findById(id);
        if (optionalTeam.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Team team = optionalTeam.get();
        Optional.ofNullable(teamDetails.getName()).ifPresent(team::setName);
        Optional.ofNullable(teamDetails.getDescription()).ifPresent(team::setDescription);
        return teamRepository.save(team);
    }

    public boolean existsById(Long id) {
        return teamRepository.existsById(id);
    }

    public Team findByName(String name) {
        return teamRepository.findByName(name);
    }

    public List<?> findMembersByTeamId(Long id) {
        return teamRepository.findMembersByTeamId(id);
    }

    public List<User> getMyTeamMembers() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (currentUser.getTeam() == null) return Collections.emptyList();
        return userRepository.findByTeamTeamId(currentUser.getTeam().getTeamId());
    }

    public List<User> getUsersByTeam(Long teamId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (currentUser.getTeam() == null || !currentUser.getTeam().getTeamId().equals(teamId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied to this team");
        }
        return userRepository.findByTeamTeamId(teamId);
    }

    public User addTeamMember(Long teamId, Long userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        String email = auth.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Current user not found"));

        String dbRole = currentUser.getRole() != null ? currentUser.getRole().getTitle() : null;

        if ("Manager".equals(dbRole)) {
            user.setTeam(team);
            return userRepository.save(user);
        }
        if (("CHEF D EQUIPE".equals(dbRole) || "Chef d equipe".equals(dbRole))
                && currentUser.getTeam() != null && currentUser.getTeam().getTeamId().equals(teamId)) {
            user.setTeam(team);
            return userRepository.save(user);
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Role '" + dbRole + "' cannot add members");
    }
    public void deleteTeam(Long id) {
        List<User> teamUsers = userRepository.findByTeamTeamId(id);
        teamUsers.forEach(user -> {
            user.setTeam(null);
            userRepository.save(user);
        });

        teamRepository.deleteById(id);
    }
//role manager avec team fonctionnel

}
