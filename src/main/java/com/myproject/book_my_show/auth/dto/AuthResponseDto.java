package com.myproject.book_my_show.auth.dto;

import com.myproject.book_my_show.user.dto.UserResponseDto;

public record AuthResponseDto(
        String accessToken,
        String tokenType,
        long expiresIn,
        UserResponseDto user
) {
}
