package com.skincare_booking_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.skincare_booking_system.dto.request.ApiResponse;
import com.skincare_booking_system.dto.request.ForgotPasswordRequest;
import com.skincare_booking_system.dto.response.ForgotPasswordResponse;
import com.skincare_booking_system.repository.UserRepository;
import com.skincare_booking_system.service.ForgotPasswordService;

@RestController
@RequestMapping("/forgot-password")
public class ForgotPasswordController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @PostMapping("/verifyEmail/{email}")
    public ApiResponse verifyEmail(@PathVariable String email) {
        return ApiResponse.builder()
                .result(forgotPasswordService.verifyEmail(email))
                .build();
    }

    @PostMapping("/verifyOtp/{email}/{otp}")
    public ApiResponse<ForgotPasswordResponse> verifyOtp(@PathVariable String email, @PathVariable Integer otp) {
        return ApiResponse.<ForgotPasswordResponse>builder()
                .result(forgotPasswordService.verifyOTP(otp, email))
                .build();
    }

    @PostMapping("/changeForgotPassword/{email}")
    public ApiResponse<ForgotPasswordResponse> changePassword(
            @PathVariable String email, @RequestBody ForgotPasswordRequest request) {

        return ApiResponse.<ForgotPasswordResponse>builder()
                .result(forgotPasswordService.changeForgotPassword(email, request))
                .build();
    }
}
