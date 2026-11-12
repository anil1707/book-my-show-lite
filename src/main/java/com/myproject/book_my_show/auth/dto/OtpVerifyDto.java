package com.myproject.book_my_show.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record OtpVerifyDto(
        @NotBlank @Email String email,
        @NotBlank @Pattern(regexp = "\\d{6}", message = "OTP must be 6 digits") String otp
) {
}
