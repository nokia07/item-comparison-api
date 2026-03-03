package com.hackerrank.products.infrastructure.adapter.web;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

class HomeControllerTest {

    @Test
    void homeShouldReturnExpectedWelcomeMessage() {
    HomeController controller = new HomeController();
        String response = controller.home();

        assertEquals("Bienvenido al Product Comparison API", response);
    }

    @Test
    void homeShouldReturnNonNullAndNonBlankMessage() {
        HomeController controller = new HomeController();
        String response = controller.home();

        assertNotNull(response);
        assertTrue(!response.isBlank());
    }

    @Test
    void homeMethodShouldBeMappedToRootPathWithPlainTextResponse() throws NoSuchMethodException {
        Method method = HomeController.class.getDeclaredMethod("home");
        GetMapping getMapping = method.getAnnotation(GetMapping.class);

        assertNotNull(getMapping);
        assertArrayEquals(new String[] { "/" }, getMapping.path());
        assertArrayEquals(new String[] { MediaType.TEXT_PLAIN_VALUE }, getMapping.produces());
    }
}
