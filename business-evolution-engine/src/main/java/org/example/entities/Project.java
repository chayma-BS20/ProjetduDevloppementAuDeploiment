package org.example.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entities.Task;
import java.time.LocalDate;
import java.util.List;

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

    // Relation Many-to-One vers Team (Un projet est géré par une seule équipe)
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    // Relation One-to-Many vers Task (Contient 0 ou * Tasks)
    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<Task> tasks;

    // Relation One-to-Many vers Ressource (Contient 0 ou * Ressources)
    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<Ressource> resources;
}