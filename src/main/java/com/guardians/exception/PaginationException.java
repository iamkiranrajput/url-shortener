package com.guardians.exception;

public class PaginationException extends RuntimeException{
    public PaginationException(String e){
        super(e);
    }
    public PaginationException(String message, Throwable cause) {
        super(message, cause);
    }
}

