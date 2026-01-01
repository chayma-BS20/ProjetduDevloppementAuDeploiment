package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.entities.Project;
import org.example.entities.Task;
import org.example.entities.User;
import org.example.repositories.ProjectRepository;
import org.example.repositories.TaskRepository;
import org.example.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    // CREATE : task liée à un project
    public Task create(Task task, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));

        task.setTaskId(null);
        task.setProject(project);

        return taskRepository.save(task);
    }

    // READ one
    public Task getById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }

    // READ all
    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    // READ all tasks of a project
    public List<Task> getByProject(Long projectId) {
        return taskRepository.findByProject_ProjectId(projectId);
    }

    // UPDATE
    public Task update(Long id, Task newData) {
        Task existing = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        existing.setTitle(newData.getTitle());
        existing.setDescription(newData.getDescription());
        existing.setDueDateTime(newData.getDueDateTime());
        existing.setStatus(newData.getStatus());
        existing.setPriority(newData.getPriority());

        return taskRepository.save(existing);
    }

    // ASSIGN task to user
    public Task assignUser(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        task.setAssignee(user);
        return taskRepository.save(task);
    }

    // DELETE
    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }
    // Vérifie si l'utilisateur peut modifier la tâche
    public boolean canModifyTask(Long taskId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null || task.getProject() == null || task.getProject().getTeam() == null) return false;

        User currentUser = userRepository.findByEmail(email).orElse(null);
        if (currentUser == null || currentUser.getRole() == null) return false;

        String role = currentUser.getRole().getTitle();

        // MANAGER → accès total
        if ("MANAGER".equals(role)) return true;

        // CHEF_D_EQUIPE → uniquement pour les tâches de son équipe
        return "CHEF_D_EQUIPE".equals(role) &&
                currentUser.getTeam() != null &&
                currentUser.getTeam().getTeamId().equals(task.getProject().getTeam().getTeamId());
    }

    // Vérifie si l'utilisateur peut assigner la tâche
    public boolean canAssignTask(Long taskId) {
        // Même logique que update
        return canModifyTask(taskId);
    }

    // Vérifie si l'utilisateur peut supprimer la tâche
    public boolean canDeleteTask(Long taskId) {
        // Même logique que update
        return canModifyTask(taskId);
    }

}
