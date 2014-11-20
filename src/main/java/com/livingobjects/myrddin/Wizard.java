package com.livingobjects.myrddin;

import com.google.common.collect.ImmutableList;
import com.livingobjects.myrddin.exception.*;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.InputStream;
import java.util.*;

public class Wizard {

    @SuppressWarnings("unchecked")
    public ApiSpecification generateSpecification(InputStream apiSpecification) throws SwaggerException {
        Yaml yaml = new Yaml();
        try {
            Object global = yaml.loadAs(apiSpecification, Map.class);
            if (global != null) {
                Map<String, Object> map = (Map<String, Object>) global;
                String basePath = (String) map.get("basePath");

                Map<String, Object> paths = readInnerMap(map, "paths", true);
                ImmutableList<ApiResource> resources = readApiResources(basePath, paths);

                Map<String, Object> info = readInnerMap(map, "info", true);
                String title = readInnerField(Optional.of("info"), info, "title", String.class, true);
                String description = readInnerField(Optional.of("info"), info, "description", String.class, true);
                String version = readInnerField(Optional.of("info"), info, "version", String.class, true);

                Map<String, Object> definitions = readInnerMap(map, "definitions", false);
                ImmutableList<Schema> models = readApiModels(definitions);

                ImmutableList<SecurityScheme> securitySchemes = readSecuritySchemes(readInnerMap(map, "securityDefinitions", false));

                return new ApiSpecification(title, description, version, resources, models, securitySchemes);
            } else {
                throw new SwaggerInvalidFormatException(Optional.empty(), "Yaml", "Map");
            }
        } catch (YAMLException e) {
            throw new SwaggerYamlException(e);
        }
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

    @SuppressWarnings("unchecked")
    private Map<String, Object> readMap(String title, Object value) throws SwaggerMissingChildException {
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
    private List<String> readList(String title, Object value) throws SwaggerMissingChildException {
        if (value != null) {
            if (value instanceof List) {
                return (List<String>) value;
            } else {
                throw new SwaggerMissingChildException(title);
            }
        } else {
            throw new SwaggerMissingChildException(title);
        }
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

    private ImmutableList<Schema> readApiModels(Map<String, Object> models) throws SwaggerException {
        LinkedList<Schema> schemas = new LinkedList<>();
        if (models != null) {
            for (Map.Entry<String, Object> modelEntry : models.entrySet()) {
                String title = modelEntry.getKey();
                Map<String, Object> properties = readMap(title, modelEntry.getValue());
                Optional<String> description = readOptionalString(properties, "description");
                Map<String, Object> propertiesMap = readInnerMap(Optional.of(title), properties, "properties");
                Schema schema = Schema.object(Optional.ofNullable(title), description, readSchemaProperties(propertiesMap));
                schemas.add(schema);
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
                    Map<String, Object> responsesMap = readInnerMap(Optional.of(value), resourceProperties, "responses");
                    ImmutableList<Response> responses = readResponses(responsesMap);
                    List<Map<String, Object>> parametersMap = readInnerList(Optional.of(value), resourceProperties, "parameters", false);
                    ImmutableList<Parameter> parameters = readParameters(value, parametersMap);
                    String summary = (String) resourceProperties.get("summary");
                    Optional<String> description = readOptionalString(resourceProperties, "description");
                    List<Map<String, Object>> securityList = readInnerList(Optional.of(value), resourceProperties, "security", false);
                    ImmutableList<Security> security = readSecurity(securityList);
                    ApiResource resource = new ApiResource(uri, value.toUpperCase(), summary, description, security, parameters, responses);
                    resources.add(resource);
                    break;
                default:
                    generateDocumentationForResource(resources, uri, value, resourceProperties);
            }
        }
    }

    private ImmutableList<Security> readSecurity(List<Map<String, Object>> securityList) throws SwaggerMissingChildException {
        List<Security> list = new ArrayList<>();
        if (securityList != null) {
            for (Map<String, Object> properties : securityList) {
                for (Map.Entry<String, Object> entry : properties.entrySet()) {
                    String scheme = entry.getKey();
                    List<String> roles = readList(scheme, entry.getValue());
                    list.add(new Security(scheme, ImmutableList.copyOf(roles)));
                }
            }
        }
        return ImmutableList.copyOf(list);
    }

    private ImmutableList<Parameter> readParameters(String parent, List<Map<String, Object>> parametersList) throws SwaggerException {
        if (parametersList != null) {
            LinkedList<Parameter> parameters = new LinkedList<>();
            for (Map<String, Object> properties : parametersList) {
                String name = (String) properties.get("name");
                String in = (String) properties.get("in");
                String description = (String) properties.get("description");
                Map<String, Object> schemaProperties = readInnerMap(Optional.of(parent), properties, "schema", false);
                if (schemaProperties == null) {
                    schemaProperties = properties;
                }
                Schema schema = readSchema(schemaProperties);
                Boolean required = (Boolean) properties.get("required");
                if (required == null) {
                    required = false;
                }
                parameters.add(new Parameter(name, in, description, schema, required));
            }
            return ImmutableList.copyOf(parameters);
        } else {
            return ImmutableList.of();
        }
    }

    private Schema readSchema(Map<String, Object> propertiesMap) throws SwaggerException {
        return readSchema(null, propertiesMap);
    }

    private Schema readSchema(String definitionTitle, Map<String, Object> propertiesMap) throws SwaggerException {
        Schema schema;
        String reference = (String) propertiesMap.get("$ref");
        Optional<String> description = readOptionalString(propertiesMap, "description");
        if (reference != null) {
            schema = Schema.reference(Optional.empty(), description, reference);
        } else {
            Optional<String> title = Optional.ofNullable(definitionTitle);
            if (!title.isPresent()) {
                title = Optional.ofNullable((String) propertiesMap.get("title"));
            }
            String type = (String) propertiesMap.get("type");
            switch (type) {
                case "array":
                    schema = Schema.array(title, description, readSchema(readInnerMap(title, propertiesMap, "items")));
                    break;
                case "object":
                    schema = Schema.object(title, description, readSchemaProperties(readInnerMap(title, propertiesMap, "properties")));
                    break;
                default:
                    schema = Schema.scalar(title, description, type, readOptionalString(propertiesMap, "format"));
                    break;
            }
        }
        return schema;
    }

    private ImmutableList<Property> readSchemaProperties(Map<String, Object> properties) throws SwaggerException {
        if (properties != null) {
            List<Property> schemas = new ArrayList<>();
            for (Map.Entry<String, Object> e : properties.entrySet()) {
                String name = e.getKey();
                Map<String, Object> map = readMap(name, e.getValue());
                schemas.add(new Property(name, readSchema(map)));
            }
            return ImmutableList.copyOf(schemas);
        } else {
            return ImmutableList.of();
        }
    }

    private ImmutableList<Response> readResponses(Map<String, Object> responsesMap) throws SwaggerException {
        LinkedList<Response> responses = new LinkedList<>();
        for (Map.Entry<String, Object> entry : responsesMap.entrySet()) {
            String code = String.valueOf(entry.getKey());
            responses.add(readResponse(code, readMap(code, entry.getValue())));
        }
        return ImmutableList.copyOf(responses);
    }

    private Response readResponse(String code, Map<String, Object> properties) throws SwaggerException {
        String description = (String) properties.get("description");
        Optional<Schema> schema;
        Map<String, Object> schemaMap = readInnerMap(Optional.ofNullable(code), properties, "schema", false);
        if (schemaMap != null) {
            schema = Optional.ofNullable(readSchema(schemaMap));
        } else {
            schema = Optional.empty();
        }
        return new Response(code, Optional.ofNullable(description), schema);
    }

    private Map<String, Object> readInnerMap(Map<String, Object> properties, String property, boolean required) throws SwaggerException {
        return readInnerMap(Optional.empty(), properties, property, required);
    }

    private Map<String, Object> readInnerMap(Optional<String> parent, Map<String, Object> properties, String property) throws SwaggerException {
        return readInnerMap(parent, properties, property, true);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> readInnerMap(Optional<String> parent, Map<String, Object> properties, String property, boolean required) throws SwaggerException {
        return readInnerField(parent, properties, property, Map.class, required);
    }

    @SuppressWarnings("unchecked")
    private <T> T readInnerField(Optional<String> parent, Map<String, Object> properties, String property, Class<T> clazz, boolean required) throws SwaggerException {
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
    private List<Map<String, Object>> readInnerList(Optional<String> parent, Map<String, Object> properties, String property, boolean required) throws SwaggerException {
        return readInnerField(parent, properties, property, List.class, required);
    }

    private Optional<String> readOptionalString(Map<String, Object> properties, String property) {
        return Optional.ofNullable((String) properties.get(property));
    }

}
