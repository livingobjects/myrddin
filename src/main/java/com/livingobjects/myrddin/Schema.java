package com.livingobjects.myrddin;

import com.google.common.collect.ImmutableList;

import java.util.Optional;

public abstract class Schema {

    public final SchemaTypes type;

    public final Optional<String> title;

    public final Optional<String> description;

    public Schema(SchemaTypes type, Optional<String> title, Optional<String> description) {
        this.type = type;
        this.title = title;
        this.description = description;
    }

    public static SchemaScalar scalar(Optional<String> title, Optional<String> description, String type, String format) {
        SchemaTypes schemaType = SchemaTypes.of(type);
        return new SchemaScalar(title, description, schemaType, Optional.ofNullable(format));
    }

    public static SchemaObject object(Optional<String> title, Optional<String> description, ImmutableList<Property> properties) {
        return new SchemaObject(title, description, properties);
    }

    public static SchemaArray array(Optional<String> title, Optional<String> description, Schema itemsType) {
        return new SchemaArray(title, description, itemsType);
    }

    public static SchemaReference reference(Optional<String> title, Optional<String> description, String reference) {
        return new SchemaReference(title, description, reference);
    }

}
