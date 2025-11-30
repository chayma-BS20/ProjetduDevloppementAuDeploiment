package org.example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ressource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resourceId;

    private String name;
    private String description;
    private String type;

    // Relation Many-to-One vers Project (Une ressource appartient à un seul projet)
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
}