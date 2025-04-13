package com.skincare_booking_system.mapper;

import org.mapstruct.*;

import com.skincare_booking_system.dto.response.InfoTherapistResponse;
import com.skincare_booking_system.dto.response.TherapistResponse;
import com.skincare_booking_system.entities.Therapist;

@Mapper(componentModel = "spring")
public interface TherapistMapper {

    TherapistResponse toTherapistResponse(Therapist therapist);

    InfoTherapistResponse toInfoTherapist(Therapist therapist);
}
