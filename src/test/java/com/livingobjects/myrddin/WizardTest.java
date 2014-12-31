package com.livingobjects.myrddin;

import com.livingobjects.myrddin.exception.SwaggerException;
import com.livingobjects.myrddin.schema.Schema;
import com.livingobjects.myrddin.schema.SchemaObject;
import com.livingobjects.myrddin.schema.SchemaOneOf;
import com.livingobjects.myrddin.schema.SchemaTypes;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class WizardTest {

    Wizard tested = new Wizard();

    @Test
    public void testGenerateSpecification() throws IOException, SwaggerException {
        File swaggerFile = new File(getClass().getResource("/test.api").getPath());
        try (FileInputStream in = new FileInputStream(swaggerFile)) {
            ApiSpecification spec = tested.generateSpecification(in);
            assertThat(spec.title).isEqualTo("Test API");
            assertThat(spec.description).isEqualTo("Testing API");
            assertThat(spec.version).isEqualTo("1");

            assertThat(spec.resources).hasSize(2);
            ApiResource apiResource = spec.resources.get(0);
            assertThat(apiResource.uri).isEqualTo("/test/domains");
            assertThat(apiResource.method).isEqualTo("GET");

            ApiResource apiResource2 = spec.resources.get(1);
            assertThat(apiResource2.uri).isEqualTo("/test/domains/{domainId}");
            assertThat(apiResource2.method).isEqualTo("POST");

            assertThat(apiResource.security.get(0).securityScheme).isEqualTo("privateAuthentication");

            assertThat(spec.definitions).hasSize(4);

            Schema schema = spec.definitions.get(0);
            SchemaObject object = (SchemaObject) schema;
            assertThat(object.type).isEqualTo(SchemaTypes.OBJECT);
            assertThat(object.properties).hasSize(4);
            assertThat(object.required).hasSize(3);

            assertThat(object.required.get(0)).isEqualTo("id");
            assertThat(object.required.get(1)).isEqualTo("code");
            assertThat(object.required.get(2)).isEqualTo("name");

            Property thirdProp = object.properties.get(3);
            assertThat(thirdProp.name).isEqualTo("kpi");
            Schema oneOfSchema = thirdProp.schema;
            SchemaOneOf oneOf = (SchemaOneOf) oneOfSchema;
            assertThat(oneOf.type).isEqualTo(SchemaTypes.ONE_OF);
            assertThat(oneOf.types).hasSize(2);
            assertThat(oneOf.types.get(0).type).isEqualTo(SchemaTypes.REF);
            assertThat(oneOf.types.get(1).type).isEqualTo(SchemaTypes.STRING);

            assertThat(spec.securitySchemes).hasSize(1);
        }
    }

    @Test
    public void testGenerateSpecificationUber() throws IOException, SwaggerException {
        File swaggerFile = new File(getClass().getResource("/uber.api").getPath());
        try (FileInputStream in = new FileInputStream(swaggerFile)) {
            ApiSpecification spec = tested.generateSpecification(in);
            assertThat(spec.title).isEqualTo("Uber API");
            assertThat(spec.description).isEqualTo("Move your app forward with the Uber API");
            assertThat(spec.version).isEqualTo("1.0.0");
            assertThat(spec.resources).hasSize(5);
            assertThat(spec.definitions).hasSize(6);
        }
    }

    @Test
    public void testGenerateSpecificationHerokuPets() throws IOException, SwaggerException {
        File swaggerFile = new File(getClass().getResource("/heroku-pets.api").getPath());
        try (FileInputStream in = new FileInputStream(swaggerFile)) {
            ApiSpecification spec = tested.generateSpecification(in);
            assertThat(spec.title).isEqualTo("PetStore on Heroku");
            assertThat(spec.description).isEqualTo("Must be filled");
            assertThat(spec.version).isEqualTo("1.0.0");
            assertThat(spec.resources).hasSize(4);
            assertThat(spec.resources.get(0).uri).isEqualTo("/pet");
            assertThat(spec.definitions).hasSize(1);
        }
    }

}
