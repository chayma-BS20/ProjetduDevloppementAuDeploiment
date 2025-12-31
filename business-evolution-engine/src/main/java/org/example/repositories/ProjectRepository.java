package org.example.repositories;

import org.example.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    boolean existsByTeam_TeamId(Long teamId);
}
