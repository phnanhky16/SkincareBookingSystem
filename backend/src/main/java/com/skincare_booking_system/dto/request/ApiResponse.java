package com.skincare_booking_system.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    String message;
    T result;
    boolean success;

    public ApiResponse(String message, T result, boolean success) {
        this.message = message;
        this.result = result;
        this.success = true;
    }
}
