package com.livingobjects.myrddin;

public enum SchemaTypes {

    REF("$ref"), NUMBER("number"), INTEGER("integer"), STRING("string"), BOOLEAN("boolean"), OBJECT("object"), ARRAY("array");

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
}
