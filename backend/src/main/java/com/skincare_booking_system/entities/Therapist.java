package com.skincare_booking_system.entities;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skincare_booking_system.constant.Roles;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Therapist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String username;
    String password;
    String fullName;
    String email;
    String phone;
    String address;
    String gender;
    String imgUrl;
    ;
    LocalDate birthDate;
    Boolean status;
    Integer yearExperience;

    @OneToMany(mappedBy = "therapist")
    @JsonIgnore
    @ToString.Exclude
    Set<TherapistSchedule> therapistSchedules;

    @OneToMany(mappedBy = "therapist")
    @JsonIgnore
    Set<Booking> bookings;

    @Enumerated(EnumType.STRING)
    Roles role;
}
