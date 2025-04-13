package com.skincare_booking_system.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.skincare_booking_system.dto.request.ApiResponse;
import com.skincare_booking_system.dto.request.SpecificTherapistScheduleRequest;
import com.skincare_booking_system.dto.response.SpecificTherapistScheduleResponse;
import com.skincare_booking_system.dto.response.TherapistScheduleResponse;
import com.skincare_booking_system.service.TherapistScheduleService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/schedule")
public class TherapistScheduleController {
    private final TherapistScheduleService therapistScheduleService;

    public TherapistScheduleController(TherapistScheduleService therapistScheduleService) {
        this.therapistScheduleService = therapistScheduleService;
    }

    @PostMapping("/therapist")
    public ApiResponse<SpecificTherapistScheduleRequest> createTherapistSchedule(
            @RequestBody SpecificTherapistScheduleRequest request) {
        log.info("Received request: {}", request);
        try {
            ApiResponse<SpecificTherapistScheduleRequest> apiResponse = new ApiResponse<>();
            apiResponse.setResult(therapistScheduleService.createTherapistSchedule(request));
            apiResponse.setSuccess(true);
            return apiResponse;
        } catch (Exception e) {
            log.error("Error creating therapist schedule: ", e);
            throw e;
        }
    }

    @GetMapping("/therapist/{date}")
    public ApiResponse<List<SpecificTherapistScheduleRequest>> getTherapistScheduleByDay(@PathVariable LocalDate date) {
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setResult(therapistScheduleService.getTherapistScheduleByDay(date));
        apiResponse.setSuccess(true);
        return apiResponse;
    }

    @GetMapping("/therapist/getById/{id}")
    public ApiResponse<SpecificTherapistScheduleResponse> getTherapistSchedule(@PathVariable long id) {
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setResult(therapistScheduleService.getTherapistSchedule(id));
        apiResponse.setSuccess(true);
        return apiResponse;
    }

    @PutMapping("/therapist/update/{id}")
    public ApiResponse<SpecificTherapistScheduleResponse> updateTherapistSchedule(
            @PathVariable long id, @RequestBody SpecificTherapistScheduleRequest request) {
        return ApiResponse.<SpecificTherapistScheduleResponse>builder()
                .result(therapistScheduleService.updateTherapistSchedule(id, request))
                .build();
    }

    @DeleteMapping("/therapist/{id}")
    public ApiResponse<SpecificTherapistScheduleResponse> deleteTherapistSchedule(@PathVariable long id) {
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setResult(therapistScheduleService.deleteTherapistSchedule(id));
        apiResponse.setSuccess(true);
        return apiResponse;
    }

    @GetMapping("/therapist/month/{therapistId}/{month}")
    public ApiResponse<List<TherapistScheduleResponse>> getTherapistScheduleByMonth(
            @PathVariable long therapistId, @PathVariable int month) {
        return ApiResponse.<List<TherapistScheduleResponse>>builder()
                .result(therapistScheduleService.getTherapistScheduleInMonth(therapistId, month))
                .build();
    }
}
