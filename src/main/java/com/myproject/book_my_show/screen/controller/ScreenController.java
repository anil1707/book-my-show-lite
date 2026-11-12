package com.myproject.book_my_show.screen.controller;

import com.myproject.book_my_show.common.response.ApiResponse;
import com.myproject.book_my_show.screen.dto.CreateScreenRequest;
import com.myproject.book_my_show.screen.dto.ScreenResponse;
import com.myproject.book_my_show.screen.service.ScreenService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("api/v1/screens")
public class ScreenController {
    private final ScreenService screenService;

    public ScreenController(ScreenService screenService){
        this.screenService = screenService;
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<ApiResponse<ScreenResponse>> addScreen(@RequestBody @Valid CreateScreenRequest request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok(
                        "Screen created successfully",
                        screenService.addScreen(request),
                        201)
                );
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<ScreenResponse>> findScreenById(@PathVariable @Valid Long id) {
        return ResponseEntity
                .ok(
                        ApiResponse.ok(
                                "Data fetched successfully",
                                screenService.findById(id),
                                2000)
                );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ScreenResponse>>> findScreenByVenueId(@RequestParam("venue-id") @Positive Long id){
        return ResponseEntity
                .ok(
                        ApiResponse.ok(
                                "Data fetched successfully",
                                screenService.findScreensByVenueId(id),
                                200
                        )
                );
    }


}
