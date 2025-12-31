package org.example.repositories;

import org.example.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    // lister les tasks d’un project
    List<Task> findByProject_ProjectId(Long projectId);
}
