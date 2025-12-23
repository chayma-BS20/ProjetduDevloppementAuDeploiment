package org.example.repositories;

import org.example.entities.Team;
import org.example.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    Team findByName(String name);

    @Query("SELECT u FROM User u WHERE u.team.teamId = :teamId")
    List<User> findMembersByTeamId(@Param("teamId") Long teamId);

    List<Team> findByProjectsProjectId(Long projectId);
}
