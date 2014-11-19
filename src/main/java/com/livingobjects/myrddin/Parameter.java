package com.livingobjects.myrddin;

public final class Parameter {

    public final String name;

    public final String locatedIn;

    public final String description;

    public final Schema schema;

    public final boolean required;

    public Parameter(String name, String locatedIn, String description, Schema schema, boolean required) {
        this.name = name;
        this.locatedIn = locatedIn;
        this.description = description;
        this.schema = schema;
        this.required = required;
    }
}
