package com.myproject.book_my_show.booking.dto;

import com.myproject.book_my_show.booking.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record BookingResponseDto(
        Long id,
        BookingStatus status,
        String eventTitle,
        String venueName,
        String screenName,
        LocalDateTime showStartTime,
        List<String> seats,          // ["A1", "A2"]
        BigDecimal totalAmount,
        LocalDateTime expiresAt
) {
}
