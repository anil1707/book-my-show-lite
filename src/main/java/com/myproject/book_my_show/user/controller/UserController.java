package com.myproject.book_my_show.user.controller;

import com.myproject.book_my_show.common.response.ApiResponse;
import com.myproject.book_my_show.user.dto.UpdateProfileRequestDto;
import com.myproject.book_my_show.user.dto.UserResponseDto;
import com.myproject.book_my_show.user.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> findById(@PathVariable @Valid Long id){
        return ResponseEntity
                .ok(
                        ApiResponse
                                .ok(
                                        "Date fetched successfully",
                                        userService.findById(id),
                                        200
                                )
                );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(
            @PathVariable @Valid Long id,
            @RequestBody @Valid UpdateProfileRequestDto request
    ){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse
                        .ok(
                                "updated successfully",
                                userService.updateProfile(id, request),
                                HttpStatus.CREATED.value()
                        )
                );
    }
}
