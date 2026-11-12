package com.myproject.book_my_show.screen.dto;

import jakarta.validation.constraints.*;

public record CreateScreenRequest(
        @NotNull
        @Positive
        Long venueId,

        @NotBlank
        @Size(max = 50)
        String name,

        @NotNull
        @Min(1)
        Integer totalSeats
) {
}
