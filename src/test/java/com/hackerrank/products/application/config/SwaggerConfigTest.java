package com.hackerrank.products.application.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

class SwaggerConfigTest {

    @Test
    void classShouldHaveConfigurationAnnotation() {
        Configuration annotation = SwaggerConfig.class.getAnnotation(Configuration.class);

        assertNotNull(annotation);
    }

    @Test
    void classShouldHaveOpenAPIDefinitionAnnotation() {
        OpenAPIDefinition annotation = SwaggerConfig.class.getAnnotation(OpenAPIDefinition.class);

        assertNotNull(annotation);
    }

    @Test
    void openAPIInfoShouldHaveExpectedTitle() {
        OpenAPIDefinition annotation = SwaggerConfig.class.getAnnotation(OpenAPIDefinition.class);

        assertNotNull(annotation.info());
        assertEquals("Product Management APIs", annotation.info().title());
    }

    @Test
    void openAPIInfoShouldHaveExpectedVersion() {
        OpenAPIDefinition annotation = SwaggerConfig.class.getAnnotation(OpenAPIDefinition.class);

        assertNotNull(annotation.info());
        assertEquals("1.0.0", annotation.info().version());
    }

    @Test
    void openAPIInfoShouldHaveExpectedDescription() {
        OpenAPIDefinition annotation = SwaggerConfig.class.getAnnotation(OpenAPIDefinition.class);

        assertNotNull(annotation.info());
        assertEquals("Microservices for Product and Inventory Management", 
                   annotation.info().description());
    }

    @Test
    void openAPIInfoShouldHaveExpectedTermsOfService() {
        OpenAPIDefinition annotation = SwaggerConfig.class.getAnnotation(OpenAPIDefinition.class);

        assertNotNull(annotation.info());
        assertEquals("Terms and conditions applied", annotation.info().termsOfService());
    }

    @Test
    void openAPIInfoContactShouldHaveExpectedValues() {
        OpenAPIDefinition annotation = SwaggerConfig.class.getAnnotation(OpenAPIDefinition.class);
        Contact contact = annotation.info().contact();

        assertNotNull(contact);
        assertEquals("Piyush Garg", contact.name());
        assertEquals("piyushgarglive@gmail.com", contact.email());
        assertEquals("piyush-garg.com", contact.url());
    }

    @Test
    void openAPIInfoLicenseShouldHaveExpectedValues() {
        OpenAPIDefinition annotation = SwaggerConfig.class.getAnnotation(OpenAPIDefinition.class);
        License license = annotation.info().license();

        assertNotNull(license);
        assertEquals("Piyush License", license.name());
    }

    @Test
    void openAPIDefinitionShouldConfigureMultipleServers() {
        OpenAPIDefinition annotation = SwaggerConfig.class.getAnnotation(OpenAPIDefinition.class);
        Server[] servers = annotation.servers();

        assertNotNull(servers);
        assertEquals(2, servers.length);
    }

    @Test
    void openAPIServersShouldHaveExpectedDescriptionsAndUrls() {
        OpenAPIDefinition annotation = SwaggerConfig.class.getAnnotation(OpenAPIDefinition.class);
        Server[] servers = annotation.servers();

        assertEquals("devServer", servers[0].description());
        assertEquals("http://localhost:8080", servers[0].url());

        assertEquals("testServer", servers[1].description());
        assertEquals("http://localhost:8080", servers[1].url());
    }

    @Test
    void openAPIDefinitionShouldRequireBearerAuthSecurity() {
        OpenAPIDefinition annotation = SwaggerConfig.class.getAnnotation(OpenAPIDefinition.class);
        SecurityRequirement[] securityRequirements = annotation.security();

        assertNotNull(securityRequirements);
        assertEquals(1, securityRequirements.length);
        assertEquals("bearerAuth", securityRequirements[0].name());
    }

    @Test
    void classShouldHaveSecuritySchemeAnnotation() {
        SecurityScheme annotation = SwaggerConfig.class.getAnnotation(SecurityScheme.class);

        assertNotNull(annotation);
    }

    @Test
    void securitySchemeShouldConfigureBearerAuth() {
        SecurityScheme annotation = SwaggerConfig.class.getAnnotation(SecurityScheme.class);

        assertEquals("bearerAuth", annotation.name());
        assertEquals("bearer", annotation.scheme());
        assertEquals(SecuritySchemeType.HTTP, annotation.type());
        assertEquals("JWT Bearer authentication", annotation.description());
        assertEquals("JWT", annotation.bearerFormat());
    }
}
