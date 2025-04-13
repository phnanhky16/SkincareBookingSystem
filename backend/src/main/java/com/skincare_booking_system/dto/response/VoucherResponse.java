package com.skincare_booking_system.dto.response;

import java.time.LocalDate;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherResponse {

    Long voucherId;
    String voucherName;
    String voucherCode;
    Double percentDiscount;
    Boolean isActive;
    LocalDate expiryDate;
    Integer quantity;
}
