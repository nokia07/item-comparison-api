package com.hackerrank.products.application.domain.usecase;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hackerrank.products.application.domain.model.Product;
import com.hackerrank.products.application.domain.model.exception.ProductNotFoundException;
import com.hackerrank.products.application.domain.model.gateway.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    private ProductUseCase productUseCase;

    private Product smartphone1;
    private Product smartphone2;
    private Product car1;

    @BeforeEach
    void setUp() {
        productUseCase = new ProductUseCase(productRepository);

        smartphone1 = Product.builder()
                .id(1L)
                .name("Galaxy S24")
                .description("Flagship Samsung phone")
                .price(new BigDecimal("999.00"))
                .category("SMARTPHONE")
                .attributes(Map.of(
                        "batteryCapacity", 4500,
                        "memory", 8,
                        "brand", "Samsung"
                ))
                .build();

        smartphone2 = Product.builder()
                .id(4L)
                .name("iPhone 15")
                .description("Apple smartphone")
                .price(new BigDecimal("899.00"))
                .category("SMARTPHONE")
                .attributes(Map.of(
                        "batteryCapacity", 3349,
                        "memory", 6,
                        "brand", "Apple"
                ))
                .build();

        car1 = Product.builder()
                .id(2L)
                .name("Toyota Corolla")
                .description("Sedan 2024")
                .price(new BigDecimal("25000.00"))
                .category("CAR")
                .attributes(Map.of(
                        "engine", "2.0L",
                        "horsepower", 169,
                        "doors", 4
                ))
                .build();
    }

    // ─────────────────────────────────────────────
    // getProductsByIds
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("should return products when all ids exist")
    void shouldReturnProductsWhenIdsExist() {
        List<Long> ids = List.of(1L, 4L);
        when(productRepository.findByIdIn(ids)).thenReturn(List.of(smartphone1, smartphone2));

        List<Product> result = productUseCase.getProductsByIds(ids);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Product::getId).containsExactlyInAnyOrder(1L, 4L);
        assertThat(result).extracting(Product::getName)
                .containsExactlyInAnyOrder("Galaxy S24", "iPhone 15");
    }

    @Test
    @DisplayName("should return single product when one id is provided")
    void shouldReturnSingleProductWhenOneIdProvided() {
        List<Long> ids = List.of(1L);
        when(productRepository.findByIdIn(ids)).thenReturn(List.of(smartphone1));

        List<Product> result = productUseCase.getProductsByIds(ids);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getName()).isEqualTo("Galaxy S24");
    }

    @Test
    @DisplayName("should throw ProductNotFoundException when no products found for given ids")
    void shouldThrowProductNotFoundExceptionWhenNoProductsFound() {
        List<Long> ids = List.of(999L, 1000L);
        when(productRepository.findByIdIn(ids)).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> productUseCase.getProductsByIds(ids))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining(ids.toString());
    }

    @Test
    @DisplayName("should throw ProductNotFoundException when ids list returns empty")
    void shouldThrowProductNotFoundExceptionWhenRepositoryReturnsEmpty() {
        List<Long> ids = List.of(0L);
        when(productRepository.findByIdIn(ids)).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> productUseCase.getProductsByIds(ids))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @DisplayName("should return products with their attributes")
    void shouldReturnProductsWithAttributes() {
        List<Long> ids = List.of(2L);
        when(productRepository.findByIdIn(ids)).thenReturn(List.of(car1));

        List<Product> result = productUseCase.getProductsByIds(ids);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAttributes())
                .containsEntry("engine", "2.0L")
                .containsEntry("horsepower", 169)
                .containsEntry("doors", 4);
    }

    // ─────────────────────────────────────────────
    // getProductsByCategory
    // ─────────────────────────────────────────────
    @Test
    @DisplayName("should return products when category exists")
    void shouldReturnProductsWhenCategoryExists() {
        when(productRepository.findByCategory("SMARTPHONE"))
                .thenReturn(List.of(smartphone1, smartphone2));

        List<Product> result = productUseCase.getProductsByCategory("SMARTPHONE");

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Product::getCategory)
                .containsOnly("SMARTPHONE");
    }

    @Test
    @DisplayName("should return single product when category has one product")
    void shouldReturnSingleProductWhenCategoryHasOneProduct() {
        when(productRepository.findByCategory("CAR")).thenReturn(List.of(car1));

        List<Product> result = productUseCase.getProductsByCategory("CAR");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Toyota Corolla");
        assertThat(result.get(0).getCategory()).isEqualTo("CAR");
    }

    @Test
    @DisplayName("should throw ProductNotFoundException when category has no products")
    void shouldThrowProductNotFoundExceptionWhenCategoryEmpty() {
        when(productRepository.findByCategory("UNKNOWN"))
                .thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> productUseCase.getProductsByCategory("UNKNOWN"))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("No products found for category: UNKNOWN");
    }

    @Test
    @DisplayName("should throw ProductNotFoundException when category name does not match any product")
    void shouldThrowProductNotFoundExceptionWhenCategoryDoesNotMatch() {
        when(productRepository.findByCategory("TABLET"))
                .thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> productUseCase.getProductsByCategory("TABLET"))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @DisplayName("should return products with attributes when category found")
    void shouldReturnProductsWithAttributesWhenCategoryFound() {
        when(productRepository.findByCategory("SMARTPHONE"))
                .thenReturn(List.of(smartphone1));

        List<Product> result = productUseCase.getProductsByCategory("SMARTPHONE");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAttributes())
                .containsEntry("brand", "Samsung")
                .containsEntry("batteryCapacity", 4500)
                .containsEntry("memory", 8);
    }

    @Test
    @DisplayName("should return all products regardless of their attributes structure")
    void shouldReturnAllProductsFromCategory() {
        when(productRepository.findByCategory("CAR")).thenReturn(List.of(car1));

        List<Product> result = productUseCase.getProductsByCategory("CAR");

        assertThat(result).isNotEmpty();
        assertThat(result).extracting(Product::getId).containsExactly(2L);
    }
}