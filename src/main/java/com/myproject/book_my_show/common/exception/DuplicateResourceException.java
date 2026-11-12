package com.myproject.book_my_show.common.exception;

public class DuplicateResourceException extends RuntimeException{
    public DuplicateResourceException(String message){
        super(message);
    }
}
