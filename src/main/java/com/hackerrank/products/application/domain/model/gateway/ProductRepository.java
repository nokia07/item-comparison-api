package com.hackerrank.products.application.domain.model.gateway;

import java.util.List;

import com.hackerrank.products.application.domain.model.Product;

public interface ProductRepository {

    List<Product> findByIdIn(List<Long> productIds);
    List<Product> findByCategory(String category);
    
}
