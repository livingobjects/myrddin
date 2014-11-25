package com.livingobjects.myrddin.schema;

import com.google.common.collect.ImmutableList;

import java.util.Optional;

public abstract class SchemaScalar extends Schema {

    public Optional<String> format;

    public Optional<ImmutableList<String>> enumeration;

    SchemaScalar(SchemaTypes type,
                 Optional<String> title,
                 Optional<String> description,
                 Optional<String> format,
                 Optional<ImmutableList<String>> enumeration) {
        super(type, title, description);
        this.format = format;
        this.enumeration = enumeration;
    }
}
