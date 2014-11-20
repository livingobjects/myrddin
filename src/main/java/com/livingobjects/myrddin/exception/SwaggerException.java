package com.livingobjects.myrddin.exception;

public abstract class SwaggerException extends Exception {

    SwaggerException(String message, Throwable cause) {
        super(message, cause);
    }

    SwaggerException(String message) {
        super(message);
    }
}
