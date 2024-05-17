package com.project.cloudator.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
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

    @Column(nullable = false)
    private Long maxStorage;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    public BigInteger getMaxStorage() {
        Long maxStorage = this.maxStorage;
        return BigInteger.valueOf(maxStorage);
    }

    public String getDisplayName() {
        switch (name) {
            case "ROLE_SUPERADMIN":
                return "Super Admin";

            case "ROLE_ADMIN":
                return "Administrador";

            case "ROLE_PREMIUM":
                return "Premium";

            case "ROLE_USER":
                return "Basico";

            default:
                return name;
        }

    }
}
