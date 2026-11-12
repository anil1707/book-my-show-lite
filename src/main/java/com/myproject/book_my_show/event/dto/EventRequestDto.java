package com.myproject.book_my_show.event.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EventRequestDto(
    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must be at most 200 characters")
    String title,

    String description,

    @NotBlank(message = "Category is required")
    @Size(max = 50)
    String category,

    @Min(value = 1, message = "Duration must be positive")
    Integer durationMin,

    @Size(max = 50)
    String language
)
{}


