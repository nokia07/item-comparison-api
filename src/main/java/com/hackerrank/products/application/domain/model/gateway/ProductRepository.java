package com.hackerrank.products.application.domain.model.gateway;

import java.util.List;

import com.hackerrank.products.application.domain.model.Product;

public interface ProductRepository {

    List<Product> compare(List<Long> productIds);
    
}
