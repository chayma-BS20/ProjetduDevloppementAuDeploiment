package org.example.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

// IMPORTS EXPLICITES POUR ÉVITER LE CONFLIT
import org.example.entities.Task;
import org.example.entities.Role;
import org.example.entities.Team;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private String address;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = true)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private Team team;

    @OneToMany(mappedBy = "assignee", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Task> assignedTasks;
}