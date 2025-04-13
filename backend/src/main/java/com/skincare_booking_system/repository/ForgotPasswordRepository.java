package com.skincare_booking_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skincare_booking_system.entities.ForgotPassword;
import com.skincare_booking_system.entities.User;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Integer> {
    ForgotPassword findForgotPasswordByOtpAndUser(Integer otp, User user);

    ForgotPassword findForgotPasswordByUser(User user);
}
