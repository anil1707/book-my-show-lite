package com.myproject.book_my_show.movieShow.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateShowRequestDto(
        @NotNull @Positive Long eventId,
        @NotNull @Positive Long screenId,
        @NotNull @Future LocalDateTime startTime,
        @NotNull LocalDateTime endTime,
        @NotNull @Positive BigDecimal basePrice
) {

    public boolean isEndTimeAfterStartTime(){
        return !startTime.isAfter(endTime);
    }
}
