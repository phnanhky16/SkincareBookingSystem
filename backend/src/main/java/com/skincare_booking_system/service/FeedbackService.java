package com.skincare_booking_system.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.skincare_booking_system.dto.request.FeedbackRequest;
import com.skincare_booking_system.dto.response.FeedbackResponse;
import com.skincare_booking_system.entities.Booking;
import com.skincare_booking_system.entities.Feedback;
import com.skincare_booking_system.entities.User;
import com.skincare_booking_system.exception.AppException;
import com.skincare_booking_system.exception.ErrorCode;
import com.skincare_booking_system.repository.BookingRepository;
import com.skincare_booking_system.repository.FeedbackRepository;
import com.skincare_booking_system.repository.TherapistRepository;
import com.skincare_booking_system.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final TherapistRepository therapistRepository;
    private final BookingRepository bookingRepository;

    public FeedbackService(
            FeedbackRepository feedbackRepository,
            UserRepository userRepository,
            TherapistRepository therapistRepository,
            BookingRepository bookingRepository) {
        this.feedbackRepository = feedbackRepository;
        this.userRepository = userRepository;
        this.therapistRepository = therapistRepository;
        this.bookingRepository = bookingRepository;
    }

    public FeedbackResponse createFeedback(FeedbackRequest request) {
        User user = userRepository.findUserById(request.getUserId());
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        Booking booking = bookingRepository.findBookingByBookingId(request.getBookingId());
        if (booking == null) {
            throw new AppException(ErrorCode.BOOKING_NOT_FOUND);
        }
        Feedback feedback = new Feedback();
        feedback.setDay(request.getDate());
        feedback.setScore(request.getScore());
        feedback.setContent(request.getContent());
        feedback.setUser(user);
        feedback.setBooking(booking);
        feedbackRepository.save(feedback);
        FeedbackResponse response = new FeedbackResponse();
        response.setScore(feedback.getScore());
        response.setDate(feedback.getDay());
        response.setContent(feedback.getContent());
        response.setBookingId(feedback.getBooking().getBookingId());
        return response;
    }

    public FeedbackResponse getFeedbackByBookingId(long bookingId) {
        Feedback feedback = feedbackRepository.findFeedbackByBookingBookingId(bookingId);
        if (feedback == null) {
            return null;
        }
        FeedbackResponse response = new FeedbackResponse();
        response.setScore(feedback.getScore());
        response.setContent(feedback.getContent());
        response.setDate(feedback.getDay());
        response.setCustomerName(
                feedback.getUser().getFirstName() + " " + feedback.getUser().getLastName());
        response.setBookingId(feedback.getBooking().getBookingId());
        return response;
    }

    public List<FeedbackResponse> getFeedbackBytherapist(long therapistId) {
        List<Feedback> feedbacks = feedbackRepository.getListFeedbackByTherapist(therapistId);
        List<FeedbackResponse> responses = new ArrayList<>();
        for (Feedback feedback : feedbacks) {
            FeedbackResponse response = new FeedbackResponse();
            response.setScore(feedback.getScore());
            response.setContent(feedback.getContent());
            response.setDate(feedback.getDay());
            response.setCustomerName(
                    feedback.getUser().getFirstName() + " " + feedback.getUser().getLastName());
            response.setBookingId(feedback.getBooking().getBookingId());
            responses.add(response);
        }
        return responses;
    }
}
