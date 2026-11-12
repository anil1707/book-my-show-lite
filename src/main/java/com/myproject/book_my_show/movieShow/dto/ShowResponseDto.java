package com.myproject.book_my_show.movieShow.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ShowResponseDto(
//        Long id,
//        Long eventId,
//        String eventTitle,
//        Long screenId,
//        String screenName,
//        LocalDateTime startTime,
//        LocalDateTime endTime,
//        BigDecimal basePrice
        Long id,
        Long eventId,
        String eventTitle,
        Long venueId,
        String venueName,
        String city,
        Long screenId,
        String screenName,
        LocalDateTime startTime,
        LocalDateTime endTime,
        BigDecimal basePrice
) {
}