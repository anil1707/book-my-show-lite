package com.myproject.book_my_show.seat.dto;

import com.myproject.book_my_show.seat.SeatType;

public record SeatResponseDto(
        Long id,
        Long screenId,
        String rowLabel,
        Integer seatNumber,
        SeatType seatType
) {
}
