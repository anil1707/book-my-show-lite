package com.myproject.book_my_show.auth.controller;

import com.myproject.book_my_show.auth.dto.AuthResponseDto;
import com.myproject.book_my_show.auth.dto.OtpRequestDto;
import com.myproject.book_my_show.auth.dto.OtpVerifyDto;
import com.myproject.book_my_show.auth.service.AuthService;
import com.myproject.book_my_show.common.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("otp/request")
    public ResponseEntity<ApiResponse<Void>> requestOtp(@Valid @RequestBody OtpRequestDto request){
        authService.requestOtp(request.email());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse
                                .ok(
                                        "If the email is valid, an OTP has been sent",
                                        null,
                                        200
                                )
                );
    }

    @PostMapping("otp/verify")
    public ResponseEntity<ApiResponse<AuthResponseDto>> verifyOtp(@Valid @RequestBody OtpVerifyDto request){
        AuthResponseDto authResponseDto = authService.verifyOtp(request.email(), request.otp());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse
                                .ok(
                                        "Login successful",
                                        authResponseDto,
                                        200
                                )
                );
    }
}
