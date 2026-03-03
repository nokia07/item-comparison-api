package com.hackerrank.products;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.mockStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

class ApplicationTests {

    @Test
    void classShouldHaveSpringBootApplicationAnnotation() {
        SpringBootApplication annotation = Application.class.getAnnotation(SpringBootApplication.class);

        assertNotNull(annotation);
    }

    @Test
    void mainMethodShouldExistAndBePublicStatic() throws NoSuchMethodException {
        Method mainMethod = Application.class.getDeclaredMethod("main", String[].class);

        assertNotNull(mainMethod);
        assertTrue(Modifier.isPublic(mainMethod.getModifiers()));
        assertTrue(Modifier.isStatic(mainMethod.getModifiers()));
    }

    @Test
    void mainMethodShouldInvokeSpringApplicationRun() {
        try (MockedStatic<SpringApplication> mockedSpringApp = mockStatic(SpringApplication.class)) {
            String[] args = new String[] {};

            assertDoesNotThrow(() -> Application.main(args));

            mockedSpringApp.verify(() -> SpringApplication.run(eq(Application.class), any(String[].class)));
        }
    }

    @Test
    void mainMethodShouldPassArgumentsToSpringApplicationRun() {
        try (MockedStatic<SpringApplication> mockedSpringApp = mockStatic(SpringApplication.class)) {
            String[] args = new String[] { "--server.port=8081", "--spring.profiles.active=test" };

            assertDoesNotThrow(() -> Application.main(args));

            mockedSpringApp.verify(() -> SpringApplication.run(Application.class, args));
        }
    }
}
