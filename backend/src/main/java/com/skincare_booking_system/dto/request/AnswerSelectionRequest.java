package com.skincare_booking_system.dto.request;

import java.util.List;

import lombok.Data;

@Data
public class AnswerSelectionRequest {

    private Long userId;
    private List<Long> answerIds;
}
