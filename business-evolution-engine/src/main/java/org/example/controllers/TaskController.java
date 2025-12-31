package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.entities.Task;
import org.example.services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    // CREATE : POST /api/tasks?projectId=1
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task create(@RequestParam Long projectId,
                       @RequestBody Task task) {
        return taskService.create(task, projectId);
    }

    // READ one : GET /api/tasks/1
    @GetMapping("/{id}")
    public Task getById(@PathVariable Long id) {
        return taskService.getById(id);
    }

    // READ all : GET /api/tasks
    @GetMapping
    public List<Task> getAll() {
        return taskService.getAll();
    }

    // READ tasks by project : GET /api/tasks/project/1
    @GetMapping("/project/{projectId}")
    public List<Task> getByProject(@PathVariable Long projectId) {
        return taskService.getByProject(projectId);
    }

    // UPDATE : PUT /api/tasks/1
    @PutMapping("/{id}")
    public Task update(@PathVariable Long id,
                       @RequestBody Task task) {
        return taskService.update(id, task);
    }

    // ASSIGN USER : PUT /api/tasks/1/assign?userId=3
    @PutMapping("/{id}/assign")
    public Task assignUser(@PathVariable Long id,
                           @RequestParam Long userId) {
        return taskService.assignUser(id, userId);
    }

    // DELETE : DELETE /api/tasks/1
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }
}
