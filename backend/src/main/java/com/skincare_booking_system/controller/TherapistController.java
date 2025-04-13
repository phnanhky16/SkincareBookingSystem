package com.skincare_booking_system.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.skincare_booking_system.dto.request.*;
import com.skincare_booking_system.dto.response.InfoTherapistResponse;
import com.skincare_booking_system.dto.response.TherapistResponse;
import com.skincare_booking_system.dto.response.TherapistRevenueResponse;
import com.skincare_booking_system.service.TherapistService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/therapists")
public class TherapistController {
    @Autowired
    private TherapistService therapistService;

    @PostMapping()
    ApiResponse<TherapistResponse> createTherapist(
            @RequestParam("username") String userName,
            @RequestParam("password") String password,
            @RequestParam("fullName") String fullName,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("address") String address,
            @RequestParam("gender") String gender,
            @RequestParam("birthDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthDate,
            @RequestParam("yearExperience") Integer yearExperience,
            @RequestParam("imgUrl") MultipartFile imgUrl)
            throws IOException {
        TherapistResponse therapistResponse = therapistService.createTherapist(
                userName, password, fullName, email, phone, address, gender, birthDate, yearExperience, imgUrl);
        return ApiResponse.<TherapistResponse>builder()
                .result(therapistResponse)
                .build();
    }

    @GetMapping()
    ApiResponse<List<TherapistResponse>> getTherapists() {
        return ApiResponse.<List<TherapistResponse>>builder()
                .result(therapistService.getAllTherapists())
                .build();
    }

    @GetMapping("/activeTherapists")
    ApiResponse<List<TherapistResponse>> getActiveTherapists() {
        return ApiResponse.<List<TherapistResponse>>builder()
                .result(therapistService.getAllTherapistsActive())
                .build();
    }

    @GetMapping("/inactiveTherapists")
    ApiResponse<List<TherapistResponse>> getInactiveTherapists() {
        return ApiResponse.<List<TherapistResponse>>builder()
                .result(therapistService.getAllTherapistsInactive())
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<TherapistResponse> getTherapist(@PathVariable("id") long id) {
        return ApiResponse.<TherapistResponse>builder()
                .result(therapistService.getTherapistbyId(id))
                .build();
    }

    @PutMapping("/updateTherapist/{id}")
    ApiResponse<TherapistResponse> updateUser(
            @PathVariable long id,
            @RequestParam("fullName") String fullName,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("address") String address,
            @RequestParam("gender") String gender,
            @RequestParam("birthDate") LocalDate birthDate,
            @RequestParam("yearExperience") Integer yearExperience,
            @RequestParam("imgUrl") MultipartFile imgUrl)
            throws IOException {

        return ApiResponse.<TherapistResponse>builder()
                .result(therapistService.updateTherapist(
                        id, fullName, email, phone, address, gender, birthDate, yearExperience, imgUrl))
                .build();
    }

    @GetMapping("/searchByName")
    public ApiResponse<List<TherapistResponse>> searchTherapists(@RequestParam String name) {
        return ApiResponse.<List<TherapistResponse>>builder()
                .result(therapistService.searchTherapistsByName(name))
                .build();
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<String> deleteTherapist(@PathVariable long id) {
        therapistService.deleteTherapistbyId(id);
        return ResponseEntity.ok("Therapist has been deleted");
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<String> restoreTherapist(@PathVariable long id) {
        therapistService.restoreTherapistById(id);
        return ResponseEntity.ok("Therapist restored successfully");
    }

    @GetMapping("/therapistProfile")
    ApiResponse<InfoTherapistResponse> getMyInfo() {
        return ApiResponse.<InfoTherapistResponse>builder()
                .result(therapistService.getMyInfo())
                .build();
    }

    @PutMapping("/change-password")
    public ApiResponse<String> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        therapistService.changePassword(request);
        return ApiResponse.<String>builder().result("Password has been changed").build();
    }

    @PutMapping("/reset-password/{id}")
    public ApiResponse<String> resetPassword(@PathVariable long id, @RequestBody ResetPasswordRequest request) {
        therapistService.resetPassword(request, id);
        return ApiResponse.<String>builder().result("Password has been reset").build();
    }

    @GetMapping("/{therapistId}/revenue/{yearAndMonth}")
    public ApiResponse<TherapistRevenueResponse> getTherapistRevenue(
            @PathVariable long therapistId, @PathVariable String yearAndMonth) {
        TherapistRevenueResponse totalRevenue = therapistService.getTherapistRevenue(therapistId, yearAndMonth);
        return ApiResponse.<TherapistRevenueResponse>builder()
                .result(totalRevenue)
                .build();
    }
}
