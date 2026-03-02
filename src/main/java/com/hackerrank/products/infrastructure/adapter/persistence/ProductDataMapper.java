package com.hackerrank.products.infrastructure.adapter.persistence;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackerrank.products.application.domain.model.Product;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductDataMapper {

    private final ObjectMapper objectMapper;

    public Product toDomain(ProductData data) {
        return Product.builder()
                .id(data.getId())
                .name(data.getName())
                .description(data.getDescription())
                .price(data.getPrice())
                .size(data.getSize())
                .weight(data.getWeight())
                .color(data.getColor())
                .category(data.getCategory())
                .attributes(parseAttributes(data.getAttributesJson()))
                .build();
    }

    private Map<String, Object> parseAttributes(String attributesJson) {
        if (attributesJson == null || attributesJson.isBlank()) {
            return Map.of();
        }

        try {
            return objectMapper.readValue(attributesJson, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException exception) {
            return Map.of();
        }
    }
}