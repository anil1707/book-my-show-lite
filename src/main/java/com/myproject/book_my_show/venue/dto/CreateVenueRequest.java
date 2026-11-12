package com.myproject.book_my_show.venue.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateVenueRequest(
        @NotBlank(message = "Name is required") @Size(max = 150) String name,
        @NotBlank(message = "City is required") @Size(max = 100) String city,
        @Size(max = 255) String address
) {}
