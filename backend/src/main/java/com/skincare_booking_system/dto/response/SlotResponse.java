package com.skincare_booking_system.dto.response;

import java.time.LocalTime;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class SlotResponse {
    long slotid;
    LocalTime slottime;
}
