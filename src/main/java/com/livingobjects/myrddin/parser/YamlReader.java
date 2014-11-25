package com.livingobjects.myrddin.parser;

import com.google.common.collect.ImmutableList;
import com.livingobjects.myrddin.exception.SwaggerException;
import com.livingobjects.myrddin.exception.SwaggerInvalidFormatException;
import com.livingobjects.myrddin.exception.SwaggerMissingChildException;
import com.livingobjects.myrddin.exception.SwaggerMissingFieldException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface YamlReader {

    default Map<String, Object> readInnerMap(Optional<String> parent, Map<String, Object> properties, String property) throws SwaggerException {
        return readInnerMap(parent, properties, property, true);
    }

    @SuppressWarnings("unchecked")
    default Map<String, Object> readInnerMap(Optional<String> parent, Map<String, Object> properties, String property, boolean required) throws SwaggerException {
        return readInnerField(parent, properties, property, Map.class, required);
    }

    @SuppressWarnings("unchecked")
    default <T> T readInnerField(Optional<String> parent, Map<String, Object> properties, String property, Class<T> clazz, boolean required) throws SwaggerException {
        Object value = properties.get(property);
        if (value != null) {
            if (clazz.isInstance(value)) {
                return clazz.cast(value);
            } else {
                throw new SwaggerInvalidFormatException(parent, property, clazz.getSimpleName());
            }
        } else {
            if (required) {
                throw new SwaggerMissingFieldException(parent, property);
            } else {
                return null;
            }
        }
    }

    @SuppressWarnings("unchecked")
    default Map<String, Object> readMap(String title, Object value) throws SwaggerMissingChildException {
        if (value != null) {
            if (value instanceof Map) {
                return (Map<String, Object>) value;
            } else {
                throw new SwaggerMissingChildException(title);
            }
        } else {
            throw new SwaggerMissingChildException(title);
        }
    }

    @SuppressWarnings("unchecked")
    default ImmutableList<String> readStringList(Optional<String> parent, Map<String, Object> properties, String property) throws SwaggerException {
        List<String> innerList = readInnerField(parent, properties, property, List.class, false);
        if (innerList != null) {
            return ImmutableList.copyOf(innerList);
        } else {
            return ImmutableList.of();
        }
    }

    @SuppressWarnings("unchecked")
    default Optional<ImmutableList<String>> readOptionalStringList(Optional<String> parent, Map<String, Object> properties, String property) throws SwaggerException {
        List<String> innerList = readInnerField(parent, properties, property, List.class, false);
        if (innerList != null) {
            return Optional.of(ImmutableList.copyOf(innerList));
        } else {
            return Optional.empty();
        }
    }

    @SuppressWarnings("unchecked")
    default Optional<ImmutableList<Map<String, Object>>> readOptionalInnerList(Optional<String> parent, Map<String, Object> properties, String property) throws SwaggerException {
        List<Map<String, Object>> innerList = readInnerField(parent, properties, property, List.class, false);
        if (innerList != null) {
            return Optional.of(ImmutableList.copyOf(innerList));
        } else {
            return Optional.empty();
        }
    }

    @SuppressWarnings("unchecked")
    default ImmutableList<Map<String, Object>> readInnerList(Optional<String> parent, Map<String, Object> properties, String property) throws SwaggerException {
        List<Map<String, Object>> innerList = readInnerField(parent, properties, property, List.class, false);
        if (innerList != null) {
            return ImmutableList.copyOf(innerList);
        } else {
            return ImmutableList.of();
        }
    }

    @SuppressWarnings("unchecked")
    default ImmutableList<String> readStringList(String title, Object value) throws SwaggerMissingChildException {
        if (value != null) {
            if (value instanceof List) {
                return ImmutableList.copyOf((List<String>) value);
            } else {
                throw new SwaggerMissingChildException(title);
            }
        } else {
            throw new SwaggerMissingChildException(title);
        }
    }

    default Optional<String> readOptionalString(Optional<String> propertyName, Map<String, Object> properties, String property) throws SwaggerException {
        return Optional.ofNullable(readInnerField(propertyName, properties, property, String.class, false));
    }

    default Optional<Integer> readOptionalInteger(Optional<String> propertyName, Map<String, Object> properties, String property) throws SwaggerException {
        return Optional.ofNullable(readInnerField(propertyName, properties, property, Integer.class, false));
    }

}
