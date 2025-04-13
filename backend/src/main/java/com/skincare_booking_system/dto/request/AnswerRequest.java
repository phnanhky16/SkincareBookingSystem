package com.skincare_booking_system.dto.request;

import lombok.Data;

@Data
public class AnswerRequest {

    private String text;
    private Long questionId;
    private Long serviceId;
}
