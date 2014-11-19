package com.livingobjects.myrddin;

import java.util.Optional;

public final class SchemaScalar extends Schema {

    public Optional<String> format;

    public SchemaScalar(Optional<String> title, Optional<String> description, SchemaTypes type, Optional<String> format) {
        super(type, title, description);
        this.format = format;
    }
}
