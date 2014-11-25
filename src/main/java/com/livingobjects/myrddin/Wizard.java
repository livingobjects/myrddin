package com.livingobjects.myrddin;

import com.livingobjects.myrddin.exception.SwaggerException;
import com.livingobjects.myrddin.exception.SwaggerInvalidFormatException;
import com.livingobjects.myrddin.exception.SwaggerYamlException;
import com.livingobjects.myrddin.parser.ApiSpecificationReader;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

public class Wizard {

    private final static ApiSpecificationReader API_SPECIFICATION_READER = new ApiSpecificationReader();

    @SuppressWarnings("unchecked")
    public ApiSpecification generateSpecification(InputStream apiSpecification) throws SwaggerException {
        Yaml yaml = new Yaml();
        try {
            Object global = yaml.loadAs(apiSpecification, Map.class);
            if (global != null) {
                Map<String, Object> map = (Map<String, Object>) global;
                return API_SPECIFICATION_READER.read(map);
            } else {
                throw new SwaggerInvalidFormatException(Optional.empty(), "Yaml", "Map");
            }
        } catch (YAMLException e) {
            throw new SwaggerYamlException(e);
        }
    }

}
