package com.hackerrank.products.infrastructure.adapter.persistence;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hackerrank.products.application.domain.model.Product;
import com.hackerrank.products.application.domain.model.gateway.ProductRepository;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class ProductDataRepositoryAdapter implements ProductRepository {
    
    private final ProductDataRepository productRepository;
    private final ProductDataMapper productDataMapper;

    @Override
    public List<Product> findByIdIn(List<Long> productIds) {
        List<ProductData> productDataList = productRepository.findByIdIn(productIds);
        return productDataList.stream()
                .map(productDataMapper::toDomain)
                .toList();
    }

    @Override
    public List<Product> findByCategory(String category) {
        return productRepository.findByCategory(category).stream()
                .map(productDataMapper::toDomain)
                .toList();
    }

}
