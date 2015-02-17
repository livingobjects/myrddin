package com.livingobjects.myrddin.exception;

import com.livingobjects.myrddin.schema.SchemaTypes;

import java.util.Arrays;
import java.util.Optional;

public final class SwaggerInvalidTypeException extends SwaggerException {

    public SwaggerInvalidTypeException(Optional<String> field, String type) {
        super(field.isPresent() ?
                        "The type '" + type + "' of field '" + field + "' must be one of " + Arrays.toString(SchemaTypes.values()) + "." :
                        "The type '" + type + "' is invalid, it must be one of " + Arrays.toString(SchemaTypes.values()) + "."
        );
    }

}
