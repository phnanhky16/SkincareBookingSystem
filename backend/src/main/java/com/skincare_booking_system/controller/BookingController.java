package com.skincare_booking_system.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import com.skincare_booking_system.dto.request.*;
import com.skincare_booking_system.dto.response.BookingResponse;
import com.skincare_booking_system.dto.response.PaymentResponse;
import com.skincare_booking_system.dto.response.TherapistForBooking;
import com.skincare_booking_system.dto.response.TotalMoneyByBookingDay;
import com.skincare_booking_system.entities.Booking;
import com.skincare_booking_system.entities.Slot;
import com.skincare_booking_system.service.BookingService;
import com.skincare_booking_system.service.TherapistService;

@RestController
@RequestMapping("/booking")
public class BookingController {
    private final TherapistService therapistService;
    private final BookingService bookingService;

    public BookingController(TherapistService therapistService, BookingService bookingService) {
        this.therapistService = therapistService;
        this.bookingService = bookingService;
    }

    @PostMapping("/therapists")
    public ApiResponse<Set<TherapistForBooking>> getTherapistForBooking(
            @RequestBody BookingTherapist bookingTherapist) {
        return ApiResponse.<Set<TherapistForBooking>>builder()
                .result(bookingService.getTherapistForBooking(bookingTherapist))
                .build();
    }

    @PostMapping("/slots")
    public ApiResponse<List<Slot>> getListSlots(@RequestBody BookingSlots bookingSlots) {
        return ApiResponse.<List<Slot>>builder()
                .result(bookingService.getListSlot(bookingSlots))
                .build();
    }

    @PostMapping("/slots/{bookingId}")
    public ApiResponse<List<Slot>> getSlotsUpdateByCustomer(
            @RequestBody BookingSlots bookingSlots, @PathVariable long bookingId) {
        return ApiResponse.<List<Slot>>builder()
                .result(bookingService.getSlotsUpdateByCustomer(bookingSlots, bookingId))
                .build();
    }

    @PostMapping("/createBooking")
    public ApiResponse<Booking> createBooking(@RequestBody BookingRequest request) {
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setResult(bookingService.createNewBooking(request));
        apiResponse.setSuccess(true);
        return apiResponse;
    }

    @GetMapping("/{bookingId}")
    public ApiResponse<BookingResponse> getBookingById(@PathVariable long bookingId) {
        return ApiResponse.<BookingResponse>builder()
                .result(bookingService.getBookingById(bookingId))
                .build();
    }

    @GetMapping("/getallBooking")
    public ApiResponse<List<BookingResponse>> getAllBooking() throws Exception {
        try {
            return ApiResponse.<List<BookingResponse>>builder()
                    .result(bookingService.getAllBookings())
                    .build();
        } catch (Exception e) {
            return ApiResponse.<List<BookingResponse>>builder()
                    .message(e.getMessage())
                    .build();
        }
    }

    @PutMapping("/update/{bookingId}")
    public ApiResponse<BookingRequest> updateBooking(
            @PathVariable long bookingId, @RequestBody BookingRequest request) {
        return ApiResponse.<BookingRequest>builder()
                .result(bookingService.updateBooking(bookingId, request))
                .build();
    }

    @DeleteMapping("/delete/{bookingId}")
    public ApiResponse deleteBooking(@PathVariable Long bookingId) {
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setResult(bookingService.deleteBooking(bookingId));
        apiResponse.setSuccess(true);
        return apiResponse;
    }

    @PostMapping("/therapist/update")
    public ApiResponse<Set<TherapistForBooking>> getTherapistByStaff(
            @RequestBody AssignNewTherapistForBooking bookingTherapist) {
        return ApiResponse.<Set<TherapistForBooking>>builder()
                .result(bookingService.getTherapistWhenUpdateBookingByStaff(bookingTherapist))
                .build();
    }

    @GetMapping("/customer/{userId}/pending")
    public ApiResponse<List<Booking>> getPendingBookings(@PathVariable Long userId) {
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setResult(bookingService.getBookingByStatusPendingByCustomer(userId));
        apiResponse.setSuccess(true);
        return apiResponse;
    }

    @GetMapping("/customer/{userId}/completed")
    public ApiResponse<List<Booking>> getCompleteBookings(@PathVariable Long userId) {
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setResult(bookingService.getBookingByStatusCompletedByCustomer(userId));
        apiResponse.setSuccess(true);
        return apiResponse;
    }

    @GetMapping("/customer/{userId}/cancel")
    public ApiResponse<List<Booking>> getCancelBookings(@PathVariable Long userId) {
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setResult(bookingService.getBookingByStatusCancelByCustomer(userId));
        apiResponse.setSuccess(true);
        return apiResponse;
    }

    @PutMapping("/{bookingId}/checkin")
    public ApiResponse<String> checkIn(@PathVariable Long bookingId) {
        return ApiResponse.<String>builder()
                .result(bookingService.checkIn(bookingId))
                .build();
    }

    @PutMapping("/checkout")
    public ApiResponse<String> checkOut(
            @RequestParam(required = false) String transactionId, @RequestParam(required = false) Long bookingId) {
        return ApiResponse.<String>builder()
                .result(bookingService.checkout(transactionId, bookingId))
                .build();
    }

    @GetMapping("/therapist/{date}/{therapistId}")
    public ApiResponse<List<BookingResponse>> getTodayBookingForTherapist(
            @PathVariable Long therapistId, @PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        return ApiResponse.<List<BookingResponse>>builder()
                .result(therapistService.getBookingsForTherapistOnDate(therapistId, localDate))
                .build();
    }

    @PutMapping("/update/service/{bookingId}")
    public ApiResponse<BookingRequest> updateService(
            @PathVariable Long bookingId, @RequestBody BookingRequest request) {
        return ApiResponse.<BookingRequest>builder()
                .result(bookingService.updateBookingWithService(bookingId, request.getServiceId()))
                .build();
    }

    @PostMapping("/{bookingId}/finish")
    public ApiResponse<PaymentResponse> finishBooking(@PathVariable Long bookingId) {
        return ApiResponse.<PaymentResponse>builder()
                .result(bookingService.finishedService(bookingId))
                .build();
    }

    @GetMapping("/booking/total-money/day/month/{month}")
    public ApiResponse<List<TotalMoneyByBookingDay>> totalMoneyByBookingDay(@PathVariable int month) {
        return ApiResponse.<List<TotalMoneyByBookingDay>>builder()
                .result(bookingService.totalMoneyByBookingDayInMonth(month))
                .build();
    }

    @GetMapping("/status/{bookingId}")
    public ApiResponse<String> checkBookingStatus(@PathVariable long bookingId) {
        return ApiResponse.<String>builder()
                .result(bookingService.checkBookingStatus(bookingId))
                .build();
    }

    @GetMapping("/count/completed/{yearAndMonth}")
    public ApiResponse<Long> countAllBookingsComplete(@PathVariable String yearAndMonth) {
        return ApiResponse.<Long>builder()
                .result(bookingService.countAllBookingsCompleted(yearAndMonth))
                .build();
    }

    @PostMapping("/getBooking/{date}")
    public ApiResponse<List<BookingResponse>> getBookingByDate(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ApiResponse.<List<BookingResponse>>builder()
                .result(bookingService.getAllBookingsByDate(date))
                .build();
    }
}
