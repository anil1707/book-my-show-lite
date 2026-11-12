package com.myproject.book_my_show.common.exception;

public class TooManyRequestsException extends RuntimeException{
    public TooManyRequestsException(String message){
        super(message);
    }
}
