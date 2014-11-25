package com.livingobjects.myrddin;

import com.livingobjects.myrddin.exception.SwaggerException;
import com.livingobjects.myrddin.schema.Schema;
import com.livingobjects.myrddin.schema.SchemaObject;
import com.livingobjects.myrddin.schema.SchemaOneOf;
import com.livingobjects.myrddin.schema.SchemaTypes;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class WizardTest {

    Wizard tested = new Wizard();

    @Test
    public void testGenerateSpecification() throws IOException, SwaggerException {
        File swaggerFile = new File(getClass().getResource("/test.api").getPath());
        try (FileInputStream in = new FileInputStream(swaggerFile)) {
            ApiSpecification spec = tested.generateSpecification(in);
            Assert.assertEquals("Test API", spec.title);
            Assert.assertEquals("Testing API", spec.description);
            Assert.assertEquals("1", spec.version);

            Assert.assertEquals(2, spec.resources.size());
            ApiResource apiResource = spec.resources.get(0);
            Assert.assertEquals("/test/domains", apiResource.uri);
            Assert.assertEquals("GET", apiResource.method);

            ApiResource apiResource2 = spec.resources.get(1);
            Assert.assertEquals("/test/domains/{domainId}", apiResource2.uri);
            Assert.assertEquals("POST", apiResource2.method);

            Assert.assertEquals("privateAuthentication", apiResource.security.get(0).securityScheme);

            Assert.assertEquals(4, spec.definitions.size());

            Schema schema = spec.definitions.get(0);
            if (schema instanceof SchemaObject) {
                SchemaObject object = (SchemaObject) schema;
                Assert.assertEquals(SchemaTypes.OBJECT, object.type);
                Assert.assertEquals(4, object.properties.size());
                Assert.assertEquals(3, object.required.size());

                Assert.assertEquals("id", object.required.get(0));
                Assert.assertEquals("code", object.required.get(1));
                Assert.assertEquals("name", object.required.get(2));

                Property thirdProp = object.properties.get(3);
                Assert.assertEquals("kpi", thirdProp.name);
                Schema oneOfSchema = thirdProp.schema;
                if (oneOfSchema instanceof SchemaOneOf) {
                    SchemaOneOf oneOf = (SchemaOneOf) oneOfSchema;
                    Assert.assertEquals(SchemaTypes.ONE_OF, oneOf.type);
                    Assert.assertEquals(2, oneOf.types.size());
                    Assert.assertEquals(SchemaTypes.REF, oneOf.types.get(0).type);
                    Assert.assertEquals(SchemaTypes.STRING, oneOf.types.get(1).type);
                }else {
                    Assert.fail("3rd definition is not of type OneOf!");
                }
            }

            Assert.assertEquals(1, spec.securitySchemes.size());
        }
    }

    @Test
    public void testGenerateSpecificationUber() throws IOException, SwaggerException {
        File swaggerFile = new File(getClass().getResource("/uber.api").getPath());
        try (FileInputStream in = new FileInputStream(swaggerFile)) {
            ApiSpecification spec = tested.generateSpecification(in);
            Assert.assertEquals("Uber API", spec.title);
            Assert.assertEquals("Move your app forward with the Uber API", spec.description);
            Assert.assertEquals("1.0.0", spec.version);
            Assert.assertEquals(5, spec.resources.size());
            Assert.assertEquals(6, spec.definitions.size());
        }
    }

    @Test
    public void testGenerateSpecificationHerokuPets() throws IOException, SwaggerException {
        File swaggerFile = new File(getClass().getResource("/heroku-pets.api").getPath());
        try (FileInputStream in = new FileInputStream(swaggerFile)) {
            ApiSpecification spec = tested.generateSpecification(in);
            Assert.assertEquals("PetStore on Heroku", spec.title);
            Assert.assertEquals("Must be filled", spec.description);
            Assert.assertEquals("1.0.0", spec.version);
            Assert.assertEquals(4, spec.resources.size());
            Assert.assertEquals(1, spec.definitions.size());
        }
    }

}
