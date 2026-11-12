package com.myproject.book_my_show.showSeat.dto;

import com.myproject.book_my_show.seat.SeatType;
import com.myproject.book_my_show.showSeat.enums.ShowSeatStatus;

import java.math.BigDecimal;

public record ShowSeatResponseDto(
        Long id,
        Long seatId,
        String rowLabel,
        Integer seatNumber,
        SeatType seatType,
        ShowSeatStatus status,
        BigDecimal price
) {
}
