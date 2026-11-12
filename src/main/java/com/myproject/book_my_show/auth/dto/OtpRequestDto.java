package com.myproject.book_my_show.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record OtpRequestDto(
        @NotBlank @Email @Size(max = 150) String email
) {
}
