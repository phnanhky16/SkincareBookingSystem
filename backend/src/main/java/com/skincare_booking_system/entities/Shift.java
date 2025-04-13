package com.skincare_booking_system.entities;

import java.time.LocalTime;
import java.util.Set;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@ToString(exclude = "therapistSchedules")
@EqualsAndHashCode(of = "shiftId")
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long shiftId;

    LocalTime startTime;
    LocalTime endTime;
    Integer limitBooking;

    @ManyToMany(mappedBy = "shifts", fetch = FetchType.LAZY)
    Set<TherapistSchedule> therapistSchedules;
}
