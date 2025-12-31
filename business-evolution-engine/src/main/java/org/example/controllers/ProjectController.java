package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.entities.Project;
import org.example.services.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    // CREATE : POST /api/projects?teamId=1
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Project create(@RequestParam Long teamId, @RequestBody Project project) {
        return projectService.create(project, teamId);
    }

    // READ one : GET /api/projects/1
    @GetMapping("/{id}")
    public Project getById(@PathVariable Long id) {
        return projectService.getById(id);
    }

    // READ all : GET /api/projects
    @GetMapping
    public List<Project> getAll() {
        return projectService.getAll();
    }

    // UPDATE : PUT /api/projects/1?teamId=2
    @PutMapping("/{id}")
    public Project update(@PathVariable Long id,
                          @RequestParam Long teamId,
                          @RequestBody Project project) {
        return projectService.update(id, project, teamId);
    }

    // DELETE : DELETE /api/projects/1
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        projectService.delete(id);
    }
}
