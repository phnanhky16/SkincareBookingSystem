package com.skincare_booking_system.entities;

import java.time.LocalDate;

import jakarta.persistence.*;

import com.skincare_booking_system.constant.Roles;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String username;
    String password;
    String firstName;
    String lastName;
    String email;
    String phone;
    String address;
    String gender;
    LocalDate birthDate;
    Boolean status;

    @Enumerated(EnumType.STRING)
    Roles role;

    public User(
            long id,
            String username,
            String password,
            String firstName,
            String lastName,
            String email,
            String phone,
            String address,
            String gender,
            LocalDate birthDate,
            Boolean status,
            Roles role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.birthDate = birthDate;
        this.status = true;
        this.role = role;
    }
}
