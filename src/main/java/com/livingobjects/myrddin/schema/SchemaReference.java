package com.livingobjects.myrddin.schema;

import java.util.Optional;

public final class SchemaReference extends Schema {

    public final String reference;

    SchemaReference(Optional<String> title, Optional<String> description, String reference) {
        super(SchemaTypes.REF, title, description);
        this.reference = reference;
    }

}
