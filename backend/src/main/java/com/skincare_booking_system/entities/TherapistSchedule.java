package com.skincare_booking_system.entities;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(exclude = "shifts")
@EqualsAndHashCode(exclude = "shifts")
public class TherapistSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long therapistScheduleId;

    LocalDate workingDay;

    @ManyToOne
    @JoinColumn(name = "therapist_id")
    Therapist therapist;

    @ManyToMany
    @JoinTable(
            name = "specific_therapist_schedule",
            joinColumns = @JoinColumn(name = "therapist_schedule_id"),
            inverseJoinColumns = @JoinColumn(name = "shift_id"))
    @JsonIgnore
    Set<Shift> shifts;
}
