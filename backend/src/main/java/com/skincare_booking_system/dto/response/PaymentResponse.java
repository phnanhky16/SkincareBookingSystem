package com.skincare_booking_system.dto.response;

import java.time.LocalDate;
import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class PaymentResponse {
    long bookingId;
    LocalDate bookingDate;
    String customerName;
    String stylistName;
    Set<PaymentServiceResponse> services;
    String voucher;
    double totalAmount;

    public PaymentResponse(long bookingId, Set<PaymentServiceResponse> services, double totalAmount, String voucher) {
        this.bookingId = bookingId;
        this.services = services;
        this.totalAmount = totalAmount;
        this.voucher = voucher;
    }
}
