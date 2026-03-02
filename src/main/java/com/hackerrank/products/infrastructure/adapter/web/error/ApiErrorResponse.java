package com.hackerrank.products.infrastructure.adapter.web.error;

import java.time.Instant;

public record ApiErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path) {
}