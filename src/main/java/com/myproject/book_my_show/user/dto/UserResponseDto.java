package com.myproject.book_my_show.user.dto;

import com.myproject.book_my_show.user.enums.Role;

import java.time.LocalDateTime;

public record UserResponseDto(
        Long id,
        String name,
        String email,
        Role role,
        LocalDateTime createdAt
) {
}
