package com.hackerrank.products.infrastructure.adapter.persistence;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackerrank.products.application.domain.model.Product;

class ProductDataMapperTest {

    @Test
    void toDomainShouldMapAllFieldsCorrectly() {
        ProductDataMapper mapper = new ProductDataMapper(new ObjectMapper());
        ProductData data = new ProductData();
        data.setId(1L);
        data.setName("Laptop");
        data.setDescription("High performance laptop");
        data.setPrice(new BigDecimal("999.99"));
        data.setSize("15 inch");
        data.setWeight(2.5);
        data.setColor("Silver");
        data.setCategory("Electronics");
        data.setAttributesJson("{\"brand\":\"Dell\",\"ram\":16}");

        Product result = mapper.toDomain(data);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Laptop", result.getName());
        assertEquals("High performance laptop", result.getDescription());
        assertEquals(new BigDecimal("999.99"), result.getPrice());
        assertEquals("15 inch", result.getSize());
        assertEquals(2.5, result.getWeight());
        assertEquals("Silver", result.getColor());
        assertEquals("Electronics", result.getCategory());
        assertNotNull(result.getAttributes());
        assertEquals(2, result.getAttributes().size());
        assertEquals("Dell", result.getAttributes().get("brand"));
        assertEquals(16, result.getAttributes().get("ram"));
    }

    @Test
    void toDomainWithNullAttributesJsonShouldReturnEmptyMap() {
        ProductDataMapper mapper = new ProductDataMapper(new ObjectMapper());
        ProductData data = new ProductData();
        data.setId(2L);
        data.setName("Mouse");
        data.setAttributesJson(null);

        Product result = mapper.toDomain(data);

        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("Mouse", result.getName());
        assertNotNull(result.getAttributes());
        assertTrue(result.getAttributes().isEmpty());
    }

    @Test
    void toDomainWithBlankAttributesJsonShouldReturnEmptyMap() {
        ProductDataMapper mapper = new ProductDataMapper(new ObjectMapper());
        ProductData data = new ProductData();
        data.setId(3L);
        data.setName("Keyboard");
        data.setAttributesJson("   ");

        Product result = mapper.toDomain(data);

        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("Keyboard", result.getName());
        assertNotNull(result.getAttributes());
        assertTrue(result.getAttributes().isEmpty());
    }

    @Test
    void toDomainWithEmptyAttributesJsonShouldReturnEmptyMap() {
        ProductDataMapper mapper = new ProductDataMapper(new ObjectMapper());
        ProductData data = new ProductData();
        data.setId(4L);
        data.setName("Monitor");
        data.setAttributesJson("");

        Product result = mapper.toDomain(data);

        assertNotNull(result);
        assertEquals(4L, result.getId());
        assertEquals("Monitor", result.getName());
        assertNotNull(result.getAttributes());
        assertTrue(result.getAttributes().isEmpty());
    }

    @Test
    void toDomainWithInvalidJsonShouldReturnEmptyMap() {
        ProductDataMapper mapper = new ProductDataMapper(new ObjectMapper());
        ProductData data = new ProductData();
        data.setId(5L);
        data.setName("Headphones");
        data.setAttributesJson("{invalid json syntax");

        Product result = mapper.toDomain(data);

        assertNotNull(result);
        assertEquals(5L, result.getId());
        assertEquals("Headphones", result.getName());
        assertNotNull(result.getAttributes());
        assertTrue(result.getAttributes().isEmpty());
    }

    @Test
    void toDomainWithComplexValidJsonShouldParseCorrectly() {
        ProductDataMapper mapper = new ProductDataMapper(new ObjectMapper());
        ProductData data = new ProductData();
        data.setId(6L);
        data.setName("Smartphone");
        data.setAttributesJson("{\"brand\":\"Samsung\",\"model\":\"Galaxy\",\"storage\":128,\"features\":{}}");

        Product result = mapper.toDomain(data);

        assertNotNull(result);
        assertEquals(6L, result.getId());
        assertEquals("Smartphone", result.getName());
        assertNotNull(result.getAttributes());
        assertEquals(4, result.getAttributes().size());
        assertEquals("Samsung", result.getAttributes().get("brand"));
        assertEquals("Galaxy", result.getAttributes().get("model"));
        assertEquals(128, result.getAttributes().get("storage"));
        assertTrue(result.getAttributes().get("features") instanceof Map);
    }
}
