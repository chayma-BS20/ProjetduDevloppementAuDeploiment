package org.example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data               // Génère les getters, setters, toString, equals et hashCode
@NoArgsConstructor  // Constructeur sans arguments
@AllArgsConstructor // Constructeur avec tous les arguments
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

    private String name;
    private String description;

    // Relation 1:N vers User (members)
    // mappedBy="team" pointe vers la propriété 'team' dans la classe User.java
    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<User> members;

    // Relation 1:N vers Project
    // mappedBy="team" pointe vers la propriété 'team' dans la classe Project.java
    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Project> projects;
}