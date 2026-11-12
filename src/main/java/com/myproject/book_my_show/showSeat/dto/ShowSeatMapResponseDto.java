package com.myproject.book_my_show.showSeat.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ShowSeatMapResponseDto(
        Long showId,
        String eventTitle,
        String screenName,
        LocalDateTime startTime,
        List<ShowSeatResponseDto> seats
) {
}
