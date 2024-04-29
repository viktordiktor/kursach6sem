package com.nikonenko.kursach6sem.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private Long id;
    private Long userId;
    private Long recreationObjectId;
    private LocalDate bookingStartTime;
    private LocalDate bookingEndTime;
}
