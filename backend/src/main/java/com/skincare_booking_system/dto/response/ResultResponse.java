package com.skincare_booking_system.dto.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ResultResponse {

    private Long id;
    private Long userId;
    private String answerText;
    private String questionText;
    private String serviceName;
    private String serviceDescription;
    private LocalDateTime createdAt;
}
