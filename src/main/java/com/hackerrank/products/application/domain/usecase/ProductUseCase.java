package com.hackerrank.products.application.domain.usecase;

import java.util.List;

import com.hackerrank.products.application.domain.model.Product;
import com.hackerrank.products.application.domain.model.gateway.ProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductUseCase {

    private final ProductRepository productRepository;    

    public List<Product> compareProducts(List<Long> productIds) {
        return productRepository.compare(productIds);
    }

}
