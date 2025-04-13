package com.skincare_booking_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.skincare_booking_system.entities.Feedback;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    Feedback findFeedbackByBookingBookingId(long id);

    @Query(
            value = "SELECT f.* FROM feedback f\n" + "INNER JOIN booking b \n"
                    + "ON f.booking_id = b.booking_id\n"
                    + "INNER JOIN therapist_schedule ts\n"
                    + "ON b.therapist_schedule_id = ts.therapist_schedule_id\n"
                    + "WHERE ts.therapist_id = ?1\n"
                    + "ORDER BY f.day DESC",
            nativeQuery = true)
    List<Feedback> getListFeedbackByTherapist(long therapistId);
}
