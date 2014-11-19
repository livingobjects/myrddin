package com.livingobjects.myrddin;

import com.google.common.collect.ImmutableList;

public final class ApiSpecification {

    public final String title;

    public final String description;

    public final String version;

    public final ImmutableList<ApiResource> resources;

    public final ImmutableList<Schema> models;

    public final ImmutableList<SecurityScheme> securitySchemes;

    public ApiSpecification(String title, String description, String version, ImmutableList<ApiResource> resources, ImmutableList<Schema> models, ImmutableList<SecurityScheme> securitySchemes) {
        this.title = title;
        this.description = description;
        this.version = version;
        this.resources = resources;
        this.models = models;
        this.securitySchemes = securitySchemes;
    }
}
