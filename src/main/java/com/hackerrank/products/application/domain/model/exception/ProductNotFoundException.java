package com.hackerrank.products.application.domain.model.exception;

public class ProductNotFoundException extends DomainException {
    public ProductNotFoundException(String id) {
        super("Product Not Found", "Product with ID's " + id + " not found");
    }
    
}
