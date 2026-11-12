package com.myproject.book_my_show.venue.dto;

import java.time.LocalDateTime;

public record VenueResponse(
        Long id, String name,
        String city,
        String address,
        LocalDateTime createdAt
) {
}
