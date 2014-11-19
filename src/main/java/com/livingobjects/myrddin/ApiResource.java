package com.livingobjects.myrddin;

import com.google.common.collect.ImmutableList;

import java.util.Optional;

public final class ApiResource {

    public final String uri;

    public final String method;

    public final String summary;

    public final Optional<String> description;

    public final ImmutableList<Security> security;

    public final ImmutableList<Parameter> parameters;

    public final ImmutableList<Response> responses;

    public ApiResource(String uri, String method, String summary, Optional<String> description, ImmutableList<Security> security, ImmutableList<Parameter> parameters, ImmutableList<Response> responses) {
        this.uri = uri;
        this.method = method;
        this.summary = summary;
        this.description = description;
        this.security = security;
        this.parameters = parameters;
        this.responses = responses;
    }

}
