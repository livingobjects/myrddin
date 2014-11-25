package com.livingobjects.myrddin.parser;

import com.livingobjects.myrddin.Response;
import com.livingobjects.myrddin.exception.SwaggerException;
import com.livingobjects.myrddin.schema.Schema;

import java.util.Map;
import java.util.Optional;

public class ResponseReader implements YamlReader {

    private final static SchemaReader schemaReader = new SchemaReader();

    public Response read(String code, Map<String, Object> properties) throws SwaggerException {
        String description = (String) properties.get("description");
        Optional<Schema> schema;
        Map<String, Object> schemaMap = readInnerMap(Optional.of(code), properties, "schema", false);
        if (schemaMap != null) {
            schema = Optional.ofNullable(schemaReader.read("schema", schemaMap));
        } else {
            schema = Optional.empty();
        }
        return new Response(code, Optional.ofNullable(description), schema);
    }

}
