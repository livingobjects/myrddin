package com.livingobjects.myrddin.parser;

import com.google.common.collect.ImmutableList;
import com.livingobjects.myrddin.Property;
import com.livingobjects.myrddin.exception.SwaggerException;
import com.livingobjects.myrddin.exception.SwaggerInvalidTypeException;
import com.livingobjects.myrddin.schema.Schema;

import java.util.*;

public class SchemaReader implements YamlReader {

    public Schema read(String parent, Optional<String> definitionTitle, Map<String, Object> propertiesMap) throws SwaggerException {
        return read(Optional.of(parent), definitionTitle, propertiesMap);
    }

    public Schema read(Optional<String> parent, Optional<String> definitionTitle, Map<String, Object> propertiesMap) throws SwaggerException {
        Schema schema;
        String reference = (String) propertiesMap.get("$ref");
        Optional<String> description = readOptionalString(parent, propertiesMap, "description");
        if (reference != null) {
            schema = Schema.reference(Optional.empty(), description, reference);
        } else {
            Optional<String> title = definitionTitle;
            if (!title.isPresent()) {
                title = Optional.ofNullable((String) propertiesMap.get("title"));
            }
            String type = (String) propertiesMap.get("type");
            if (type != null) {
                switch (type) {
                    case "array":
                        schema = Schema.array(title, description, read(type, Optional.empty(), readInnerMap(title, propertiesMap, "items")));
                        break;
                    case "object":
                        schema = readSchemaObject(parent, title, description, propertiesMap);
                        break;
                    case "string":
                        schema = readSchemaString(parent, title, description, propertiesMap);
                        break;
                    case "boolean":
                        schema = Schema.bool(title, description);
                        break;
                    case "integer":
                    case "number":
                        schema = readSchemaNumber(parent, title, description, propertiesMap);
                        break;
                    default:
                        throw new SwaggerInvalidTypeException(parent, type);
                }
            } else {
                Optional<ImmutableList<Map<String, Object>>> anyOf = readOptionalInnerList(parent, propertiesMap, "anyOf");
                if (anyOf.isPresent()) {
                    schema = Schema.anyOf(title, description, readSchemaList(parent, anyOf.get()));
                } else {
                    Optional<ImmutableList<Map<String, Object>>> oneOf = readOptionalInnerList(parent, propertiesMap, "oneOf");
                    if (oneOf.isPresent()) {
                        schema = Schema.oneOf(title, description, readSchemaList(parent, oneOf.get()));
                    } else {
                        throw new SwaggerInvalidTypeException(parent, "undefined");
                    }
                }
            }
        }
        return schema;
    }

    @SuppressWarnings("unchecked")
    public ImmutableList<Schema> readSchemaList(Optional<String> parent, ImmutableList<Map<String, Object>> list) throws SwaggerException {
        LinkedList<Schema> schemas = new LinkedList<>();
        for (Map<String, Object> map : list) {
            schemas.add(read(parent, Optional.empty(), map));
        }
        return ImmutableList.copyOf(schemas);
    }

    private Schema readSchemaString(Optional<String> propertyName, Optional<String> title, Optional<String> description, Map<String, Object> propertiesMap) throws SwaggerException {
        Optional<String> format = readOptionalString(propertyName, propertiesMap, "format");
        Optional<ImmutableList<String>> enumeration = readOptionalStringList(propertyName, propertiesMap, "enum");
        Optional<String> pattern = readOptionalString(propertyName, propertiesMap, "pattern");
        Optional<Integer> minLength = readOptionalInteger(propertyName, propertiesMap, "minLength");
        Optional<Integer> maxLength = readOptionalInteger(propertyName, propertiesMap, "maxLength");
        return Schema.string(title, description, format, enumeration, pattern, minLength, maxLength);
    }

    private Schema readSchemaNumber(Optional<String> propertyName, Optional<String> title, Optional<String> description, Map<String, Object> propertiesMap) throws SwaggerException {
        Optional<String> format = readOptionalString(propertyName, propertiesMap, "format");
        Optional<ImmutableList<String>> enumeration = readOptionalStringList(propertyName, propertiesMap, "enum");
        Optional<Integer> multipleOf = Optional.empty();
        Optional<Integer> minimum = Optional.empty();
        Optional<Integer> maximum = Optional.empty();
        Optional<Boolean> exclusiveMinimum = Optional.empty();
        Optional<Boolean> exclusiveMaximum = Optional.empty();
        return Schema.number(title, description, format, enumeration, multipleOf, minimum, maximum, exclusiveMinimum, exclusiveMaximum);
    }

    public Schema readSchemaObject(Optional<String> propertyName, Optional<String> title, Optional<String> description, Map<String, Object> propertiesMap) throws SwaggerException {
        ImmutableList<String> required = readStringList(propertyName, propertiesMap, "required");
        Map<String, Object> properties = readInnerMap(propertyName, propertiesMap, "properties");
        ImmutableList<Map<String, Object>> definitionsList = readInnerList(propertyName, propertiesMap, "definitions");
        ImmutableList<Schema> definitions = readSchemaList(propertyName, definitionsList);
        return Schema.object(title, description, readSchemaProperties(propertyName, properties), required, definitions);
    }

    private ImmutableList<Property> readSchemaProperties(Optional<String> propertyName, Map<String, Object> properties) throws SwaggerException {
        if (properties != null) {
            List<Property> schemas = new ArrayList<>();
            for (Map.Entry<String, Object> e : properties.entrySet()) {
                String name = e.getKey();
                Map<String, Object> map = readMap(name, e.getValue());
                schemas.add(new Property(name, read(propertyName, Optional.empty(), map)));
            }
            return ImmutableList.copyOf(schemas);
        } else {
            return ImmutableList.of();
        }
    }

}
