package com.livingobjects.myrddin;

import com.google.common.collect.ImmutableList;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Wizard {


    public ApiSpecification generateSpecification(File apiSpecification) throws IOException {
        try (InputStream in = new FileInputStream(apiSpecification)) {
            return generateSpecification(in);
        }
    }

    public ApiSpecification generateSpecification(InputStream apiSpecification) throws FileNotFoundException {
        Yaml yaml = new Yaml();

        Map<String, Object> map = yaml.loadAs(apiSpecification, Map.class);

        String basePath = (String) map.get("basePath");

        Map<String, Object> paths = (Map<String, Object>) map.get("paths");
        ImmutableList<ApiResource> resources = readApiResources(basePath, paths);

        Map<String, Object> info = (Map<String, Object>) map.get("info");
        String title = (String) info.get("title");
        String description = (String) info.get("description");
        String version = (String) info.get("version");

        Map<String, Object> definitions = (Map<String, Object>) map.get("definitions");
        ImmutableList<Schema> models = readApiModels(definitions);

        ImmutableList<SecurityScheme> securitySchemes = readSecuritySchemes((Map<String, Object>) map.get("securityDefinitions"));

        return new ApiSpecification(title, description, version, resources, models, securitySchemes);
    }

    private ImmutableList<SecurityScheme> readSecuritySchemes(Map<String, Object> securityDefinitions) {
        LinkedList<SecurityScheme> securitySchemes = new LinkedList<>();
        if (securityDefinitions != null) {
            for (Map.Entry<String, Object> entry : securityDefinitions.entrySet()) {
                String title = entry.getKey();
                Map<String, Object> properties = (Map<String, Object>) entry.getValue();
                String type = (String) properties.get("type");
                String in = (String) properties.get("in");
                String name = (String) properties.get("name");
                securitySchemes.add(new SecurityScheme(title, name, type, in));
            }
        }
        return ImmutableList.copyOf(securitySchemes);
    }

    private ImmutableList<ApiResource> readApiResources(String basePath, Map<String, Object> paths) {
        LinkedList<ApiResource> resources = new LinkedList<>();
        if (paths != null) {
            for (Map.Entry<String, Object> pathEntry : paths.entrySet()) {
                generateDocumentationForResource(resources, basePath, pathEntry.getKey(), (Map<String, Object>) pathEntry.getValue());
            }
        }
        return ImmutableList.copyOf(resources);
    }

    private ImmutableList<Schema> readApiModels(Map<String, Object> models) {
        LinkedList<Schema> schemas = new LinkedList<>();
        if (models != null) {
            for (Map.Entry<String, Object> modelEntry : models.entrySet()) {
                String title = modelEntry.getKey();
                Map<String, Object> properties = (Map<String, Object>) modelEntry.getValue();
                Optional<String> description = Optional.ofNullable((String) properties.get("description"));
                Schema schema = Schema.object(Optional.ofNullable(title), description, readSchemaProperties((Map<String, Object>) properties.get("properties")));
                schemas.add(schema);
            }
        }
        return ImmutableList.copyOf(schemas);
    }

    private void generateDocumentationForResource(LinkedList<ApiResource> resources, String parentUri, String currentUri, Map<String, Object> properties) {
        String uri;
        if (parentUri != null) {
            uri = parentUri + currentUri;
        } else {
            uri = currentUri;
        }
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            String value = entry.getKey();
            switch (value.toUpperCase()) {
                case "HEAD":
                case "DELETE":
                case "OPTIONS":
                case "PATCH":
                case "GET":
                case "PUT":
                case "POST":
                    Map<String, Object> resourceProperties = (Map<String, Object>) properties.get(value);
                    ImmutableList<Response> responses = readResponses((Map<String, Object>) resourceProperties.get("responses"));
                    ImmutableList<Parameter> parameters = readParameters((List<Map<String, Object>>) resourceProperties.get("parameters"));
                    String summary = (String) resourceProperties.get("summary");
                    Optional<String> description = Optional.ofNullable((String) resourceProperties.get("description"));
                    List<Map<String, Object>> securityList = (List<Map<String, Object>>) resourceProperties.get("security");
                    ImmutableList<Security> security = readSecurity(securityList);
                    ApiResource resource = new ApiResource(uri, value.toUpperCase(), summary, description, security, parameters, responses);
                    resources.add(resource);
                    break;
                default:
                    generateDocumentationForResource(resources, uri, value, (Map<String, Object>) entry.getValue());
            }
        }
    }

    private ImmutableList<Security> readSecurity(List<Map<String, Object>> securityList) {
        List<Security> list = new ArrayList<>();
        if (securityList != null) {
            for (Map<String, Object> properties : securityList) {
                for (Map.Entry<String, Object> entry : properties.entrySet()) {
                    String scheme = entry.getKey();
                    List<String> roles = (List<String>) entry.getValue();
                    list.add(new Security(scheme, ImmutableList.copyOf(roles)));
                }
            }
        }
        return ImmutableList.copyOf(list);
    }

    private ImmutableList<Parameter> readParameters(List<Map<String, Object>> parametersList) {
        if (parametersList != null) {
            LinkedList<Parameter> parameters = new LinkedList<>();
            for (Map<String, Object> properties : parametersList) {
                String name = (String) properties.get("name");
                String in = (String) properties.get("in");
                String description = (String) properties.get("description");
                Map<String, Object> schemaProperties = (Map<String, Object>) properties.get("schema");
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

    private Schema readSchema(Map<String, Object> propertiesMap) {
        return readSchema(null, propertiesMap);
    }

    private Schema readSchema(String definitionTitle, Map<String, Object> propertiesMap) {
        Schema schema;
        String reference = (String) propertiesMap.get("$ref");
        Optional<String> description = Optional.ofNullable((String) propertiesMap.get("description"));
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
                    schema = Schema.array(title, description, readSchema((Map<String, Object>) propertiesMap.get("items")));
                    break;
                case "object":
                    schema = Schema.object(title, description, readSchemaProperties((Map<String, Object>) propertiesMap.get("properties")));
                    break;
                default:
                    schema = Schema.scalar(title, description, type, (String) propertiesMap.get("format"));
                    break;
            }
        }
        return schema;
    }

    private ImmutableList<Property> readSchemaProperties(Map<String, Object> properties) {
        if (properties != null) {
            List<Property> schemas = properties.entrySet().stream().map(e -> {
                String name = e.getKey();
                Map<String, Object> map = (Map<String, Object>) e.getValue();
                return new Property(name, readSchema(map));
            }).collect(Collectors.toList());
            return ImmutableList.copyOf(schemas);
        } else {
            return ImmutableList.of();
        }
    }

    private ImmutableList<Response> readResponses(Map<String, Object> responsesMap) {
        LinkedList<Response> responses = new LinkedList<>();
        for (Map.Entry<String, Object> entry : responsesMap.entrySet()) {
            String code = String.valueOf(entry.getKey());
            responses.add(readResponse(code, (Map<String, Object>) entry.getValue()));
        }
        return ImmutableList.copyOf(responses);
    }

    private Response readResponse(String code, Map<String, Object> properties) {
        String description = (String) properties.get("description");
        Optional<Schema> schema;
        Map<String, Object> schemaMap = (Map<String, Object>) properties.get("schema");
        if (schemaMap != null) {
            schema = Optional.ofNullable(readSchema(schemaMap));
        } else {
            schema = Optional.empty();
        }
        return new Response(code, Optional.ofNullable(description), schema);
    }

}
