package com.livingobjects.myrddin;

import com.google.common.collect.ImmutableList;
import com.livingobjects.myrddin.schema.Schema;

public final class ApiSpecification {

    public final String title;

    public final String description;

    public final String version;

    public final ImmutableList<ApiResource> resources;

    public final ImmutableList<Schema> definitions;

    public final ImmutableList<SecurityScheme> securitySchemes;

    public ApiSpecification(String title, String description, String version, ImmutableList<ApiResource> resources, ImmutableList<Schema> definitions, ImmutableList<SecurityScheme> securitySchemes) {
        this.title = title;
        this.description = description;
        this.version = version;
        this.resources = resources;
        this.definitions = definitions;
        this.securitySchemes = securitySchemes;
    }
}
