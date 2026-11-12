package com.myproject.book_my_show.venue.controller;

import com.myproject.book_my_show.common.response.ApiResponse;
import com.myproject.book_my_show.venue.Service.VenueService;
import com.myproject.book_my_show.venue.dto.CreateVenueRequest;
import com.myproject.book_my_show.venue.dto.VenueResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("api/v1/venue")
public class VenueController {
    private final VenueService venueService;
    public VenueController(VenueService venueService){
        this.venueService = venueService;
    }
    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<VenueResponse>> addVenue(@RequestBody @Valid CreateVenueRequest request){
        VenueResponse response = venueService.addVenue(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Venue added successfully", response, 201));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<VenueResponse>> findVenueById(@PathVariable @Valid Long id){
        VenueResponse response = venueService.venueById(id);
        return ResponseEntity.
                ok(ApiResponse.ok("Venue fetched successfully", response, 200));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<VenueResponse>>> findAllVenue(){
        List<VenueResponse> response = venueService.findAllVenue();
        return ResponseEntity.ok(
                ApiResponse.ok("Venue fetched successfully", response, 200)
        );
    }
}
