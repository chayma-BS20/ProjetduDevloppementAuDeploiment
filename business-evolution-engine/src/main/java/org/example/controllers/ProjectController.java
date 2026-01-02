package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.entities.Project;
import org.example.repositories.TeamRepository;
import org.example.services.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final TeamRepository teamRepository;

    // CREATE: MEMBER/MANAGER créent leur projet (assign manager = current user)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('CHEF D EQUIPE', 'MANAGER')")
    public Project create(@RequestParam Long teamId, @RequestBody Project project) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // Dans service: project.setManager(userRepository.findByEmail(auth.getName()).get());
        return projectService.create(project, teamId);
    }

    // READ one/all: MEMBER voit tout, MANAGER voit tout
    @GetMapping("/{id" +
            "" +
            "" +
            "}")
    @PreAuthorize("hasAnyRole('MANAGER', 'CHEF D EQUIPE' ,'MEMBRE')")
    public Project getById(@PathVariable Long id) {
        return projectService.getById(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'CHEF D EQUIPE', 'MEMBRE')")
    public List<Project> getAll() {
        return projectService.getAll();
    }

    // UPDATE: MANAGER full + owner check pour MEMBER/MANAGER
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'CHEF D EQUIPE')")
    public Project update(@PathVariable Long id, @RequestParam Long teamId, @RequestBody Project project) {
        return projectService.update(id, project, teamId);
    }

    // DELETE: MANAGER full + owner check pour MEMBER/MANAGER
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('MANAGER')")
    public void delete(@PathVariable Long id) {
        projectService.delete(id);
    }
}
//projet fonctionnel avec role manager