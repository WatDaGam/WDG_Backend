package com.wdg.wdgbackend.controller.util;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {
    private final HttpStatus status;

    public CustomException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
