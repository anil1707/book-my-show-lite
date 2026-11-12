package com.myproject.book_my_show.seat.controller;

import com.myproject.book_my_show.common.response.ApiResponse;
import com.myproject.book_my_show.seat.dto.GenerateSeatsRequestDto;
import com.myproject.book_my_show.seat.dto.SeatResponseDto;
import com.myproject.book_my_show.seat.service.SeatService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/seat")
public class SeatController {
    private final SeatService seatService;
    public SeatController(SeatService seatService){
        this.seatService = seatService;
    }
    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<List<SeatResponseDto>>> generateSeat(@RequestBody @Valid GenerateSeatsRequestDto request){
        List<SeatResponseDto> response = seatService.generateSeat(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Seat generated successfully", response, 201));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SeatResponseDto>>> getSeatByScreenId(
            @RequestParam("screen-id")
            @Valid
            Long screenId
    ){
        return ResponseEntity
                .ok(
                        ApiResponse.ok(
                                "Data fetched successfully",
                                seatService.seatByScreenId(screenId),
                                200
                        )
                );
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<SeatResponseDto>> getSeatById(
            @PathVariable
            @Valid
            Long id
    ){
        return ResponseEntity
                .ok(
                        ApiResponse.ok(
                                "Data fetched successfully",
                                seatService.seatById(id),
                                200
                        )
                );
    }
}
