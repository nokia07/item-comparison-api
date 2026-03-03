package com.hackerrank.products.infrastructure.adapter.web.error;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.hackerrank.products.application.domain.model.exception.ProductNotFoundException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        request = new MockHttpServletRequest();
        request.setRequestURI("/test/endpoint");
    }

    @Test
    void handleBadRequestWithIllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");
        ResponseEntity<ApiErrorResponse> response = handler.handleBadRequest(exception, request);

        assertErrorResponse(response, HttpStatus.BAD_REQUEST, "Invalid argument");
    }

    @Test
    void handleBadRequestWithMissingServletRequestParameterException() {
        MissingServletRequestParameterException exception = new MissingServletRequestParameterException("id", "long");
        ResponseEntity<ApiErrorResponse> response = handler.handleBadRequest(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().message().contains("Required request parameter"));
        assertCommonFields(response.getBody(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void handleBadRequestWithMethodArgumentTypeMismatchException() {
        MethodArgumentTypeMismatchException exception = new MethodArgumentTypeMismatchException("abc", Integer.class, "id", null, null);
        ResponseEntity<ApiErrorResponse> response = handler.handleBadRequest(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().message());
        assertCommonFields(response.getBody(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void handleProductNotFoundError() {
        ProductNotFoundException exception = new ProductNotFoundException("10");

        ResponseEntity<ApiErrorResponse> response = handler.handleProductNotFoundError(exception, request);

        assertErrorResponse(response, HttpStatus.NOT_FOUND, "Product with ID's 10 not found");
    }

    @Test
    void handleValidationWithConstraintViolations() {
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("id must be positive");
        ConstraintViolationException exception = new ConstraintViolationException(Set.of(violation));

        ResponseEntity<ApiErrorResponse> response = handler.handleValidation(exception, request);

        assertErrorResponse(response, HttpStatus.BAD_REQUEST, "id must be positive");
    }

    @Test
    void handleValidationWithoutConstraintViolationsUsesFallbackMessage() {
        ConstraintViolationException exception = new ConstraintViolationException(Collections.emptySet());

        ResponseEntity<ApiErrorResponse> response = handler.handleValidation(exception, request);

        assertErrorResponse(response, HttpStatus.BAD_REQUEST, "Invalid request parameters");
    }

    @Test
    void handleMethodValidationWithoutErrorsUsesFallbackMessage() {
        HandlerMethodValidationException exception = mock(HandlerMethodValidationException.class);
        when(exception.getAllErrors()).thenReturn(Collections.emptyList());

        ResponseEntity<ApiErrorResponse> response = handler.handleMethodValidation(exception, request);

        assertErrorResponse(response, HttpStatus.BAD_REQUEST, "Invalid request parameters");
    }

    @Test
    void handleUnexpectedError() {
        Exception exception = new RuntimeException("boom");

        ResponseEntity<ApiErrorResponse> response = handler.handleUnexpectedError(exception, request);

        assertErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
    }

    private void assertErrorResponse(ResponseEntity<ApiErrorResponse> response, HttpStatus status, String message) {
        assertEquals(status, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(message, response.getBody().message());
        assertCommonFields(response.getBody(), status);
    }

    private void assertCommonFields(ApiErrorResponse body, HttpStatus status) {
        assertNotNull(body.timestamp());
        assertEquals(status.value(), body.status());
        assertEquals(status.getReasonPhrase(), body.error());
        assertEquals("/test/endpoint", body.path());
    }
}