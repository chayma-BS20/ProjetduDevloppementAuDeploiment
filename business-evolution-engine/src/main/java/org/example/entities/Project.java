package org.example.entities;
import com.fasterxml.jackson.annotation.JsonManagedReference;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entities.Task;
import java.time.LocalDate;
import java.util.List;
import org.example.entities.Ressource;
import org.example.entities.Team;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    private String projectName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private double budget;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<Task> tasks;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Ressource> resources;
}