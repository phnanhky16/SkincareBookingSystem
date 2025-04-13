package com.skincare_booking_system.dto.response;

import java.time.LocalTime;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SlotTimeResponse {
    LocalTime timeStart;
    LocalTime timeEnd;
    LocalTime timeBetween;
}
