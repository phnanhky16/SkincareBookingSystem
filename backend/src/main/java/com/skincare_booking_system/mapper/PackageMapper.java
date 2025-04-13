package com.skincare_booking_system.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.skincare_booking_system.dto.request.PackageRequest;
import com.skincare_booking_system.dto.request.PackageUpdateRequest;
import com.skincare_booking_system.dto.response.PackageResponse;
import com.skincare_booking_system.entities.Package;

@Mapper(componentModel = "spring")
public interface PackageMapper {

    Package toPackage(PackageRequest packageRequest);

    void updatePackage(@MappingTarget Package pkg, PackageUpdateRequest packageUpdateRequest);

    PackageResponse toPackageResponse(Package pkg);
}
