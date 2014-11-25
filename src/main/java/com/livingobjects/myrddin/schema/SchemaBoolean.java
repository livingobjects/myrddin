package com.livingobjects.myrddin.schema;

import java.util.Optional;

public final class SchemaBoolean extends Schema {

    public SchemaBoolean(Optional<String> title, Optional<String> description) {
        super(SchemaTypes.BOOLEAN, title, description);
    }
}
