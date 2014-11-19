package com.livingobjects.myrddin;

import java.util.Optional;

public final class Response {

    public final String code;

    public final Optional<String> description;

    public final Optional<Schema> schema;

    public Response(String code, Optional<String> description, Optional<Schema> schema) {
        this.code = code;
        this.description = description;
        this.schema = schema;
    }
}
