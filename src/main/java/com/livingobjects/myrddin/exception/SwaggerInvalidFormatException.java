package com.livingobjects.myrddin.exception;

import java.util.Optional;

public final class SwaggerInvalidFormatException extends SwaggerException {

    public SwaggerInvalidFormatException(Optional<String> parent, String field, String format) {
        super(parent.isPresent() ?
                "The field '" + field + "' for parent '" + parent.get() + "' has wrong format. A '" + format + "' is expected here." :
                "The field '" + field + "' has wrong format. A '" + format + "' is expected here.");
    }

}
