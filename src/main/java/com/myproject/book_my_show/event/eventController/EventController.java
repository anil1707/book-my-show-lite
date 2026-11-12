package com.myproject.book_my_show.event.eventController;

import com.myproject.book_my_show.common.response.ApiResponse;
import com.myproject.book_my_show.event.dto.EventRequestDto;
import com.myproject.book_my_show.event.dto.EventResponseDto;
import com.myproject.book_my_show.event.service.EventService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {
    private EventService eventService;

    public EventController(EventService eventService){
        this.eventService = eventService;
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<EventResponseDto>>> getEvents(){

        List<EventResponseDto> response = eventService.findAllEvents();

        return ResponseEntity.ok(ApiResponse.ok("Data fetched successfully", response, 200));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<EventResponseDto>> findEventById(
            @PathVariable
            @Positive(message = "Event id must be positive")
            long id
    ){
        EventResponseDto response = eventService.findById(id);
        return ResponseEntity.ok(ApiResponse.ok("Data fetched successfully", response, 200 ));
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<ApiResponse<EventResponseDto>> createEvent(
            @Valid @RequestBody EventRequestDto request
            ){
        EventResponseDto response = eventService.addEvent(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(response, 201));
    }
}
