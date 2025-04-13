package com.skincare_booking_system.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.skincare_booking_system.dto.request.ApiResponse;
import com.skincare_booking_system.dto.request.PackageRequest;
import com.skincare_booking_system.dto.request.PackageUpdateRequest;
import com.skincare_booking_system.dto.response.PackageResponse;
import com.skincare_booking_system.service.PackageService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/packages")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class PackageController {
    PackageService packageService;

    @PostMapping
    ApiResponse<PackageResponse> createRequest(@RequestBody @Valid PackageRequest request) {
        ApiResponse<PackageResponse> response = new ApiResponse<>();
        response.setResult(packageService.createPackage(request));
        response.setSuccess(true);
        return response;
    }

    @GetMapping
    ApiResponse<List<PackageResponse>> getAllPackages() {
        return ApiResponse.<List<PackageResponse>>builder()
                .result(packageService.getAllPackages())
                .build();
    }

    @GetMapping("/active")
    ApiResponse<List<PackageResponse>> getAllPackagesActive() {
        return ApiResponse.<List<PackageResponse>>builder()
                .result(packageService.getPackagesActive())
                .build();
    }

    @GetMapping("/deactive")
    ApiResponse<List<PackageResponse>> getAllPackagesDeactive() {
        return ApiResponse.<List<PackageResponse>>builder()
                .result(packageService.getPackagesDeactive())
                .build();
    }

    @GetMapping("/searchByName")
    ApiResponse<List<PackageResponse>> getPackagesByPackagesName(@RequestParam String packageName) {
        return ApiResponse.<List<PackageResponse>>builder()
                .result(packageService.getPackagesByPackagesName(packageName))
                .build();
    }

    @GetMapping("/searchByNameCUS")
    ApiResponse<List<PackageResponse>> getPackagesByPackagesNameCUS(@RequestParam String packageName) {
        return ApiResponse.<List<PackageResponse>>builder()
                .result(packageService.getPackagesByPackagesNameCUS(packageName))
                .build();
    }

    @PutMapping("/update/{packageName}")
    ApiResponse<PackageResponse> updatePackages(
            @PathVariable String packageName, @Valid @RequestBody PackageUpdateRequest packageUpdateRequest) {
        return ApiResponse.<PackageResponse>builder()
                .result(packageService.updatePackage(packageName, packageUpdateRequest))
                .build();
    }

    @PutMapping("/deactive/{packageName}")
    ApiResponse<String> deactivatePackage(@PathVariable String packageName) {
        return ApiResponse.<String>builder()
                .result(packageService.deactivePackage(packageName))
                .build();
    }

    @PutMapping("/active/{packageName}")
    ApiResponse<String> activatePackage(@PathVariable String packageName) {
        return ApiResponse.<String>builder()
                .result(packageService.activePackage(packageName))
                .build();
    }
}
