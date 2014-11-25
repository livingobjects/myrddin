package com.livingobjects.myrddin.schema;

import com.google.common.collect.ImmutableList;

import java.util.Optional;

public final class SchemaAnyOf extends Schema {

    public final ImmutableList<Schema> types;

    public SchemaAnyOf(Optional<String> title, Optional<String> description, ImmutableList<Schema> types) {
        super(SchemaTypes.ANY_OF, title, description);
        this.types = types;
    }
}
