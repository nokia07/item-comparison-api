package com.hackerrank.products.application.domain.usecase;

import java.util.List;

import com.hackerrank.products.application.domain.model.Product;
import com.hackerrank.products.application.domain.model.exception.ProductNotFoundException;
import com.hackerrank.products.application.domain.model.gateway.ProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductUseCase {

    private final ProductRepository productRepository;    

    public List<Product> getProductsByIds(List<Long> productIds) {
        List<Product> products = productRepository.findByIdIn(productIds);
        if (products.isEmpty()) {
            throw new ProductNotFoundException("" + productIds);
        }
        return products;
    }

    public List<Product> getProductsByCategory(String category) {
        List<Product> products = productRepository.findByCategory(category);
        if (products.isEmpty()) {
            throw new ProductNotFoundException("No products found for category: " + category);
        }
        return products;
    }

}
