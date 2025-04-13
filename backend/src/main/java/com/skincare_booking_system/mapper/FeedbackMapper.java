package com.skincare_booking_system.mapper;

import org.mapstruct.Mapper;

import com.skincare_booking_system.dto.request.FeedbackRequest;
import com.skincare_booking_system.dto.response.FeedbackResponse;
import com.skincare_booking_system.entities.Feedback;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {
    Feedback toFeedback(FeedbackRequest request);

    FeedbackResponse toFeedbackResponse(Feedback feedback);
}
