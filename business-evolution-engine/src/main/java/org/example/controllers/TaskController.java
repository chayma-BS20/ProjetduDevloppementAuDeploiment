package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.entities.Task;
import org.example.services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    // CREATE : MANAGER, CHEF_D_EQUIPE, MEMBRE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('CHEF_D_EQUIPE','MEMBRE')")
    public Task create(@RequestParam Long projectId,
                       @RequestBody Task task) {
        return taskService.create(task, projectId);
    }

    // READ one : tous les rôles
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER','CHEF_D_EQUIPE','MEMBRE')")
    public Task getById(@PathVariable Long id) {
        return taskService.getById(id);
    }

    // READ all : tous les rôles
    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER','CHEF_D_EQUIPE','MEMBRE')")
    public List<Task> getAll() {
        return taskService.getAll();
    }

    // READ tasks by project : tous les rôles
    @GetMapping("/project/{projectId}")
    @PreAuthorize("hasAnyRole('MANAGER','CHEF_D_EQUIPE','MEMBRE')")
    public List<Task> getByProject(@PathVariable Long projectId) {
        return taskService.getByProject(projectId);
    }

    // UPDATE : MANAGER peut tout, CHEF_D_EQUIPE sur son projet, MEMBRE sur ses tâches
    @PutMapping("/{id}")
    @PreAuthorize("@taskService.canModifyTask(#id)")
    public Task update(@PathVariable Long id,
                       @RequestBody Task task) {
        return taskService.update(id, task);
    }

    // ASSIGN USER : MANAGER et CHEF_D_EQUIPE sur leur projet
    @PutMapping("/{id}/assign")
    @PreAuthorize("hasAnyRole('CHEF_D_EQUIPE')")
    public Task assignUser(@PathVariable Long id,
                           @RequestParam Long userId) {
        return taskService.assignUser(id, userId);
    }

    // DELETE : MANAGER et CHEF_D_EQUIPE sur leur projet
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@taskService.canDeleteTask(#id)")
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }
}
//role member fonctionne avec Task
//role manager fonctionnel avec Task