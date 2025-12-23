package org.example.repositories;

import org.example.entities.User;
import org.example.entities.Role;
import org.example.entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Authentification
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    List<User> findByRole(Role role);
    List<User> findByRoleTitle(String roleTitle);

    List<User> findByTeam(Team team);
    List<User> findByTeamTeamId(Long teamId);

    @Query("SELECT DISTINCT u FROM User u JOIN u.assignedTasks t WHERE t.project.projectId = :projectId")
    List<User> findUsersByProjectId(@Param("projectId") Long projectId);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
