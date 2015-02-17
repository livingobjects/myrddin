package com.livingobjects.myrddin.schema;

import com.google.common.collect.ImmutableList;

import java.util.Optional;

public final class SchemaString extends SchemaScalar {

    public final Optional<String> pattern;

    public final Optional<Integer> minLength;

    public final Optional<Integer> maxLength;

    public SchemaString(Optional<String> title,
                        Optional<String> description,
                        Optional<String> format,
                        Optional<ImmutableList<String>> enumeration,
                        Optional<String> pattern,
                        Optional<Integer> minLength,
                        Optional<Integer> maxLength) {
        super(SchemaTypes.STRING, title, description, format, enumeration);
        this.pattern = pattern;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }
}
