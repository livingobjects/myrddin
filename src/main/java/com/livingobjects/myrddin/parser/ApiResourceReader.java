package com.livingobjects.myrddin.parser;

import com.google.common.collect.ImmutableList;
import com.livingobjects.myrddin.ApiResource;
import com.livingobjects.myrddin.Parameter;
import com.livingobjects.myrddin.Response;
import com.livingobjects.myrddin.Security;
import com.livingobjects.myrddin.exception.SwaggerException;
import com.livingobjects.myrddin.exception.SwaggerMissingChildException;

import java.util.*;

public class ApiResourceReader implements YamlReader {

    private static final ParameterReader parameterReader = new ParameterReader();
    private static final ResponseReader responseReader = new ResponseReader();

    public ApiResource read(String uri, String method, Map<String, Object> resourceProperties) throws SwaggerException {
        Optional<String> methodOpt = Optional.of(method);
        Map<String, Object> responsesMap = readInnerMap(methodOpt, resourceProperties, "responses");
        ImmutableList<Response> responses = readResponses(responsesMap);
        List<Map<String, Object>> parametersMap = readInnerList(methodOpt, resourceProperties, "parameters");
        ImmutableList<Parameter> parameters = readParameters(methodOpt, parametersMap);
        String summary = (String) resourceProperties.get("summary");
        Optional<String> description = readOptionalString(methodOpt, resourceProperties, "description");
        List<Map<String, Object>> securityList = readInnerList(methodOpt, resourceProperties, "security");
        ImmutableList<Security> security = readSecurity(securityList);
        return new ApiResource(uri, method.toUpperCase(), summary, description, security, parameters, responses);
    }

    private ImmutableList<Security> readSecurity(List<Map<String, Object>> securityList) throws SwaggerMissingChildException {
        List<Security> list = new ArrayList<>();
        if (securityList != null) {
            for (Map<String, Object> properties : securityList) {
                for (Map.Entry<String, Object> entry : properties.entrySet()) {
                    String scheme = entry.getKey();
                    List<String> roles = readStringList(scheme, entry.getValue());
                    list.add(new Security(scheme, ImmutableList.copyOf(roles)));
                }
            }
        }
        return ImmutableList.copyOf(list);
    }

    private ImmutableList<Parameter> readParameters(Optional<String> parent, List<Map<String, Object>> parametersList) throws SwaggerException {
        if (parametersList != null) {
            LinkedList<Parameter> parameters = new LinkedList<>();
            for (Map<String, Object> properties : parametersList) {
                parameters.add(parameterReader.read(parent, properties));
            }
            return ImmutableList.copyOf(parameters);
        } else {
            return ImmutableList.of();
        }
    }

    private ImmutableList<Response> readResponses(Map<String, Object> responsesMap) throws SwaggerException {
        LinkedList<Response> responses = new LinkedList<>();
        for (Map.Entry<String, Object> entry : responsesMap.entrySet()) {
            String code = String.valueOf(entry.getKey());
            responses.add(responseReader.read(code, readMap(code, entry.getValue())));
        }
        return ImmutableList.copyOf(responses);
    }

}
