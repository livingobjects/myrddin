package com.livingobjects.myrddin.schema;

import com.google.common.collect.ImmutableList;

import java.util.Optional;

public final class SchemaNumber extends SchemaScalar {

    public final Optional<Integer> multipleOf;

    public final Optional<Integer> minimum;

    public final Optional<Integer> maximum;

    public final Optional<Boolean> exclusiveMinimum;

    public final Optional<Boolean> exclusiveMaximum;

    public SchemaNumber(Optional<String> title,
                        Optional<String> description,
                        Optional<String> format,
                        Optional<ImmutableList<String>> enumeration,
                        Optional<Integer> multipleOf,
                        Optional<Integer> minimum,
                        Optional<Integer> maximum,
                        Optional<Boolean> exclusiveMinimum,
                        Optional<Boolean> exclusiveMaximum) {
        super(SchemaTypes.NUMBER, title, description, format, enumeration);
        this.multipleOf = multipleOf;
        this.minimum = minimum;
        this.maximum = maximum;
        this.exclusiveMinimum = exclusiveMinimum;
        this.exclusiveMaximum = exclusiveMaximum;
    }
}
