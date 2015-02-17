package com.livingobjects.myrddin.parser;

import com.livingobjects.myrddin.Parameter;
import com.livingobjects.myrddin.exception.SwaggerException;
import com.livingobjects.myrddin.schema.Schema;

import java.util.Map;
import java.util.Optional;

public class ParameterReader implements YamlReader {

    public Parameter read(Optional<String> propertyName, Map<String, Object> properties) throws SwaggerException {
        String name = (String) properties.get("name");
        String in = (String) properties.get("in");
        String description = (String) properties.get("description");
        Map<String, Object> schemaProperties = readInnerMap(propertyName, properties, "schema", false);
        if (schemaProperties == null) {
            schemaProperties = properties;
        }
        SchemaReader schemaReader = new SchemaReader();
        Schema schema = schemaReader.read(propertyName, Optional.empty(), schemaProperties);
        Boolean required = (Boolean) properties.get("required");
        if (required == null) {
            required = false;
        }
        return new Parameter(name, in, description, schema, required);
    }

}
