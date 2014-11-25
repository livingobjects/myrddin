package com.livingobjects.myrddin.schema;

import com.google.common.collect.ImmutableList;

import java.util.Optional;

public final class SchemaOneOf extends Schema {

    public final ImmutableList<Schema> types;

    public SchemaOneOf(Optional<String> title, Optional<String> description, ImmutableList<Schema> types) {
        super(SchemaTypes.ONE_OF, title, description);
        this.types = types;
    }
}
