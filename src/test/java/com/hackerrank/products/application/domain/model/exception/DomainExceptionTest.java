package com.hackerrank.products.application.domain.model.exception;

import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class DomainExceptionTest {

    @Test
    void classShouldBeAbstract() {
        assertTrue(Modifier.isAbstract(DomainException.class.getModifiers()));
    }

    @Test
    void shouldExtendRuntimeException() {
        assertTrue(RuntimeException.class.isAssignableFrom(DomainException.class));
    }

    @Test
    void shouldCreateConcreteInstanceWithCorrectTitle() {
        DomainException exception = new ProductNotFoundException("123");

        assertNotNull(exception);
        assertEquals("Product Not Found", exception.getTitle());
    }

    @Test
    void shouldCreateConcreteInstanceWithCorrectMessage() {
        DomainException exception = new ProductNotFoundException("456");

        assertNotNull(exception);
        assertEquals("Product with ID's 456 not found", exception.getMessage());
    }

    @Test
    void getTitleShouldReturnTitlePassedToConstructor() {
        DomainException exception = new ProductNotFoundException("789");

        String title = exception.getTitle();

        assertNotNull(title);
        assertEquals("Product Not Found", title);
    }

    @Test
    void getMessageShouldReturnMessagePassedToConstructor() {
        String productId = "999";
        DomainException exception = new ProductNotFoundException(productId);

        String message = exception.getMessage();

        assertNotNull(message);
        assertTrue(message.contains(productId));
        assertEquals("Product with ID's 999 not found", message);
    }

    @Test
    void shouldBeInstanceOfRuntimeException() {
        DomainException exception = new ProductNotFoundException("111");

        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void shouldBeThrownAndCaughtAsRuntimeException() {
        RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
            throw new ProductNotFoundException("222");
        });

        assertNotNull(thrownException);
        assertTrue(thrownException instanceof DomainException);
    }

    @Test
    void shouldBeThrownAndCaughtAsDomainException() {
        DomainException thrownException = assertThrows(DomainException.class, () -> {
            throw new ProductNotFoundException("333");
        });

        assertNotNull(thrownException);
        assertEquals("Product Not Found", thrownException.getTitle());
        assertEquals("Product with ID's 333 not found", thrownException.getMessage());
    }

    @Test
    void shouldAccessPrivateTitleFieldUsingReflectionTestUtils() {
        DomainException exception = new ProductNotFoundException("444");

        Object titleValue = ReflectionTestUtils.getField(exception, "title");

        assertNotNull(titleValue);
        assertEquals("Product Not Found", titleValue);
    }

    @Test
    void shouldModifyPrivateTitleFieldUsingReflectionTestUtils() {
        DomainException exception = new ProductNotFoundException("555");
        String newTitle = "Custom Title";

        ReflectionTestUtils.setField(exception, "title", newTitle);
        String modifiedTitle = exception.getTitle();

        assertEquals(newTitle, modifiedTitle);
    }

    @Test
    void multipleInstancesShouldHaveIndependentTitles() {
        DomainException exception1 = new ProductNotFoundException("666");
        DomainException exception2 = new ProductNotFoundException("777");

        assertEquals("Product Not Found", exception1.getTitle());
        assertEquals("Product Not Found", exception2.getTitle());
        
        ReflectionTestUtils.setField(exception1, "title", "Modified Title");
        
        assertEquals("Modified Title", exception1.getTitle());
        assertEquals("Product Not Found", exception2.getTitle());
    }
}
