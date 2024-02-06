package com.project.cloudator.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    public String getDisplayName() {
        switch (name) {
            case "ROLE_SUPERADMIN":
                return "Super Administrador";

            case "ROLE_ADMIN":
                return "Administrador";

            case "ROLE_PREMIUM":
                return "Premium";

            case "ROLE_USER":
                return "Estándar";

            default:
                return name;
        }

    }
}
