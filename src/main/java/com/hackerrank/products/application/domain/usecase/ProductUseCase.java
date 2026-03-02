package com.hackerrank.products.application.domain.usecase;

import java.util.List;

import com.hackerrank.products.application.domain.model.Product;
import com.hackerrank.products.application.domain.model.exception.ProductNotFoundException;
import com.hackerrank.products.application.domain.model.gateway.ProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductUseCase {

    private final ProductRepository productRepository;    

    public List<Product> compareProducts(List<Long> productIds) {
        List<Product> products = productRepository.compare(productIds);
        if (products.isEmpty()) {
            throw new ProductNotFoundException("" + productIds);
        }
        return products;
    }

}
