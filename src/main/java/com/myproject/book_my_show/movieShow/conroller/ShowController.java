package com.myproject.book_my_show.movieShow.conroller;

import com.myproject.book_my_show.common.response.ApiResponse;
import com.myproject.book_my_show.movieShow.dto.CreateShowRequestDto;
import com.myproject.book_my_show.movieShow.dto.ShowResponseDto;
import com.myproject.book_my_show.movieShow.repository.ShowRepository;
import com.myproject.book_my_show.movieShow.service.ShowService;
import com.myproject.book_my_show.showSeat.dto.ShowSeatMapResponseDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shows")
public class ShowController {
    private final ShowService showService;
    public ShowController(ShowService showService){
        this.showService = showService;
    }
//    1. add show
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<ApiResponse<ShowResponseDto>> addShow(
            @RequestBody @Valid CreateShowRequestDto request
            ){
        ShowResponseDto response = showService.createShow(request);

        return ResponseEntity
                .ok(ApiResponse
                        .ok("Show created successfully", response, 200)
                );
    }

//    2. list of show for an event
    @GetMapping
    public ResponseEntity<ApiResponse<List<ShowResponseDto>>> findShowsByEvent(
            @RequestParam("event-id")
            @Valid
            long eventId,
            @RequestParam(value = "city", required = false) String city

    ){
        return ResponseEntity
                .ok(ApiResponse
                        .ok(
                                "data fetched successfully",
                                showService.findShowListByEventId(eventId, city),
                                200
                        )
                );
    }

//    3. show data by show id
    @GetMapping("{id}/seats")
    public ResponseEntity<ApiResponse<ShowSeatMapResponseDto>> findShowById(
            @PathVariable("id")
            @Valid
            Long id
    ){

        ShowSeatMapResponseDto responseDto = showService.showById(id);
        return ResponseEntity
                .ok(
                        ApiResponse
                                .ok(
                                        "Data fetched successfully",
                                        responseDto,
                                        200
                                )
                );

    }
}
