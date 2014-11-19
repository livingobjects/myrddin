package com.livingobjects.myrddin;

import com.google.common.collect.ImmutableList;

import java.util.Optional;

public final class SchemaObject extends Schema {

    public final ImmutableList<Property> properties;

    SchemaObject(Optional<String> title, Optional<String> description, ImmutableList<Property> properties) {
        super(SchemaTypes.OBJECT, title, description);
        this.properties = properties;
    }

}
