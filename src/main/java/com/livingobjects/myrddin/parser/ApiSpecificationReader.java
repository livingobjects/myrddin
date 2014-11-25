package com.livingobjects.myrddin.parser;

import com.google.common.collect.ImmutableList;
import com.livingobjects.myrddin.ApiResource;
import com.livingobjects.myrddin.ApiSpecification;
import com.livingobjects.myrddin.SecurityScheme;
import com.livingobjects.myrddin.exception.SwaggerException;
import com.livingobjects.myrddin.exception.SwaggerMissingChildException;
import com.livingobjects.myrddin.schema.Schema;

import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

public class ApiSpecificationReader implements YamlReader {

    private static final ApiResourceReader apiResourceReader = new ApiResourceReader();
    private static final SchemaReader schemaReader = new SchemaReader();

    public ApiSpecification read(Map<String, Object> properties) throws SwaggerException {
        String basePath = (String) properties.get("basePath");

        Map<String, Object> paths = readInnerMap(Optional.<String>empty(), properties, "paths", true);
        ImmutableList<ApiResource> resources = readApiResources(basePath, paths);

        Map<String, Object> info = readInnerMap(Optional.<String>empty(), properties, "info", true);
        String title = readInnerField(Optional.of("info"), info, "title", String.class, true);
        String description = readInnerField(Optional.of("info"), info, "description", String.class, true);
        String version = readInnerField(Optional.of("info"), info, "version", String.class, true);

        Map<String, Object> definitions = readInnerMap(Optional.<String>empty(), properties, "definitions", false);
        ImmutableList<Schema> models = readModelsDefinitions(Optional.of("definitions"), definitions);

        ImmutableList<SecurityScheme> securitySchemes = readSecuritySchemes(readInnerMap(Optional.<String>empty(), properties, "securityDefinitions", false));

        return new ApiSpecification(title, description, version, resources, models, securitySchemes);
    }


    private ImmutableList<SecurityScheme> readSecuritySchemes(Map<String, Object> securityDefinitions) throws SwaggerMissingChildException {
        LinkedList<SecurityScheme> securitySchemes = new LinkedList<>();
        if (securityDefinitions != null) {
            for (Map.Entry<String, Object> entry : securityDefinitions.entrySet()) {
                String title = entry.getKey();
                Map<String, Object> properties = readMap(title, entry.getValue());
                String type = (String) properties.get("type");
                String in = (String) properties.get("in");
                String name = (String) properties.get("name");
                securitySchemes.add(new SecurityScheme(title, name, type, in));
            }
        }
        return ImmutableList.copyOf(securitySchemes);
    }


    private ImmutableList<ApiResource> readApiResources(String basePath, Map<String, Object> paths) throws SwaggerException {
        LinkedList<ApiResource> resources = new LinkedList<>();
        if (paths != null) {
            for (Map.Entry<String, Object> pathEntry : paths.entrySet()) {
                String path = pathEntry.getKey();
                generateDocumentationForResource(resources, basePath, path, readMap(path, pathEntry.getValue()));
            }
        }
        return ImmutableList.copyOf(resources);
    }

    private ImmutableList<Schema> readModelsDefinitions(Optional<String> propertyName, Map<String, Object> definitions) throws SwaggerException {
        LinkedList<Schema> schemas = new LinkedList<>();
        if (definitions != null) {
            for (Map.Entry<String, Object> modelEntry : definitions.entrySet()) {
                String title = modelEntry.getKey();
                Map<String, Object> properties = readMap(title, modelEntry.getValue());
                Optional<String> description = readOptionalString(propertyName, properties, "description");
                schemas.add(schemaReader.readSchemaObject(Optional.ofNullable(title), Optional.ofNullable(title), description, properties));
            }
        }
        return ImmutableList.copyOf(schemas);
    }


    private void generateDocumentationForResource(LinkedList<ApiResource> resources, String parentUri, String currentUri, Map<String, Object> properties) throws SwaggerException {
        String uri;
        if (parentUri != null) {
            uri = parentUri + currentUri;
        } else {
            uri = currentUri;
        }
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            String value = entry.getKey();
            Map<String, Object> resourceProperties = readMap(value, entry.getValue());
            switch (value.toUpperCase()) {
                case "HEAD":
                case "DELETE":
                case "OPTIONS":
                case "PATCH":
                case "GET":
                case "PUT":
                case "POST":
                    resources.add(apiResourceReader.read(uri, value, resourceProperties));
                    break;
                default:
                    generateDocumentationForResource(resources, uri, value, resourceProperties);
            }
        }
    }

}
