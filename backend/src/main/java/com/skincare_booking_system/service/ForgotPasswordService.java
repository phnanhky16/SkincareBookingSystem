package com.skincare_booking_system.service;

import java.time.Instant;
import java.util.Date;
import java.util.Random;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.skincare_booking_system.dto.request.ForgotPasswordRequest;
import com.skincare_booking_system.dto.request.MailBody;
import com.skincare_booking_system.dto.response.ForgotPasswordResponse;
import com.skincare_booking_system.entities.ForgotPassword;
import com.skincare_booking_system.entities.User;
import com.skincare_booking_system.exception.AppException;
import com.skincare_booking_system.exception.ErrorCode;
import com.skincare_booking_system.repository.ForgotPasswordRepository;
import com.skincare_booking_system.repository.UserRepository;

@Service
public class ForgotPasswordService {
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public ForgotPasswordService(
            JavaMailSender mailSender,
            UserRepository userRepository,
            ForgotPasswordRepository forgotPasswordRepository,
            EmailService emailService,
            PasswordEncoder passwordEncoder) {
        this.mailSender = mailSender;
        this.userRepository = userRepository;
        this.forgotPasswordRepository = forgotPasswordRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    public ForgotPasswordResponse verifyEmail(String email) {
        User user =
                userRepository.findUserByEmail(email).orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_EXISTED));
        ForgotPassword checkUser = forgotPasswordRepository.findForgotPasswordByUser(user);
        if (checkUser != null) {
            forgotPasswordRepository.deleteById(checkUser.getFpId());
        }
        int otp = optGenerate();
        MailBody mailBody = MailBody.builder()
                .to(email)
                .subject("OTP for your forgot password request:")
                .otp(otp)
                .build();
        ForgotPassword fp = ForgotPassword.builder()
                .otp(otp)
                .expirationTime(new Date(System.currentTimeMillis() + 70 * 1000))
                .user(user)
                .build();
        emailService.sendSimpleMessage(mailBody);
        forgotPasswordRepository.save(fp);
        return ForgotPasswordResponse.builder().message("Email sent verify!").build();
    }

    public ForgotPasswordResponse verifyOTP(Integer otp, String email) {
        User user =
                userRepository.findUserByEmail(email).orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_EXISTED));
        ForgotPassword fp = forgotPasswordRepository.findForgotPasswordByOtpAndUser(otp, user);
        if (fp == null) {
            throw new AppException(ErrorCode.INVALID_OTP);
        }
        if (fp.getExpirationTime().before(Date.from(Instant.now()))) {
            throw new AppException(ErrorCode.OTP_HAS_EXPIRED);
        }
        return ForgotPasswordResponse.builder().message("OTP is verified").build();
    }

    public ForgotPasswordResponse changeForgotPassword(String email, ForgotPasswordRequest request) {
        if (!request.getPassword().equals(request.getRepassword())) {
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        }
        User user =
                userRepository.findUserByEmail(email).orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_EXISTED));

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        ForgotPassword fp = forgotPasswordRepository.findForgotPasswordByUser(user);
        forgotPasswordRepository.deleteById(fp.getFpId());
        System.out.println("Deleting ForgotPassword with ID: " + fp.getFpId());
        return ForgotPasswordResponse.builder()
                .message("Change password successfully")
                .build();
    }

    private Integer optGenerate() {
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }
}
