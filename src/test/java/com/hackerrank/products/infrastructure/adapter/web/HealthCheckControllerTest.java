package com.hackerrank.products.infrastructure.adapter.web;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.v3.oas.annotations.Operation;

class HealthCheckControllerTest {

    @Test
    void checkShouldReturnTrue() {
        HealthCheckController controller = new HealthCheckController();
        Boolean response = controller.check();

        assertEquals(Boolean.TRUE, response);
    }

    @Test
    void checkShouldReturnNonNullValue() {
        HealthCheckController controller = new HealthCheckController();
        Boolean response = controller.check();

        assertNotNull(response);
        assertTrue(response);
    }

    @Test
    void classShouldHaveExpectedRequestMapping() {
        RequestMapping requestMapping = HealthCheckController.class.getAnnotation(RequestMapping.class);

        assertNotNull(requestMapping);
        assertArrayEquals(new String[] { "api/v1/products" }, requestMapping.value());
    }

    @Test
    void checkMethodShouldExposeExpectedEndpointAndMetadata() throws NoSuchMethodException {
        Method method = HealthCheckController.class.getDeclaredMethod("check");

        GetMapping getMapping = method.getAnnotation(GetMapping.class);
        ResponseStatus responseStatus = method.getAnnotation(ResponseStatus.class);
        Operation operation = method.getAnnotation(Operation.class);

        assertNotNull(getMapping);
        assertArrayEquals(new String[] { "/health" }, getMapping.path());
        assertArrayEquals(new String[] { MediaType.APPLICATION_JSON_VALUE }, getMapping.produces());

        assertNotNull(responseStatus);
        assertEquals(HttpStatus.OK, responseStatus.value());

        assertNotNull(operation);
        assertEquals("Health Check", operation.summary());
        assertEquals("Check if the service is running properly.", operation.description());
    }

}
