package com.skincare_booking_system.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.skincare_booking_system.dto.request.UpdateSlotRequest;
import com.skincare_booking_system.dto.response.SlotResponse;
import com.skincare_booking_system.dto.response.SlotTimeResponse;
import com.skincare_booking_system.entities.Shift;
import com.skincare_booking_system.entities.Slot;
import com.skincare_booking_system.exception.AppException;
import com.skincare_booking_system.exception.ErrorCode;
import com.skincare_booking_system.repository.ShiftRepository;
import com.skincare_booking_system.repository.SlotRepository;

@Service
public class SlotService {
    private final SlotRepository slotRepository;
    private final ShiftRepository shiftRepository;

    public SlotService(SlotRepository slotRepository, ShiftRepository shiftRepository) {
        this.slotRepository = slotRepository;
        this.shiftRepository = shiftRepository;
    }

    public SlotResponse createSlot(Slot slot) {
        if (slotRepository.existsBySlottime(slot.getSlottime())) {
            throw new AppException(ErrorCode.SLOT_TIME_ALREADY_EXISTS);
        }
        slotRepository.save(slot);
        return SlotResponse.builder()
                .slotid(slot.getSlotid())
                .slottime(slot.getSlottime())
                .build();
    }

    public List<Slot> getAllSlot() {
        List<Slot> slots = slotRepository.getAllSlotActive();
        return slots;
    }

    // lay nhung slot chua qua gio hien tai
    public List<Slot> getAllSlotValid(LocalDate date) {
        List<Slot> slots = slotRepository.getAllSlotActive();
        if (!LocalDate.now().isEqual(date)) {
            return slots;
        }

        LocalTime now = LocalTime.now();
        return slots.stream().filter(slot -> !now.isAfter(slot.getSlottime())).collect(Collectors.toList());
    }

    public Slot update(long slotid, Slot slot) {
        Slot updeSlot = slotRepository.findById(slotid).orElseThrow(() -> new AppException(ErrorCode.SLOT_NOT_FOUND));
        boolean exists = slotRepository.existsBySlottime(slot.getSlottime());
        if (exists) {
            throw new AppException(ErrorCode.SLOT_TIME_ALREADY_EXISTS);
        }
        updeSlot.setSlottime(slot.getSlottime());
        return slotRepository.save(updeSlot);
    }

    public Slot delete(long slotid) {
        Slot slot = slotRepository.findById(slotid).orElseThrow(() -> new AppException(ErrorCode.SLOT_NOT_FOUND));

        slot.setDeleted(true);
        return slotRepository.save(slot);
    }

    public List<SlotResponse> updateSlotTime(UpdateSlotRequest request) {
        List<Slot> slotsWithoutMinute = slotRepository.getSlotsWithoutMinute();
        List<Slot> newListSlot = new ArrayList<>();
        Set<LocalTime> newTimeSet = new HashSet<>();

        long slotBeginId = 1;
        Slot slotBegin = slotRepository.findSlotBySlotid(slotBeginId);
        if (slotBegin != null) {
            newListSlot.add(slotBegin);
            newTimeSet.add(slotBegin.getSlottime());
        }

        Shift shift = shiftRepository.getLatestShift();

        for (Slot slot : slotsWithoutMinute) {
            LocalTime updatedTime = slot.getSlottime()
                    .plusHours(request.getTime().getHour())
                    .plusMinutes(request.getTime().getMinute());

            if (updatedTime.isAfter(shift.getEndTime())) {
                break;
            }

            Slot newSlot = new Slot();
            newSlot.setSlottime(updatedTime);
            newSlot.setDeleted(false);
            newListSlot.add(newSlot);
            newTimeSet.add(updatedTime);

            LocalTime nextSlotTime = updatedTime.plusMinutes(request.getTime().getMinute());
            while (nextSlotTime.isBefore(shift.getEndTime()) || nextSlotTime.equals(shift.getEndTime())) {
                Slot additionalSlot = new Slot();
                additionalSlot.setSlottime(nextSlotTime);
                additionalSlot.setDeleted(false);
                newListSlot.add(additionalSlot);
                newTimeSet.add(nextSlotTime);

                nextSlotTime = nextSlotTime.plusMinutes(request.getTime().getMinute());
            }
        }

        List<SlotResponse> responses = new ArrayList<>();
        for (Slot slot : newListSlot) {
            Slot existingSlot = slotRepository.findBySlottime(slot.getSlottime());
            if (existingSlot == null) {
                Slot savedSlot = slotRepository.save(slot);
                responses.add(new SlotResponse(savedSlot.getSlotid(), savedSlot.getSlottime()));
            } else {
                existingSlot.setDeleted(false);
                slotRepository.save(existingSlot);
                responses.add(new SlotResponse(existingSlot.getSlotid(), existingSlot.getSlottime()));
            }
        }

        List<Slot> allSlots = slotRepository.findAll();
        for (Slot slot : allSlots) {
            if (!newTimeSet.contains(slot.getSlottime())) {
                slot.setDeleted(true);
                slotRepository.save(slot);
            }
        }

        return responses;
    }

    public SlotTimeResponse getSlotTimeBetween() {
        Slot slotBegin = slotRepository.slotBeginActive();
        Slot slotAfterBegin = slotRepository.slotAfterBeginActive(slotBegin.getSlottime());
        Slot slotEnd = slotRepository.slotEndActive();
        LocalTime timeBetweenTwoSlot = slotAfterBegin
                .getSlottime()
                .minusHours(slotBegin.getSlottime().getHour())
                .minusMinutes(slotBegin.getSlottime().getMinute());
        SlotTimeResponse response = new SlotTimeResponse();
        response.setTimeStart(slotBegin.getSlottime());
        response.setTimeEnd(slotEnd.getSlottime());
        response.setTimeBetween(timeBetweenTwoSlot);
        return response;
    }
}
