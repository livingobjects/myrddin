package com.livingobjects.myrddin.exception;

public final class SwaggerMissingChildException extends SwaggerException {

    public SwaggerMissingChildException(String parent) {
        super("The field '" + parent + "' must define inner values.");
    }

}
