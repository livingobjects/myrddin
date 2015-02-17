package com.livingobjects.myrddin.schema;

import com.google.common.collect.ImmutableList;
import com.livingobjects.myrddin.Property;
import com.livingobjects.myrddin.exception.SwaggerInvalidTypeException;

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

    public static SchemaBoolean bool(Optional<String> title,
                                     Optional<String> description) {
        return new SchemaBoolean(title, description);
    }

    public static SchemaNumber number(Optional<String> title,
                                      Optional<String> description,
                                      Optional<String> format,
                                      Optional<ImmutableList<String>> enumeration,
                                      Optional<Integer> multipleOf,
                                      Optional<Integer> minimum,
                                      Optional<Integer> maximum,
                                      Optional<Boolean> exclusiveMinimum,
                                      Optional<Boolean> exclusiveMaximum) throws SwaggerInvalidTypeException {
        return new SchemaNumber(title, description, format, enumeration, multipleOf, minimum, maximum, exclusiveMinimum, exclusiveMaximum);
    }

    public static SchemaString string(Optional<String> title,
                                      Optional<String> description,
                                      Optional<String> format,
                                      Optional<ImmutableList<String>> enumeration,
                                      Optional<String> pattern,
                                      Optional<Integer> minLength,
                                      Optional<Integer> maxLength) throws SwaggerInvalidTypeException {
        return new SchemaString(title, description, format, enumeration, pattern, minLength, maxLength);
    }

    public static SchemaObject object(Optional<String> title,
                                      Optional<String> description,
                                      ImmutableList<Property> properties,
                                      ImmutableList<String> required,
                                      ImmutableList<Schema> definitions) {
        return new SchemaObject(title, description, properties, required, definitions);
    }

    public static SchemaArray array(Optional<String> title,
                                    Optional<String> description,
                                    Schema itemsType) {
        return new SchemaArray(title, description, itemsType);
    }

    public static SchemaReference reference(Optional<String> title,
                                            Optional<String> description,
                                            String reference) {
        return new SchemaReference(title, description, reference);
    }

    public static SchemaAnyOf anyOf(Optional<String> title,
                                    Optional<String> description,
                                    ImmutableList<Schema> types) {
        return new SchemaAnyOf(title, description, types);
    }

    public static SchemaOneOf oneOf(Optional<String> title,
                                    Optional<String> description,
                                    ImmutableList<Schema> types) {
        return new SchemaOneOf(title, description, types);
    }

}
