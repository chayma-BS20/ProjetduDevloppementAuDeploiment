package org.example.entities;
import  org.example.entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data // Getters, Setters, toString, equals/hashCode
@NoArgsConstructor // Constructeur sans arguments
@AllArgsConstructor // Constructeur avec tous les arguments
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    private String title; // Correction: 'title' au lieu de 'name' basé sur le diagramme
    private String description;

    // Relation Bidirectionnelle One-to-Many : MappedBy doit pointer vers la propriété 'role' dans User
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private List<User> users;
}