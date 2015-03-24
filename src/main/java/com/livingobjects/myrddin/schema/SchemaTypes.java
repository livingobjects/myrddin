package com.livingobjects.myrddin.schema;

public enum SchemaTypes {

    REF("$ref"), NUMBER("number"), INTEGER("integer"), STRING("string"), BOOLEAN("boolean"), OBJECT("object"), ARRAY("array"), ANY_OF("anyOf"), ONE_OF("oneOf");

    public final String value;

    SchemaTypes(String value) {
        this.value = value;
    }

    public static SchemaTypes of(String jsonType) {
        SchemaTypes result = null;
        for (SchemaTypes schemaTypes : values()) {
            if (schemaTypes.value.equalsIgnoreCase(jsonType)) {
                result = schemaTypes;
            }
        }
        return result;
    }

    // FIXME 2015-03-24: Ugly hack for handlebars
    public boolean isRef() {
        return is(REF);
    }

    public boolean isNumber() {
        return is(NUMBER);
    }

    public boolean isInteger() {
        return is(INTEGER);
    }

    public boolean isString() {
        return is(STRING);
    }

    public boolean isBoolean() {
        return is(BOOLEAN);
    }

    public boolean isObject() {
        return is(OBJECT);
    }

    public boolean isArray() {
        return is(ARRAY);
    }

    public boolean isAnyOf() {
        return is(ANY_OF);
    }

    public boolean isOneOf() {
        return is(ONE_OF);
    }

    private boolean is(SchemaTypes schemaTypes) {
        return this == schemaTypes;
    }
}
