package com.myproject.book_my_show.booking.controller;

import com.myproject.book_my_show.booking.dto.BookingResponseDto;
import com.myproject.book_my_show.booking.dto.LockSeatsRequestDto;
import com.myproject.book_my_show.booking.service.BookingService;
import com.myproject.book_my_show.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bookings")
@SecurityRequirement(name = "bearerAuth")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService){
        this.bookingService = bookingService;
    }

    @PostMapping("/lock")
    public ResponseEntity<ApiResponse<BookingResponseDto>> lockSeats(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid LockSeatsRequestDto request
            ){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok(
                        "Seats locked. Complete payment before the lock expires",
                        bookingService.lockSeat(userId, request),
                        HttpStatus.CREATED.value())
                );
    }
}
