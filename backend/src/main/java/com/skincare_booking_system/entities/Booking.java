package com.skincare_booking_system.entities;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skincare_booking_system.constant.BookingStatus;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long bookingId;

    LocalDate bookingDay;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "therapist_id")
    Therapist therapist;

    @ManyToOne
    @JoinColumn(name = "therapist_schedule_id")
    TherapistSchedule therapistSchedule;

    @ManyToOne
    @JoinColumn(name = "slot_id")
    Slot slot;

    @ManyToOne
    @JoinColumn(name = "voucher_id")
    Voucher voucher;

    @Enumerated(EnumType.STRING)
    BookingStatus status;

    @ManyToMany
    @JoinTable(
            name = "booking_detail",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id"))
    @JsonIgnore
    Set<Services> services;

    @OneToOne(mappedBy = "booking")
    @JsonIgnore
    Feedback feedback;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    @JsonIgnore
    Payment payment;
}
