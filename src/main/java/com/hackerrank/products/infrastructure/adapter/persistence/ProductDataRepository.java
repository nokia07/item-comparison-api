package com.hackerrank.products.infrastructure.adapter.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDataRepository extends JpaRepository<ProductData, Long> {
    List<ProductData> findByIdIn(List<Long> ids);
}
