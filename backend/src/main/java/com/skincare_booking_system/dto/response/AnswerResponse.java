package com.skincare_booking_system.dto.response;

import lombok.Data;

@Data
public class AnswerResponse {

    private Long id;
    private String answerText;
    private String questionText;
    private String serviceName;
    private String serviceDescription;
}
