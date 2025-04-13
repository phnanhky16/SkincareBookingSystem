package com.skincare_booking_system.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.skincare_booking_system.constant.BookingStatus;
import com.skincare_booking_system.constant.Roles;
import com.skincare_booking_system.dto.request.*;
import com.skincare_booking_system.dto.response.StaffResponse;
import com.skincare_booking_system.entities.*;
import com.skincare_booking_system.exception.AppException;
import com.skincare_booking_system.exception.ErrorCode;
import com.skincare_booking_system.mapper.StaffMapper;
import com.skincare_booking_system.repository.*;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class StaffService {
    StaffRepository staffRepository;
    StaffMapper staffMapper;
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    ServicesRepository servicesRepository;
    SlotRepository slotRepository;
    TherapistSchedulerepository therapistSchedulerepository;
    private final BookingRepository bookingRepository;
    private final VoucherRepository voucherRepository;
    private final VoucherService voucherService;

    public StaffResponse createStaff(StaffRequest request) {
        if (staffRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        if (staffRepository.existsByPhone(request.getPhone())) {
            throw new AppException(ErrorCode.PHONENUMBER_EXISTED);
        }
        if (staffRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.PHONENUMBER_EXISTED);
        }
        Staff staff = staffMapper.toStaff(request);
        staff.setPassword(passwordEncoder.encode(request.getPassword()));
        staff.setRole(Roles.STAFF);
        staff.setStatus(true);
        return staffMapper.toStaffResponse(staffRepository.save(staff));
    }

    public List<StaffResponse> getAllStaffs() {
        return staffRepository.findAll().stream()
                .map(staffMapper::toStaffResponse)
                .toList();
    }

    public StaffResponse getStaffById(Long staffId) {
        return staffRepository
                .findById(staffId)
                .map(staffMapper::toStaffResponse) // Chuyển từ Staff -> StaffResponse
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    public List<StaffResponse> getAllStaffsActive() {
        return staffRepository.findByStatusTrue().stream()
                .map(staffMapper::toStaffResponse)
                .toList();
    }

    public List<StaffResponse> getAllStaffsInactive() {
        return staffRepository.findByStatusFalse().stream()
                .map(staffMapper::toStaffResponse)
                .toList();
    }

    public StaffResponse getStaffsbyPhone(String phone) {
        return staffMapper.toStaffResponse(
                staffRepository.findByPhone(phone).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    public List<StaffResponse> searchStaffsByName(String name) {
        List<Staff> staff = staffRepository.findByFullNameContainingIgnoreCase(name);

        if (staff.isEmpty()) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        return staff.stream().map(staffMapper::toStaffResponse).toList();
    }

    public void deleteStaff(Long id) {
        Staff staff = staffRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        staff.setStatus(false);
        staffRepository.save(staff);
    }

    public void restoreStaff(Long id) {
        Staff staff = staffRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        staff.setStatus(true);
        staffRepository.save(staff);
    }

    public StaffResponse updateStaff(Long id, StaffUpdateRequest request) {
        Staff staff = staffRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        staffMapper.toUpdateStaff(staff, request);

        return staffMapper.toStaffResponse(staffRepository.save(staff));
    }

    public StaffResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        Staff staff =
                staffRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return staffMapper.toStaffResponse(staff);
    }

    public void changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Staff sta = staffRepository
                .findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (!passwordEncoder.matches(request.getOldPassword(), sta.getPassword())) {
            throw new AppException(ErrorCode.PASSWORD_WRONG);
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        sta.setPassword(passwordEncoder.encode(request.getNewPassword()));
        staffRepository.save(sta);
    }

    public void resetPassword(ResetPasswordRequest request, long id) {
        Staff staff = staffRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        staff.setPassword(passwordEncoder.encode(request.getNewPassword()));
        staffRepository.save(staff);
    }

    public StaffCreateCustomerRequest staffCreateCustomer(StaffCreateCustomerRequest request) {
        User user = userRepository.findUserByPhone(request.getPhone());
        if (user != null) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User newAccount = new User();
        newAccount.setPhone(request.getPhone());
        newAccount.setFirstName(request.getFirstName());
        newAccount.setLastName(request.getLastName());
        newAccount.setUsername(request.getPhone());
        newAccount.setPassword(passwordEncoder.encode(request.getPhone()));
        newAccount.setStatus(true);
        newAccount.setBirthDate(request.getBirthDate());
        newAccount.setGender(request.getGender());
        newAccount.setRole(Roles.CUSTOMER);
        userRepository.save(newAccount);
        return request;
    }

    public StaffCreateBookingRequest createBookingByStaff(StaffCreateBookingRequest request) {
        User account = userRepository.findUserByPhone(request.getPhoneNumber());
        if (account == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        Set<Services> serviceSet = new HashSet<>();
        for (Long id : request.getServiceId()) {
            Services service = servicesRepository.getServiceById(id);
            serviceSet.add(service);
        }
        Slot slot = slotRepository.findSlotBySlotid(request.getSlotId());
        if (slot == null) {
            throw new AppException(ErrorCode.SLOT_NOT_FOUND);
        }
        TherapistSchedule therapistSchedule =
                therapistSchedulerepository.getTherapistScheduleId(request.getTherapistId(), request.getBookingDate());
        if (therapistSchedule == null) {
            throw new AppException(ErrorCode.THERAPIST_UNAVAILABLE);
        }

        Voucher voucher = voucherRepository.findVoucherByVoucherId(request.getVoucherId());
        if (voucher != null) {
            voucherService.useVoucher(voucher.getVoucherCode());
        }
        Booking booking = new Booking();
        booking.setServices(serviceSet);
        booking.setBookingDay(request.getBookingDate());
        booking.setStatus(BookingStatus.PENDING);
        booking.setUser(account);
        booking.setSlot(slot);
        booking.setTherapistSchedule(therapistSchedule);
        booking.setTherapist(therapistSchedule.getTherapist());
        booking.setVoucher(voucher);
        Booking newBooking = bookingRepository.save(booking);
        for (Services service : serviceSet) {
            bookingRepository.updateBookingDetail(
                    service.getPrice(), newBooking.getBookingId(), service.getServiceId());
        }
        return request;
    }
}
