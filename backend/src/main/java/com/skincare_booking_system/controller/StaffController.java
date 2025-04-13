package com.skincare_booking_system.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.skincare_booking_system.dto.request.*;
import com.skincare_booking_system.dto.response.StaffResponse;
import com.skincare_booking_system.entities.Booking;
import com.skincare_booking_system.service.StaffService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/staffs")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class StaffController {
    StaffService staffService;

    @PostMapping()
    ApiResponse<StaffResponse> createStaff(@RequestBody StaffRequest request) {
        return ApiResponse.<StaffResponse>builder()
                .result(staffService.createStaff(request))
                .build();
    }

    @GetMapping()
    ApiResponse<List<StaffResponse>> getStaffs() {
        return ApiResponse.<List<StaffResponse>>builder()
                .result(staffService.getAllStaffs())
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<StaffResponse> getActiveStaffs(@PathVariable Long id) {
        return ApiResponse.<StaffResponse>builder()
                .result(staffService.getStaffById(id))
                .build();
    }

    @GetMapping("/activeStaffs")
    ApiResponse<List<StaffResponse>> getActiveStaffs() {
        return ApiResponse.<List<StaffResponse>>builder()
                .result(staffService.getAllStaffsActive())
                .build();
    }

    @GetMapping("/inactiveStaffs")
    ApiResponse<List<StaffResponse>> getInactiveStaffs() {
        return ApiResponse.<List<StaffResponse>>builder()
                .result(staffService.getAllStaffsInactive())
                .build();
    }

    @GetMapping("/phone/{phoneNumber}")
    ApiResponse<StaffResponse> getStaff(@PathVariable("phoneNumber") String phone) {
        return ApiResponse.<StaffResponse>builder()
                .result(staffService.getStaffsbyPhone(phone))
                .build();
    }

    @GetMapping("/searchByName")
    public ApiResponse<List<StaffResponse>> searchStaffs(@RequestParam String name) {
        return ApiResponse.<List<StaffResponse>>builder()
                .result(staffService.searchStaffsByName(name))
                .build();
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<String> deleteStaff(@PathVariable Long id) {
        staffService.deleteStaff(id);
        return ResponseEntity.ok("Staff has been deleted");
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<String> restoreStaff(@PathVariable Long id) {
        staffService.restoreStaff(id);
        return ResponseEntity.ok("Staff restored successfully");
    }

    @PutMapping("/update/{id}")
    ApiResponse<StaffResponse> updateStaff(@PathVariable Long id, @RequestBody StaffUpdateRequest request) {
        return ApiResponse.<StaffResponse>builder()
                .result(staffService.updateStaff(id, request))
                .build();
    }

    @GetMapping("/staffInfo")
    ApiResponse<StaffResponse> getMyInfo() {
        return ApiResponse.<StaffResponse>builder()
                .result(staffService.getMyInfo())
                .build();
    }

    @PutMapping("/change-password")
    public ApiResponse<String> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        staffService.changePassword(request);
        return ApiResponse.<String>builder().result("Password has been changed").build();
    }

    @PutMapping("/reset-password/{id}")
    public ApiResponse<String> resetPassword(@PathVariable long id, @RequestBody ResetPasswordRequest request) {
        staffService.resetPassword(request, id);
        return ApiResponse.<String>builder().result("Password has been reset").build();
    }

    @PostMapping("/customer")
    public ApiResponse<StaffCreateCustomerRequest> staffCreateCustomer(
            @Valid @RequestBody StaffCreateCustomerRequest request) {
        return ApiResponse.<StaffCreateCustomerRequest>builder()
                .result(staffService.staffCreateCustomer(request))
                .build();
    }

    @PostMapping("/booking")
    public ApiResponse<Booking> createBookingByStaff(@Valid @RequestBody StaffCreateBookingRequest request) {
        ApiResponse response = new ApiResponse<>();
        response.setResult(staffService.createBookingByStaff(request));
        response.setSuccess(true);
        return response;
    }
}
