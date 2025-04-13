package com.skincare_booking_system.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class PaymentServiceResponse {
    String serviceName;
    String image;
    Double price;

    public PaymentServiceResponse(String serviceName, String image, Double price) {
        this.serviceName = serviceName;
        this.image = image;
        this.price = price;
    }
}
