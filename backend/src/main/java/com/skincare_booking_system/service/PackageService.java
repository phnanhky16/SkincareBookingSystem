package com.skincare_booking_system.service;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.skincare_booking_system.dto.request.PackageRequest;
import com.skincare_booking_system.dto.request.PackageUpdateRequest;
import com.skincare_booking_system.dto.response.PackageResponse;
import com.skincare_booking_system.entities.Package;
import com.skincare_booking_system.entities.Services;
import com.skincare_booking_system.exception.AppException;
import com.skincare_booking_system.exception.ErrorCode;
import com.skincare_booking_system.mapper.PackageMapper;
import com.skincare_booking_system.repository.PackageRepository;
import com.skincare_booking_system.repository.ServicesRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class PackageService {

    PackageRepository packageRepository;
    PackageMapper packageMapper;
    ServicesRepository servicesRepository;

    public PackageResponse createPackage(PackageRequest packageRequest) {
        if (packageRepository.existsByPackageName(packageRequest.getPackageName())) {
            throw new AppException(ErrorCode.PACKAGE_EXIST);
        }
        List<String> servicesNames = packageRequest.getServicesNames();
        List<Services> services = servicesRepository.findServicesByServiceNameIn(servicesNames);
        if (services.isEmpty()) {
            throw new AppException(ErrorCode.SERVICES_INVALID);
        }
        boolean allActive = services.stream().allMatch(Services::getIsActive);
        if (!allActive) {
            throw new AppException(ErrorCode.SERVICES_FAILED);
        }
        LocalTime totalDuration = services.stream()
                .map(Services::getDuration) // Lấy duration của mỗi service
                .reduce(LocalTime.MIDNIGHT, (acc, dur) -> acc.plusHours(dur.getHour())
                        .plusMinutes(dur.getMinute())
                        .plusSeconds(dur.getSecond()));
        Package p = packageMapper.toPackage(packageRequest);
        p.setPackageActive(true);
        p.setServices(services);
        p.setDuration(totalDuration);
        return packageMapper.toPackageResponse(packageRepository.save(p));
    }

    public List<PackageResponse> getAllPackages() {
        List<Package> packages = packageRepository.findAll();
        if (packages.isEmpty()) {
            throw new AppException(ErrorCode.PACKAGE_NOT_FOUND);
        }
        return packages.stream().map(packageMapper::toPackageResponse).collect(Collectors.toList());
    }

    public List<PackageResponse> getPackagesByPackagesName(String packageName) {
        List<Package> packages = packageRepository.findPackageByPackageNameContainsIgnoreCase(packageName);
        if (packages.isEmpty()) {
            throw new AppException(ErrorCode.PACKAGE_NOT_FOUND);
        }

        return packages.stream().map(packageMapper::toPackageResponse).collect(Collectors.toList());
    }

    public List<PackageResponse> getPackagesByPackagesNameCUS(String packageName) {
        List<Package> packages =
                packageRepository.findPackageByPackageNameContainsIgnoreCaseAndPackageActiveTrue(packageName);
        if (packages.isEmpty()) {
            throw new AppException(ErrorCode.PACKAGE_NOT_FOUND);
        }

        return packages.stream().map(packageMapper::toPackageResponse).collect(Collectors.toList());
    }

    public List<PackageResponse> getPackagesActive() {
        List<Package> activePackages = packageRepository.findByPackageActiveTrue();
        if (activePackages.isEmpty()) {
            throw new AppException(ErrorCode.PACKAGE_NOT_FOUND);
        }
        return activePackages.stream().map(packageMapper::toPackageResponse).toList();
    }

    public List<PackageResponse> getPackagesDeactive() {
        List<Package> deActivePackages = packageRepository.findByPackageActiveFalse();
        if (deActivePackages.isEmpty()) {
            throw new AppException(ErrorCode.PACKAGE_NOT_FOUND);
        }
        return deActivePackages.stream().map(packageMapper::toPackageResponse).toList();
    }

    public PackageResponse updatePackage(String packageName, PackageUpdateRequest packageUpdateRequest) {
        Package p = packageRepository
                .findByPackageName(packageName)
                .orElseThrow(() -> new AppException(ErrorCode.SERVICES_INVALID));
        packageMapper.updatePackage(p, packageUpdateRequest);
        return packageMapper.toPackageResponse(p);
    }

    public String deactivePackage(String packageName) {
        Package p = packageRepository
                .findByPackageName(packageName)
                .orElseThrow(() -> new AppException(ErrorCode.SERVICES_INVALID));
        p.setPackageActive(false);
        packageRepository.save(p);
        return "Package deactivated successfully";
    }

    public String activePackage(String packageName) {
        Package p = packageRepository
                .findByPackageName(packageName)
                .orElseThrow(() -> new AppException(ErrorCode.SERVICES_INVALID));
        p.setPackageActive(true);
        packageRepository.save(p);
        return "Package activated successfully";
    }
}
