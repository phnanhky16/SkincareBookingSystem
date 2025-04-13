package com.skincare_booking_system.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherRequest {

    @NotBlank(message = "VOUCHER_NAME_REQUIRED")
    String voucherName;

    @NotBlank(message = "VOUCHER_CODE_REQUIRED")
    String voucherCode;

    @Min(value = 0, message = "PERCENT_DISCOUNT_MIN")
    @Max(value = 100, message = "PERCENT_DISCOUNT_MAX")
    Double percentDiscount;

    Boolean isActive;

    @NotNull(message = "EXPIRY_DATE_REQUIRED")
    LocalDate expiryDate;

    @NotNull(message = "QUANTITY_REQUIRED")
    @Min(value = 0, message = "QUANTITY_MIN")
    Integer quantity;
}
