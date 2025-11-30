package org.example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data               // Génère les getters, setters, toString, equals et hashCode
@NoArgsConstructor  // Génère un constructeur sans arguments
@AllArgsConstructor // Génère un constructeur avec tous les arguments
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    private String title;
    private String description;

    // Utilisation de LocalDateTime pour la date et l'heure de l'échéance
    private LocalDateTime dueDateTime;

    private String status;
    private int priority;

    // Relation Many-to-One vers Project (Une tâche appartient à un seul projet)
    @ManyToOne
    @JoinColumn(name = "project_id") // Clé étrangère dans la table Task
    private Project project;

    // Relation Many-to-One vers User (L'utilisateur assigné à cette tâche)
    @ManyToOne
    @JoinColumn(name = "assignee_user_id") // Clé étrangère vers la table User
    private User assignee;
}