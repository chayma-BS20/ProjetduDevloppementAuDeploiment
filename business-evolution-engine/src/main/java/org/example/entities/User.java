package org.example.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;     // <-- CLÉ PRIMAIRE

    private String username;
    private String email;
    private String password;

    public User() {
    }

    // getters / setters …

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // autres getters/setters...
}
