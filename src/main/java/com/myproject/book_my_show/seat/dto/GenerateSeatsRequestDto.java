package com.myproject.book_my_show.seat.dto;

import com.myproject.book_my_show.seat.SeatType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

public record GenerateSeatsRequestDto(
        @NotNull(message = "Screen id is required")
        @Positive(message = "Screen id must be positive")
        Long screenId,

        @NotNull(message = "Rows is required")
        @Min(value = 1, message = "Rows must be at least 1")
        @Max(value = 26, message = "Rows cannot exceed 26")
        Integer rows,

        @NotNull(message = "Seats per row is required")
        @Min(value = 1, message = "Seats per row must be at least 1")
        @Max(value = 50, message = "Seats per row cannot exceed 50")
        Integer seatsPerRow,

        @NotNull(message = "Seat type is required")
        SeatType seatType
) {
}
