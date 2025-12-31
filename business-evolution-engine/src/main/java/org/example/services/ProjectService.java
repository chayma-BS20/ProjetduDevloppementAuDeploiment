package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.entities.Project;
import org.example.entities.Team;
import org.example.repositories.ProjectRepository;
import org.example.repositories.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TeamRepository teamRepository;

    // CREATE
    public Project create(Project project, Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + teamId));

        // Empêcher qu'une team ait déjà un project (en plus du unique=true)
        if (projectRepository.existsByTeam_TeamId(teamId)) {
            throw new RuntimeException("This team already has a project.");
        }

        project.setProjectId(null); // sécurité
        project.setTeam(team);      // relation 1-1

        return projectRepository.save(project);
    }

    // READ (one)
    public Project getById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));
    }

    // READ (all)
    public List<Project> getAll() {
        return projectRepository.findAll();
    }

    // UPDATE
    public Project update(Long id, Project newData, Long teamId) {
        Project existing = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + teamId));

        // Si on change de team -> vérifier qu'elle n'a pas déjà un projet
        Long currentTeamId = existing.getTeam() != null ? existing.getTeam().getTeamId() : null;
        if (currentTeamId == null || !currentTeamId.equals(teamId)) {
            if (projectRepository.existsByTeam_TeamId(teamId)) {
                throw new RuntimeException("This team already has a project.");
            }
        }

        existing.setProjectName(newData.getProjectName());
        existing.setStartDate(newData.getStartDate());
        existing.setEndDate(newData.getEndDate());
        existing.setStatus(newData.getStatus());
        existing.setBudget(newData.getBudget());
        existing.setTeam(team);

        return projectRepository.save(existing);
    }

    // DELETE
    public void delete(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new RuntimeException("Project not found with id: " + id);
        }
        projectRepository.deleteById(id);
    }
}
