package com.project.cloudator.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_info")

public class UserInfo {

    @Id
    private Long id;

    @Column(name = "name", nullable = true, length = 32)
    private String name;

    @Column(name = "surname", nullable = true, length = 32)
    private String surname;

    @Column(name = "birthday", nullable = true)
    private Date birthday;

    @Column(name = "address", nullable = true, length = 32)
    private String address;

    @Column(name = "foto", nullable = true, length = 255)
    private String foto;

    public String getPName() {
        if (name != null) {
            return name;
        } else {
            return "N/A";
        }
    }

    public String getPSurname() {
        if (surname != null) {
            return surname;
        } else {
            return "N/A";
        }
    }

    public String getPBirthday() {
        if (birthday != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            return dateFormat.format(birthday);
        } else {
            return "N/A";
        }
    }

    public String getPAddress() {
        if (address != null) {
            return address;
        } else {
            return "N/A";
        }
    }

    public String getPFoto() {
        return foto;
    }
}
