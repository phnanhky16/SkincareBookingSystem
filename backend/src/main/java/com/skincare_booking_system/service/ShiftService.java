package com.skincare_booking_system.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.skincare_booking_system.dto.request.ShiftRequest;
import com.skincare_booking_system.dto.request.UpdateShiftRequest;
import com.skincare_booking_system.dto.response.ShiftResponse;
import com.skincare_booking_system.entities.Shift;
import com.skincare_booking_system.exception.AppException;
import com.skincare_booking_system.exception.ErrorCode;
import com.skincare_booking_system.repository.ShiftRepository;

@Service
public class ShiftService {

    private final ShiftRepository shiftRepository;

    public ShiftService(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }

    public ShiftResponse createShift(ShiftRequest request) {
        if (shiftRepository
                .findByStartTimeAndEndTime(request.getStartTime(), request.getEndTime())
                .isPresent()) {
            throw new AppException(ErrorCode.SHIFT_EXIST);
        }
        Shift shift = Shift.builder()
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .limitBooking(request.getLimitBooking())
                .build();

        shiftRepository.save(shift);
        return ShiftResponse.builder()
                .shiftId(shift.getShiftId())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .limitBooking(request.getLimitBooking())
                .build();
    }

    public List<ShiftResponse> getAllShifts() {
        List<Shift> shifts = shiftRepository.findAll();
        if (shifts.isEmpty()) {
            throw new AppException(ErrorCode.SHIFT_NOT_EXIST);
        }
        return shifts.stream().map(this::convertToShiftResponse).collect(Collectors.toList());
    }

    public ShiftResponse updateShift(long id, UpdateShiftRequest request) {
        Shift shift = shiftRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.SHIFT_NOT_EXIST));

        if (request.getStartTime() != null) {
            shift.setStartTime(request.getStartTime());
        }

        if (request.getEndTime() != null) {
            shift.setEndTime(request.getEndTime());
        }

        if (request.getLimitBooking() != null) {
            shift.setLimitBooking(request.getLimitBooking());
        }

        shiftRepository.save(shift);
        ShiftResponse shiftResponse = convertToShiftResponse(shift);
        return shiftResponse;
    }

    public void deleteShift(long id) {
        Shift shift = shiftRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.SHIFT_NOT_EXIST));

        shiftRepository.delete(shift);
    }

    private ShiftResponse convertToShiftResponse(Shift shift) {
        return new ShiftResponse(shift.getShiftId(), shift.getStartTime(), shift.getEndTime(), shift.getLimitBooking());
    }
}
