package com.skincare_booking_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skincare_booking_system.dto.request.ApiResponse;
import com.skincare_booking_system.service.BookingService;
import com.skincare_booking_system.service.ServicesService;
import com.skincare_booking_system.service.UserService;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private BookingService bookingService;

    @Autowired
    private ServicesService servicesService;

    @Autowired
    private UserService customerService;

    @GetMapping("/booking/count/{month}")
    public ApiResponse<Long> countAllBookingsInMonth(@PathVariable int month) {
        return ApiResponse.<Long>builder()
                .result(bookingService.countAllBookingsInMonth(month))
                .build();
    }

    @GetMapping("/booking/total-money/month/{month}")
    public ApiResponse<Double> totalMoneyAllServiceInMonth(@PathVariable int month) {
        return ApiResponse.<Double>builder()
                .result(bookingService.totalMoneyByMonth(month))
                .build();
    }

    @GetMapping("/customer/count")
    public ApiResponse<Long> countAllCustomers() {
        return ApiResponse.<Long>builder()
                .result(customerService.countAllCustomers())
                .build();
    }

    @GetMapping("/service/count")
    public ApiResponse<Long> countAllServices() {
        return ApiResponse.<Long>builder()
                .result(servicesService.countAllServices())
                .build();
    }
}
