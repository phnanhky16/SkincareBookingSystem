package com.skincare_booking_system.dto.request;

import com.skincare_booking_system.entities.Therapist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TherapistAvailability {
    private Therapist therapist;
    private int availableSlots;
    private double rating;
}
