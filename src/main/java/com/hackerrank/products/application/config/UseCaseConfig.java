package com.hackerrank.products.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hackerrank.products.application.domain.model.gateway.ProductRepository;
import com.hackerrank.products.application.domain.usecase.ProductUseCase;

@Configuration
public class UseCaseConfig {

    @Bean
    public ProductUseCase productUseCase(ProductRepository productRepository) {
        return new ProductUseCase(productRepository);
    }
}