package com.project.cloudator.entity;

import org.springframework.stereotype.Component;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_access")
public class UserAccess {
    @Id
    private Long id;

    @Column(name = "counter")
    private Integer counter = 0;

    @Column(name = "isblocked")
    private boolean isblocked = false; 

    public boolean isAccountNonLocked() {
        return !this.isblocked; // Lógica para determinar si la cuenta está bloqueada o no
    }
}
