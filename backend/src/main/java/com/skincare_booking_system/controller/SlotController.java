package com.skincare_booking_system.controller;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.skincare_booking_system.dto.request.ApiResponse;
import com.skincare_booking_system.dto.request.UpdateSlotRequest;
import com.skincare_booking_system.dto.response.SlotResponse;
import com.skincare_booking_system.dto.response.SlotTimeResponse;
import com.skincare_booking_system.entities.Slot;
import com.skincare_booking_system.service.SlotService;

@RestController
@RequestMapping("/slot")
public class SlotController {
    @Autowired
    private SlotService slotService;

    @PostMapping("/create")
    ApiResponse<SlotResponse> createSlot(@Valid @RequestBody Slot slot) {
        return ApiResponse.<SlotResponse>builder()
                .result(slotService.createSlot(slot))
                .build();
    }

    @GetMapping("/getAllSlot")
    ApiResponse<List<Slot>> getAllSlot() {
        return ApiResponse.<List<Slot>>builder()
                .result(slotService.getAllSlot())
                .build();
    }

    @GetMapping("/{date}")
    ApiResponse<List<Slot>> getAllSlotValid(@PathVariable LocalDate date) {
        return ApiResponse.<List<Slot>>builder()
                .result(slotService.getAllSlotValid(date))
                .build();
    }

    @PutMapping("/{slotid}")
    ApiResponse<SlotResponse> updateSlot(@PathVariable Long slotid, @Valid @RequestBody Slot slot) {
        ApiResponse response = new ApiResponse<>();
        response.setResult(slotService.update(slotid, slot));
        response.setSuccess(true);
        return response;
    }

    @DeleteMapping("/{slotid}")
    ApiResponse<SlotResponse> deleteSlot(@PathVariable long slotid) {
        ApiResponse response = new ApiResponse<>();
        response.setResult(slotService.delete(slotid));
        return response;
    }

    @PostMapping()
    public ApiResponse<List<SlotResponse>> updateSlotWithTime(@RequestBody UpdateSlotRequest request) {
        return ApiResponse.<List<SlotResponse>>builder()
                .result(slotService.updateSlotTime(request))
                .build();
    }

    @GetMapping("/time/between")
    public ApiResponse<SlotTimeResponse> getSlotTimeBetween() {
        return ApiResponse.<SlotTimeResponse>builder()
                .result(slotService.getSlotTimeBetween())
                .build();
    }
}
