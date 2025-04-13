package com.skincare_booking_system.mapper;

import org.mapstruct.Mapper;

import com.skincare_booking_system.dto.response.ServicesResponse;
import com.skincare_booking_system.entities.Services;

@Mapper(componentModel = "spring")
public interface ServicesMapper {
    ServicesResponse toServicesResponse(Services service);
}
