package com.myproject.book_my_show.screen.dto;

public record ScreenResponse(
        Long id,
        Long venueId,
        String venueName,
        String name,
        Integer totalSeats
) {

}
