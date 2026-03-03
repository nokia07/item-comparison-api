package com.hackerrank.products.infrastructure.adapter.web.dto;

import java.util.Map;

public record ProductDetailDTO(
        Long id,
        String name,
        String description,
        Map<String, Object> fields) {
}
