package com.skincare_booking_system.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.skincare_booking_system.dto.request.ApiResponse;
import com.skincare_booking_system.dto.request.ShiftRequest;
import com.skincare_booking_system.dto.request.UpdateShiftRequest;
import com.skincare_booking_system.dto.response.ShiftResponse;
import com.skincare_booking_system.service.ShiftService;

@RestController
@RequestMapping("/shifts")
public class ShiftController {

    private final ShiftService shiftService;

    public ShiftController(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    @PostMapping("/create")
    public ApiResponse<ShiftResponse> createShift(@RequestBody @Valid ShiftRequest request) {
        ShiftResponse shift = shiftService.createShift(request);

        return ApiResponse.<ShiftResponse>builder().result(shift).build();
    }

    @GetMapping("/getAll")
    public ApiResponse<List<ShiftResponse>> getAllShifts() {
        List<ShiftResponse> shifts = shiftService.getAllShifts();
        return ApiResponse.<List<ShiftResponse>>builder().result(shifts).build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<ShiftResponse> updateShift(
            @PathVariable long id, @RequestBody @Valid UpdateShiftRequest request) {
        ShiftResponse updatedShift = shiftService.updateShift(id, request);

        return ApiResponse.<ShiftResponse>builder().result(updatedShift).build();
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteShift(@PathVariable long id) {

        return ApiResponse.<Void>builder().success(true).build();
    }
}
