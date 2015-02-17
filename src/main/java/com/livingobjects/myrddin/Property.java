package com.livingobjects.myrddin;

import com.livingobjects.myrddin.schema.Schema;

public final class Property {

    public final String name;

    public final Schema schema;

    public Property(String name, Schema schema) {
        this.name = name;
        this.schema = schema;
    }
}
