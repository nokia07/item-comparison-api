package com.hackerrank.products.infrastructure.adapter.persistence;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "products")
@NoArgsConstructor
public class ProductData {
    @Id
    private Long id;

    private String name;
    private String description;
    private BigDecimal price;
    private String size;
    private Double weight;
    private String color;
    private String category;

    @Column(columnDefinition = "CLOB")
    private String attributesJson;
}
