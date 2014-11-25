package com.livingobjects.myrddin.schema;

import com.google.common.collect.ImmutableList;
import com.livingobjects.myrddin.Property;

import java.util.Optional;

public final class SchemaObject extends Schema {

    public final ImmutableList<Property> properties;

    public final ImmutableList<String> required;

    public final ImmutableList<Schema> definitions;

    public SchemaObject(Optional<String> title,
                        Optional<String> description,
                        ImmutableList<Property> properties,
                        ImmutableList<String> required,
                        ImmutableList<Schema> definitions) {
        super(SchemaTypes.OBJECT, title, description);
        this.properties = properties;
        this.required = required;
        this.definitions = definitions;
    }
}
