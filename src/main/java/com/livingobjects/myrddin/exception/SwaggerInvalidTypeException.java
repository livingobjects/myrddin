package com.livingobjects.myrddin.exception;

import com.livingobjects.myrddin.SchemaTypes;

import java.util.Arrays;

public final class SwaggerInvalidTypeException extends SwaggerException {

    public SwaggerInvalidTypeException(String field, String type) {
        super("The type '" + type + "' of field '" + field + "' must be one of " + Arrays.toString(SchemaTypes.values()) + ".");
    }

}
