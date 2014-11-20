package com.livingobjects.myrddin;

import com.livingobjects.myrddin.exception.SwaggerException;
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

            Assert.assertEquals(1, spec.resources.size());
            ApiResource apiResource = spec.resources.get(0);
            Assert.assertEquals("/test/domains", apiResource.uri);
            Assert.assertEquals("GET", apiResource.method);
            Assert.assertEquals("privateAuthentication", apiResource.security.get(0).securityScheme);

            Assert.assertEquals(2, spec.models.size());
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
            Assert.assertEquals(6, spec.models.size());
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
            Assert.assertEquals(1, spec.models.size());
        }
    }

}
