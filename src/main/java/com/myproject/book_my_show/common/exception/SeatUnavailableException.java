package com.myproject.book_my_show.common.exception;

public class SeatUnavailableException extends RuntimeException{
    public SeatUnavailableException(String message){
        super(message);
    }
}
