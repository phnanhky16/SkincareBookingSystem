package com.skincare_booking_system.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.skincare_booking_system.dto.request.StaffRequest;
import com.skincare_booking_system.dto.request.StaffUpdateRequest;
import com.skincare_booking_system.dto.response.StaffResponse;
import com.skincare_booking_system.entities.Staff;

@Mapper(componentModel = "spring")
public interface StaffMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toUpdateStaff(@MappingTarget Staff staff, StaffUpdateRequest request);

    Staff toStaff(StaffRequest request);

    StaffResponse toStaffResponse(Staff staff);
}
