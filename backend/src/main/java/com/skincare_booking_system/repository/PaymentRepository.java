package com.skincare_booking_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.skincare_booking_system.entities.Booking;
import com.skincare_booking_system.entities.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT p FROM Payment p WHERE p.booking = :booking")
    Payment findPaymentByBooking(@Param("booking") Booking booking);

    Payment findByTransactionId(String transactionId);

    Payment findByBooking_BookingId(Long booking);
}
