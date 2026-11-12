package com.myproject.book_my_show.event.dto;

import java.time.LocalDateTime;

public record EventResponseDto(
        Long id,
        String title,
        String description,
        String category,
        Integer durationMin,
        String language,
        LocalDateTime createdAt
){ }
