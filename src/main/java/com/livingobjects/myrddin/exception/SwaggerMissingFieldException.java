package com.livingobjects.myrddin.exception;

import java.util.Optional;

public final class SwaggerMissingFieldException extends SwaggerException {

    public SwaggerMissingFieldException(Optional<String> parent, String field) {
        super(parent.isPresent() ? "The field '" + field + "' is not defined for parent '" + parent.get() + "'." : "The field '" + field + "' is not defined.");
    }

}
