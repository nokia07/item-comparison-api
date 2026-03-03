package com.hackerrank.products.infrastructure.adapter.persistence;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hackerrank.products.application.domain.model.Product;

@ExtendWith(MockitoExtension.class)
class ProductDataRepositoryAdapterTest {

    @Mock
    private ProductDataRepository productRepository;

    @Mock
    private ProductDataMapper productDataMapper;

    @Test
    void findByIdInShouldMapAllRepositoryResults() {
        ProductDataRepositoryAdapter adapter = new ProductDataRepositoryAdapter(productRepository, productDataMapper);
        List<Long> ids = List.of(1L, 2L);
        ProductData data1 = new ProductData();
        data1.setId(1L);
        ProductData data2 = new ProductData();
        data2.setId(2L);
        Product product1 = Product.builder().id(1L).name("A").build();
        Product product2 = Product.builder().id(2L).name("B").build();

        when(productRepository.findByIdIn(ids)).thenReturn(List.of(data1, data2));
        when(productDataMapper.toDomain(data1)).thenReturn(product1);
        when(productDataMapper.toDomain(data2)).thenReturn(product2);

        List<Product> result = adapter.findByIdIn(ids);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productRepository).findByIdIn(ids);
        verify(productDataMapper).toDomain(data1);
        verify(productDataMapper).toDomain(data2);
    }

    @Test
    void findByIdInShouldReturnEmptyListWhenRepositoryReturnsEmpty() {
        ProductDataRepositoryAdapter adapter = new ProductDataRepositoryAdapter(productRepository, productDataMapper);
        List<Long> ids = List.of(99L);
        when(productRepository.findByIdIn(ids)).thenReturn(List.of());

        List<Product> result = adapter.findByIdIn(ids);

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(productRepository).findByIdIn(ids);
        verifyNoInteractions(productDataMapper);
    }

    @Test
    void findByCategoryShouldMapAllRepositoryResults() {
        ProductDataRepositoryAdapter adapter = new ProductDataRepositoryAdapter(productRepository, productDataMapper);
        String category = "electronics";
        ProductData data1 = new ProductData();
        data1.setId(10L);
        ProductData data2 = new ProductData();
        data2.setId(11L);
        Product product1 = Product.builder().id(10L).name("Phone").build();
        Product product2 = Product.builder().id(11L).name("Laptop").build();

        when(productRepository.findByCategory(category)).thenReturn(List.of(data1, data2));
        when(productDataMapper.toDomain(data1)).thenReturn(product1);
        when(productDataMapper.toDomain(data2)).thenReturn(product2);

        List<Product> result = adapter.findByCategory(category);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productRepository).findByCategory(category);
        verify(productDataMapper).toDomain(data1);
        verify(productDataMapper).toDomain(data2);
    }

    @Test
    void findByCategoryShouldReturnEmptyListWhenRepositoryReturnsEmpty() {
        ProductDataRepositoryAdapter adapter = new ProductDataRepositoryAdapter(productRepository, productDataMapper);
        String category = "unknown";
        when(productRepository.findByCategory(category)).thenReturn(List.of());

        List<Product> result = adapter.findByCategory(category);

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(productRepository).findByCategory(category);
        verifyNoInteractions(productDataMapper);
    }
}
