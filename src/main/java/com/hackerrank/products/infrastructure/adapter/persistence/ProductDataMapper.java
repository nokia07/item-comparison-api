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
        Product product = new Product();
        product.setId(data.getId());
        product.setName(data.getName());
        product.setDescription(data.getDescription());
        product.setPrice(data.getPrice());
        product.setSize(data.getSize());
        product.setWeight(data.getWeight());
        product.setColor(data.getColor());
        product.setCategory(data.getCategory());
        product.setAttributes(parseAttributes(data.getAttributesJson()));
        return product;
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