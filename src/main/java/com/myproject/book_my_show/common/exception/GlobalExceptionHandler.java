package com.myproject.book_my_show.common.exception;


import com.myproject.book_my_show.common.response.ApiError;
import com.myproject.book_my_show.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler{
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex){
        Map<String, String> fields = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
        .forEach(f -> {
            System.out.println("anil: --------> " +f.getField() + ", " +f.getDefaultMessage());
            fields.put(f.getField(), f.getDefaultMessage());
        });
        return ResponseEntity.badRequest()
                .body(ApiResponse
                        .failure(
                                "Validation failed",
                                new ApiError("VALIDATION_ERROR", fields),
                                400)
                );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException ex){
        String message = "Invalid value '" + ex.getValue() + "' for parameter '" + ex.getName() + "'";
        return ResponseEntity.badRequest()
                .body(ApiResponse.fail(
                        message,
                        404
                ));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodValidation(HandlerMethodValidationException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getParameterValidationResults().forEach(result -> {
            String param = result.getMethodParameter().getParameterName();
            result.getResolvableErrors().forEach(error ->
                    errors.put(param, error.getDefaultMessage()));
        });

        return ResponseEntity.badRequest()
                .body(ApiResponse.failure("Validation failed", new ApiError("VALIDATION_ERROR", errors), 400));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingParam(MissingServletRequestParameterException ex) {
        String message = "Required parameter '" + ex.getParameterName() + "' is missing";
        return ResponseEntity.badRequest()
                .body(ApiResponse.fail(message, 400));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnreadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.fail("Malformed or invalid request body", 400));
    }

    @ExceptionHandler(InvalidOtpException.class)
    public ResponseEntity<ApiResponse<Void>> invalidOtpException(RuntimeException ex){
        return ResponseEntity.badRequest()
                .body(ApiResponse.fail(ex.getMessage(), 400));
    }

    @ExceptionHandler(SeatUnavailableException.class)
    public ResponseEntity<ApiResponse<Void>> seatUnavailableException(SeatUnavailableException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.fail(ex.getMessage(), HttpStatus.CONFLICT.value()));
    }



    @ExceptionHandler(TooManyRequestsException.class)
    public ResponseEntity<ApiResponse<Void>> tooManyRequestExceptionHandler(RuntimeException ex){
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS.value())
                .body(ApiResponse.fail(ex.getMessage(), 429));
    }


    @ExceptionHandler
    public ResponseEntity<ApiResponse<Void>> duplicateResourceException(Exception ex){
        return ResponseEntity.badRequest().body(ApiResponse.fail(ex.getMessage(), 400));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> resourceNotFoundException(Exception ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.fail(ex.getMessage(), 404));
    }
}
