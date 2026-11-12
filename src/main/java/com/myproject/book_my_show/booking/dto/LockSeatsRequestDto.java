package com.myproject.book_my_show.booking.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record LockSeatsRequestDto(
        @NotNull @Positive Long showId,
        @NotEmpty @Size(max = 10, message = "You can book at most 10 seats at once")
        List<@NotNull @Positive Long> showSeatIds
) {
}
