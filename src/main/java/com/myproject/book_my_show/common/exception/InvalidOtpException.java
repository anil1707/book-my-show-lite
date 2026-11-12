package com.myproject.book_my_show.common.exception;

public class InvalidOtpException extends RuntimeException{
    public InvalidOtpException(String message){
        super(message);
    }
}
