package com.skincare_booking_system.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.skincare_booking_system.dto.request.ApiResponse;
import com.skincare_booking_system.dto.request.VoucherRequest;
import com.skincare_booking_system.dto.response.VoucherResponse;
import com.skincare_booking_system.service.VoucherService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/vouchers")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class VoucherController {

    VoucherService voucherService;

    @PostMapping
    ApiResponse<VoucherResponse> createVoucher(@RequestBody @Valid VoucherRequest request) {
        return ApiResponse.<VoucherResponse>builder()
                .result(voucherService.createVoucher(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<VoucherResponse>> getAllVouchers() {
        return ApiResponse.<List<VoucherResponse>>builder()
                .result(voucherService.getAllVouchers())
                .build();
    }

    @GetMapping("/code/{voucherCode}")
    ApiResponse<VoucherResponse> getVoucherByCode(@PathVariable String voucherCode) {
        return ApiResponse.<VoucherResponse>builder()
                .result(voucherService.getVoucherByCode(voucherCode))
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<VoucherResponse> getVoucherById(@PathVariable Long id) {
        return ApiResponse.<VoucherResponse>builder()
                .result(voucherService.getVoucherById(id))
                .build();
    }

    @GetMapping("/active")
    ApiResponse<List<VoucherResponse>> getActiveVouchers() {
        return ApiResponse.<List<VoucherResponse>>builder()
                .result(voucherService.getActiveVouchers())
                .build();
    }

    @GetMapping("/deactive")
    ApiResponse<List<VoucherResponse>> getInactiveVouchers() {
        return ApiResponse.<List<VoucherResponse>>builder()
                .result(voucherService.getInactiveVouchers())
                .build();
    }

    @PutMapping("/deactive/{id}")
    ApiResponse<String> deactivateVoucher(@PathVariable Long id) {
        return ApiResponse.<String>builder()
                .result(voucherService.deactivateVoucher(id))
                .build();
    }

    @PutMapping("/active/{id}")
    ApiResponse<String> activateVoucher(@PathVariable Long id) {
        return ApiResponse.<String>builder()
                .result(voucherService.activateVoucher(id))
                .build();
    }

    @PutMapping("/use/{voucherCode}")
    ApiResponse<String> useVoucher(@PathVariable String voucherCode) {
        return ApiResponse.<String>builder()
                .result(voucherService.useVoucher(voucherCode))
                .build();
    }

    @PutMapping("/update/{id}")
    ApiResponse<VoucherResponse> updateVoucher(@PathVariable Long id, @Valid @RequestBody VoucherRequest request) {
        return ApiResponse.<VoucherResponse>builder()
                .result(voucherService.updateVoucher(id, request))
                .build();
    }
}
