package com.myproject.book_my_show.common.response;





import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        boolean success,
        Integer status,
        String message,
        T data,
        ApiError error,
        Instant timestamp
) {
    public static <T> ApiResponse<T> ok(T data, Integer status) {
        return new ApiResponse<>(true, status, null, data, null, Instant.now());
    }

    public static <T> ApiResponse<T> ok(String message, T data, Integer status) {
        return new ApiResponse<>(true, status, message, data, null, Instant.now());
    }

    public static <T> ApiResponse<T> fail(String message, Integer status){
        return  new ApiResponse<>(false, status, message, null, null, Instant.now());
    }

    public static <T> ApiResponse<T> failure(String message, ApiError error, Integer status) {
        return new ApiResponse<>(false, status, message, null, error, Instant.now());
    }
}
