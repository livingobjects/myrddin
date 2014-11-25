package com.livingobjects.myrddin.schema;

import java.util.Optional;

public final class SchemaArray extends Schema {

    public final Schema itemsType;

    SchemaArray(Optional<String> title, Optional<String> description, Schema itemsType) {
        super(SchemaTypes.ARRAY, title, description);
        this.itemsType = itemsType;
    }

}
