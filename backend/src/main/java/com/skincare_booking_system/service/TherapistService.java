package com.skincare_booking_system.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.skincare_booking_system.constant.Roles;
import com.skincare_booking_system.dto.request.ChangePasswordRequest;
import com.skincare_booking_system.dto.request.ResetPasswordRequest;
import com.skincare_booking_system.dto.response.*;
import com.skincare_booking_system.entities.Booking;
import com.skincare_booking_system.entities.Therapist;
import com.skincare_booking_system.exception.AppException;
import com.skincare_booking_system.exception.ErrorCode;
import com.skincare_booking_system.mapper.TherapistMapper;
import com.skincare_booking_system.repository.BookingRepository;
import com.skincare_booking_system.repository.ServicesRepository;
import com.skincare_booking_system.repository.TherapistRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TherapistService {
    @Autowired
    private TherapistRepository therapistRepository;

    @Autowired
    private TherapistMapper therapistMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ServicesRepository servicesRepository;

    @Autowired
    private ImagesService imagesService;

    public TherapistResponse createTherapist(
            String username,
            String password,
            String fullName,
            String email,
            String phone,
            String address,
            String gender,
            LocalDate birthDate,
            Integer yearExperience,
            MultipartFile imgUrl)
            throws IOException {
        if (therapistRepository.existsByUsername(username)) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        if (therapistRepository.existsByPhone(phone)) {
            throw new AppException(ErrorCode.PHONENUMBER_EXISTED);
        }
        if (therapistRepository.existsByEmail(email)) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        String imageUrl = (imgUrl != null && !imgUrl.isEmpty()) ? imagesService.uploadImage(imgUrl) : null;
        Therapist therapist = Therapist.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .fullName(fullName)
                .email(email)
                .phone(phone)
                .address(address)
                .gender(gender)
                .birthDate(birthDate)
                .yearExperience(yearExperience)
                .status(true)
                .role(Roles.THERAPIST)
                .imgUrl(imageUrl)
                .build();
        therapistRepository.save(therapist);
        return therapistMapper.toTherapistResponse(therapist);
    }

    public List<TherapistResponse> getAllTherapists() {
        return therapistRepository.findAll().stream()
                .map(therapistMapper::toTherapistResponse)
                .toList();
    }

    public List<TherapistResponse> getAllTherapistsActive() {
        return therapistRepository.findByStatusTrue().stream()
                .map(therapistMapper::toTherapistResponse)
                .toList();
    }

    public List<TherapistResponse> getAllTherapistsInactive() {
        return therapistRepository.findByStatusFalse().stream()
                .map(therapistMapper::toTherapistResponse)
                .toList();
    }

    public TherapistResponse getTherapistbyId(Long id) {
        return therapistMapper.toTherapistResponse(therapistRepository
                .findTherapistById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    public List<TherapistResponse> searchTherapistsByName(String name) {
        List<Therapist> therapists = therapistRepository.findByFullNameContainingIgnoreCase(name);

        if (therapists.isEmpty()) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        return therapists.stream().map(therapistMapper::toTherapistResponse).toList();
    }

    public void deleteTherapistbyId(Long id) {
        Therapist therapist = therapistRepository
                .findTherapistById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        therapist.setStatus(false);
        therapistRepository.save(therapist);
    }

    public void restoreTherapistById(Long id) {
        Therapist therapist = therapistRepository
                .findTherapistById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        therapist.setStatus(true);
        therapistRepository.save(therapist);
    }

    public InfoTherapistResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        Therapist therapist = therapistRepository
                .findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return therapistMapper.toInfoTherapist(therapist);
    }

    public TherapistResponse updateTherapist(
            Long id,
            String fullName,
            String email,
            String phone,
            String address,
            String gender,
            LocalDate birthDate,
            Integer yearExperience,
            MultipartFile imgUrl)
            throws IOException {
        Therapist therapist = therapistRepository
                .findTherapistById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        String imageUrl = therapist.getImgUrl(); // Giữ ảnh cũ nếu không upload mới
        if (imgUrl != null && !imgUrl.isEmpty()) {
            imageUrl = imagesService.uploadImage(imgUrl);
        }

        therapist.setFullName(fullName);
        therapist.setEmail(email);
        therapist.setPhone(phone);
        therapist.setAddress(address);
        therapist.setGender(gender);
        therapist.setBirthDate(birthDate);
        therapist.setYearExperience(yearExperience);
        therapist.setImgUrl(imageUrl);

        therapistRepository.save(therapist);
        return therapistMapper.toTherapistResponse(therapist);
    }

    public double calculateAverageFeedback(Long therapistId, String yearAndMonth) {
        String[] parts = yearAndMonth.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);

        List<Booking> bookings = bookingRepository.findBookingByTherapistIdAndMonthYear(therapistId, month, year);
        // Tính tổng điểm feedback và đếm số lượng feedback
        double totalFeedbackScore = bookings.stream()
                .filter(booking -> booking.getFeedback() != null) // Chỉ tính booking có feedback
                .mapToDouble(booking -> booking.getFeedback().getScore()) // Lấy điểm từ feedback
                .sum();

        long feedbackCount = bookings.stream()
                .filter(booking -> booking.getFeedback() != null) // Chỉ tính booking có feedback
                .count();
        double averageFeedbackScore = feedbackCount > 0 ? totalFeedbackScore / feedbackCount : 0.0;

        log.info(
                "Therapist ID: {}, Total Feedback Score: {}, Average Feedback Score: {}",
                therapistId,
                totalFeedbackScore,
                averageFeedbackScore);

        return averageFeedbackScore;
    }

    public List<BookingResponse> getBookingsForTherapistOnDate(Long therapistId, LocalDate date) {
        List<Booking> bookings = bookingRepository.findAllByTherapistAndDate(therapistId, date);
        // Chuyển đổi lúc trả ra từ Booking sang BookingResponse
        List<BookingResponse> responses = new ArrayList<>();

        for (Booking booking : bookings) {
            Set<Long> serviceId = servicesRepository.getServiceIdByBooking(booking.getBookingId());

            BookingResponse bookingResponse = new BookingResponse();
            bookingResponse.setId(booking.getBookingId());
            bookingResponse.setTherapistId(therapistId);
            bookingResponse.setTime(booking.getSlot().getSlottime());
            bookingResponse.setDate(booking.getBookingDay());
            bookingResponse.setServiceId(serviceId);
            bookingResponse.setStatus(booking.getStatus());
            bookingResponse.setUserId(booking.getUser().getId());
            if (booking.getVoucher() != null) {
                bookingResponse.setVoucherId(booking.getVoucher().getVoucherId());
            }
            responses.add(bookingResponse);
        }
        return responses;
    }

    public void changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Therapist the = therapistRepository
                .findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (!passwordEncoder.matches(request.getOldPassword(), the.getPassword())) {
            throw new AppException(ErrorCode.PASSWORD_WRONG);
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        the.setPassword(passwordEncoder.encode(request.getNewPassword()));
        therapistRepository.save(the);
    }

    public void resetPassword(ResetPasswordRequest request, Long id) {
        Therapist the = therapistRepository
                .findTherapistById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        the.setPassword(passwordEncoder.encode(request.getNewPassword()));
        therapistRepository.save(the);
    }

    private int countBooking(Long therapistId, String yearAndMonth) {
        // Tách tháng và năm từ yearAndMonth
        String[] parts = yearAndMonth.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);

        // Gọi hàm để lấy danh sách booking
        List<Booking> bookings = bookingRepository.findBookingByTherapistIdAndMonthYear(therapistId, month, year);

        int sizeBookings = bookings.size();

        log.info("Total bookings: {}", sizeBookings);
        return sizeBookings;
    }

    private double calculateTotalRevenue(Long therapistId, String yearAndMonth) {
        String[] parts = yearAndMonth.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);

        List<Booking> bookings = bookingRepository.findBookingByTherapistIdAndMonthYear(therapistId, month, year);
        log.info("Bookings for therapist ID {} in month {} of year {}: {}", therapistId, month, year, bookings);
        log.info("Number of bookings for therapist ID {}: {}", therapistId, bookings.size());

        double totalPayment = bookings.stream()
                .filter(booking -> booking.getPayment() != null
                        && booking.getPayment().getPaymentStatus().equals("Completed"))
                .mapToDouble(booking -> booking.getPayment().getPaymentAmount())
                .sum();

        log.info("Total payment: {}", totalPayment);
        return totalPayment;
    }

    public TherapistRevenueResponse getTherapistRevenue(long therapistId, String yearAndMonth) {
        String[] parts = yearAndMonth.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        double bonusPercent = 0;

        double totalRevenue = calculateTotalRevenue(therapistId, yearAndMonth);
        int sizeBookings = countBooking(therapistId, yearAndMonth);

        // Lấy thông tin về therapist
        Therapist therapist = therapistRepository.findTherapistsById(therapistId);
        if (therapist == null) { // Kiểm tra nếu therapist không tồn tại
            throw new AppException(ErrorCode.THERAPIST_NOT_FOUND);
        }

        String therapistName = therapist.getFullName();
        // Tạo đối tượng TherapistRevenueResponse
        return TherapistRevenueResponse.builder()
                .therapistId(therapistId)
                .therapistName(therapistName)
                .bookingQuantity(sizeBookings) // Đảm bảo bookingQuantity được định nghĩa trong TherapistRevenueResponse
                .totalRevenue(totalRevenue)
                .build();
    }
}
