package com.security.demo.exception;

public class APIException extends RuntimeException{
    public APIException(String message) {
        super(message);
    }
}
