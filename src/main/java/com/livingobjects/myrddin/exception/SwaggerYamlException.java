package com.livingobjects.myrddin.exception;

public class SwaggerYamlException extends SwaggerException {

    public SwaggerYamlException(Throwable e) {
        super("The Swagger file is not a valid Yaml file.", e);
    }
}
