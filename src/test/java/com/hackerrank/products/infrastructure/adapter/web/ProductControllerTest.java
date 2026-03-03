package com.hackerrank.products.infrastructure.adapter.web;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hackerrank.products.application.domain.model.Product;
import com.hackerrank.products.application.domain.model.exception.ProductNotFoundException;
import com.hackerrank.products.application.domain.usecase.ProductUseCase;
import com.hackerrank.products.infrastructure.adapter.web.dto.ProductDetailDTO;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductUseCase productUseCase;

    private ProductController productController;

    // ─── Fixtures ────────────────────────────────────────────────────────────────

    private Product smartphone1;
    private Product smartphone2;
    private Product car1;
    private Product motorcycle1;
    private Product productNoAttributes;

    @BeforeEach
    void setUp() {
        productController = new ProductController(productUseCase);

        // ReflectionTestUtils.setField(productController, "somePrivateField", value);
        // No hay campos privados fuera del constructor actualmente,
        // el patrón queda disponible para futuras expansiones.

        smartphone1 = Product.builder()
                .id(1L)
                .name("Galaxy S24")
                .description("Flagship Samsung phone")
                .price(new BigDecimal("999.00"))
                .size("6.5 inches")
                .weight(0.19)
                .color("Black")
                .category("SMARTPHONE")
                .attributes(Map.of(
                        "batteryCapacity", 4500,
                        "memory", 8,
                        "brand", "Samsung",
                        "operatingSystem", "Android"
                ))
                .build();

        smartphone2 = Product.builder()
                .id(4L)
                .name("iPhone 15")
                .description("Apple smartphone")
                .price(new BigDecimal("899.00"))
                .size("6.1 inches")
                .weight(0.171)
                .color("Pink")
                .category("SMARTPHONE")
                .attributes(Map.of(
                        "batteryCapacity", 3349,
                        "memory", 6,
                        "brand", "Apple",
                        "operatingSystem", "iOS"
                ))
                .build();

        car1 = Product.builder()
                .id(2L)
                .name("Toyota Corolla")
                .description("Sedan 2024")
                .price(new BigDecimal("25000.00"))
                .size("Medium")
                .weight(1300.0)
                .color("White")
                .category("CAR")
                .attributes(Map.of(
                        "engine", "2.0L",
                        "horsepower", 169,
                        "doors", 4,
                        "fuelType", "Gasoline"
                ))
                .build();

        motorcycle1 = Product.builder()
                .id(3L)
                .name("Yamaha MT-07")
                .description("Naked bike")
                .price(new BigDecimal("9500.00"))
                .size("Standard")
                .weight(184.0)
                .color("Blue")
                .category("MOTORCYCLE")
                .attributes(Map.of(
                        "engine", "689cc",
                        "horsepower", 74,
                        "type", "Sport",
                        "cooling", "Liquid"
                ))
                .build();

        productNoAttributes = Product.builder()
                .id(99L)
                .name("No Attrs Product")
                .description("Product without attributes")
                .price(new BigDecimal("100.00"))
                .size("Small")
                .weight(0.5)
                .color("White")
                .category("OTHER")
                .attributes(null)
                .build();
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // compareByIds
    // ─────────────────────────────────────────────────────────────────────────────
    @Nested
    @DisplayName("GET /compare — compareByIds")
    class CompareByIds {

        @Test
        @DisplayName("should return 200 with full attributes when fields is null")
        void shouldReturn200WithFullAttributesWhenFieldsIsNull() {
            List<Long> ids = List.of(1L, 4L);
            when(productUseCase.getProductsByIds(ids)).thenReturn(List.of(smartphone1, smartphone2));

            ResponseEntity<List<ProductDetailDTO>> response = productController.compareByIds(ids, null);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).hasSize(2);

            ProductDetailDTO first = response.getBody().get(0);
            assertThat(first.id()).isEqualTo(1L);
            assertThat(first.name()).isEqualTo("Galaxy S24");
            assertThat(first.description()).isEqualTo("Flagship Samsung phone");
            assertThat(first.fields())
                    .containsKey("batteryCapacity")
                    .containsKey("brand")
                    .containsKey("memory")
                    .containsKey("operatingSystem");
        }

        @Test
        @DisplayName("should return 200 with only requested dynamic fields")
        void shouldReturn200WithOnlyRequestedDynamicFields() {
            List<Long> ids = List.of(1L);
            List<String> fields = List.of("brand", "batteryCapacity");
            when(productUseCase.getProductsByIds(ids)).thenReturn(List.of(smartphone1));

            ResponseEntity<List<ProductDetailDTO>> response = productController.compareByIds(ids, fields);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            Map<String, Object> resultFields = response.getBody().get(0).fields();
            assertThat(resultFields).containsOnlyKeys("brand", "batteryCapacity");
            assertThat(resultFields.get("brand")).isEqualTo("Samsung");
            assertThat(resultFields.get("batteryCapacity")).isEqualTo(4500);
        }

        @Test
        @DisplayName("should resolve standard product fields correctly")
        void shouldResolveStandardProductFieldsCorrectly() {
            List<Long> ids = List.of(1L);
            List<String> fields = List.of("id", "name", "description", "price", "size", "weight", "color", "category");
            when(productUseCase.getProductsByIds(ids)).thenReturn(List.of(smartphone1));

            ResponseEntity<List<ProductDetailDTO>> response = productController.compareByIds(ids, fields);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            Map<String, Object> resultFields = response.getBody().get(0).fields();
            assertThat(resultFields.get("id")).isEqualTo(1L);
            assertThat(resultFields.get("name")).isEqualTo("Galaxy S24");
            assertThat(resultFields.get("description")).isEqualTo("Flagship Samsung phone");
            assertThat(resultFields.get("price")).isEqualTo(new BigDecimal("999.00"));
            assertThat(resultFields.get("size")).isEqualTo("6.5 inches");
            assertThat(resultFields.get("weight")).isEqualTo(0.19);
            assertThat(resultFields.get("color")).isEqualTo("Black");
            assertThat(resultFields.get("category")).isEqualTo("SMARTPHONE");
        }

        @Test
        @DisplayName("should mix standard and dynamic fields correctly")
        void shouldMixStandardAndDynamicFields() {
            List<Long> ids = List.of(2L);
            List<String> fields = List.of("price", "engine", "horsepower");
            when(productUseCase.getProductsByIds(ids)).thenReturn(List.of(car1));

            ResponseEntity<List<ProductDetailDTO>> response = productController.compareByIds(ids, fields);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            Map<String, Object> resultFields = response.getBody().get(0).fields();
            assertThat(resultFields).containsOnlyKeys("price", "engine", "horsepower");
            assertThat(resultFields.get("price")).isEqualTo(new BigDecimal("25000.00"));
            assertThat(resultFields.get("engine")).isEqualTo("2.0L");
            assertThat(resultFields.get("horsepower")).isEqualTo(169);
        }

        @Test
        @DisplayName("should return null value for unknown field")
        void shouldReturnNullValueForUnknownField() {
            List<Long> ids = List.of(1L);
            List<String> fields = List.of("unknownField");
            when(productUseCase.getProductsByIds(ids)).thenReturn(List.of(smartphone1));

            ResponseEntity<List<ProductDetailDTO>> response = productController.compareByIds(ids, fields);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().get(0).fields()).containsEntry("unknownField", null);
        }

        @Test
        @DisplayName("should return empty fields map when product has null attributes and fields is null")
        void shouldReturnEmptyFieldsWhenNullAttributesAndNullFields() {
            List<Long> ids = List.of(99L);
            when(productUseCase.getProductsByIds(ids)).thenReturn(List.of(productNoAttributes));

            ResponseEntity<List<ProductDetailDTO>> response = productController.compareByIds(ids, null);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().get(0).fields()).isEmpty();
        }

        @Test
        @DisplayName("should return all attributes when fields list is empty")
        void shouldReturnAllAttributesWhenFieldsListIsEmpty() {
            List<Long> ids = List.of(1L);
            when(productUseCase.getProductsByIds(ids)).thenReturn(List.of(smartphone1));

            ResponseEntity<List<ProductDetailDTO>> response = productController.compareByIds(ids, Collections.emptyList());

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().get(0).fields())
                    .containsKey("batteryCapacity")
                    .containsKey("brand")
                    .containsKey("memory")
                    .containsKey("operatingSystem");
        }

        @Test
        @DisplayName("should trim whitespace from field names")
        void shouldTrimWhitespaceFromFieldNames() {
            List<Long> ids = List.of(1L);
            List<String> fields = List.of(" price ", " brand ");
            when(productUseCase.getProductsByIds(ids)).thenReturn(List.of(smartphone1));

            ResponseEntity<List<ProductDetailDTO>> response = productController.compareByIds(ids, fields);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            Map<String, Object> resultFields = response.getBody().get(0).fields();
            assertThat(resultFields).containsKey("price");
            assertThat(resultFields).containsKey("brand");
            assertThat(resultFields).doesNotContainKey(" price ");
            assertThat(resultFields).doesNotContainKey(" brand ");
        }

        @Test
        @DisplayName("should return correct DTO structure for multiple products")
        void shouldReturnCorrectDTOStructureForMultipleProducts() {
            List<Long> ids = List.of(1L, 4L);
            when(productUseCase.getProductsByIds(ids)).thenReturn(List.of(smartphone1, smartphone2));

            ResponseEntity<List<ProductDetailDTO>> response = productController.compareByIds(ids, null);

            assertThat(response.getBody()).extracting(ProductDetailDTO::id)
                    .containsExactlyInAnyOrder(1L, 4L);
            assertThat(response.getBody()).extracting(ProductDetailDTO::name)
                    .containsExactlyInAnyOrder("Galaxy S24", "iPhone 15");
            assertThat(response.getBody()).extracting(ProductDetailDTO::description)
                    .containsExactlyInAnyOrder("Flagship Samsung phone", "Apple smartphone");
        }

        @Test
        @DisplayName("should propagate ProductNotFoundException from use case")
        void shouldPropagateProductNotFoundException() {
            List<Long> ids = List.of(999L);
            when(productUseCase.getProductsByIds(ids))
                    .thenThrow(new ProductNotFoundException("" + ids));

            assertThatThrownBy(() -> productController.compareByIds(ids, null))
                    .isInstanceOf(ProductNotFoundException.class)
                    .hasMessageContaining(ids.toString());
        }
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // compareByCategory
    // ─────────────────────────────────────────────────────────────────────────────
    @Nested
    @DisplayName("GET /compare/category/{category} — compareByCategory")
    class CompareByCategory {

        @Test
        @DisplayName("should return 200 with full attributes when fields is null")
        void shouldReturn200WithFullAttributesWhenFieldsIsNull() {
            when(productUseCase.getProductsByCategory("SMARTPHONE"))
                    .thenReturn(List.of(smartphone1, smartphone2));

            ResponseEntity<List<ProductDetailDTO>> response =
                    productController.compareByCategory("SMARTPHONE", null);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).hasSize(2);
            assertThat(response.getBody()).extracting(ProductDetailDTO::name)
                    .containsExactlyInAnyOrder("Galaxy S24", "iPhone 15");
            assertThat(response.getBody().get(0).fields())
                    .containsKey("batteryCapacity")
                    .containsKey("brand");
        }

        @Test
        @DisplayName("should return only requested fields for category products")
        void shouldReturnOnlyRequestedFieldsForCategoryProducts() {
            List<String> fields = List.of("price", "engine");
            when(productUseCase.getProductsByCategory("CAR")).thenReturn(List.of(car1));

            ResponseEntity<List<ProductDetailDTO>> response =
                    productController.compareByCategory("CAR", fields);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            Map<String, Object> resultFields = response.getBody().get(0).fields();
            assertThat(resultFields).containsOnlyKeys("price", "engine");
            assertThat(resultFields.get("price")).isEqualTo(new BigDecimal("25000.00"));
            assertThat(resultFields.get("engine")).isEqualTo("2.0L");
        }

        @Test
        @DisplayName("should return correct id, name and description in DTO")
        void shouldReturnCorrectIdNameDescriptionInDTO() {
            when(productUseCase.getProductsByCategory("CAR")).thenReturn(List.of(car1));

            ResponseEntity<List<ProductDetailDTO>> response =
                    productController.compareByCategory("CAR", null);

            ProductDetailDTO dto = response.getBody().get(0);
            assertThat(dto.id()).isEqualTo(2L);
            assertThat(dto.name()).isEqualTo("Toyota Corolla");
            assertThat(dto.description()).isEqualTo("Sedan 2024");
        }

        @Test
        @DisplayName("should resolve standard fields for category products")
        void shouldResolveStandardFieldsForCategoryProducts() {
            List<String> fields = List.of("id", "name", "price", "color", "category");
            when(productUseCase.getProductsByCategory("MOTORCYCLE")).thenReturn(List.of(motorcycle1));

            ResponseEntity<List<ProductDetailDTO>> response =
                    productController.compareByCategory("MOTORCYCLE", fields);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            Map<String, Object> resultFields = response.getBody().get(0).fields();
            assertThat(resultFields.get("id")).isEqualTo(3L);
            assertThat(resultFields.get("name")).isEqualTo("Yamaha MT-07");
            assertThat(resultFields.get("price")).isEqualTo(new BigDecimal("9500.00"));
            assertThat(resultFields.get("color")).isEqualTo("Blue");
            assertThat(resultFields.get("category")).isEqualTo("MOTORCYCLE");
        }

        @Test
        @DisplayName("should return empty fields map when product has null attributes")
        void shouldReturnEmptyFieldsWhenProductHasNullAttributes() {
            when(productUseCase.getProductsByCategory("OTHER"))
                    .thenReturn(List.of(productNoAttributes));

            ResponseEntity<List<ProductDetailDTO>> response =
                    productController.compareByCategory("OTHER", null);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().get(0).fields()).isEmpty();
        }

        @Test
        @DisplayName("should return all attributes when fields list is empty")
        void shouldReturnAllAttributesWhenFieldsListIsEmpty() {
            when(productUseCase.getProductsByCategory("CAR")).thenReturn(List.of(car1));

            ResponseEntity<List<ProductDetailDTO>> response =
                    productController.compareByCategory("CAR", Collections.emptyList());

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().get(0).fields())
                    .containsKey("engine")
                    .containsKey("horsepower")
                    .containsKey("doors")
                    .containsKey("fuelType");
        }

        @Test
        @DisplayName("should return null value for unknown dynamic field in category")
        void shouldReturnNullForUnknownFieldInCategory() {
            List<String> fields = List.of("unknownField");
            when(productUseCase.getProductsByCategory("CAR")).thenReturn(List.of(car1));

            ResponseEntity<List<ProductDetailDTO>> response =
                    productController.compareByCategory("CAR", fields);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().get(0).fields()).containsEntry("unknownField", null);
        }

        @Test
        @DisplayName("should trim whitespace from field names in category comparison")
        void shouldTrimWhitespaceFromFieldNamesInCategoryComparison() {
            List<String> fields = List.of(" engine ", " horsepower ");
            when(productUseCase.getProductsByCategory("MOTORCYCLE")).thenReturn(List.of(motorcycle1));

            ResponseEntity<List<ProductDetailDTO>> response =
                    productController.compareByCategory("MOTORCYCLE", fields);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            Map<String, Object> resultFields = response.getBody().get(0).fields();
            assertThat(resultFields).containsKey("engine");
            assertThat(resultFields).containsKey("horsepower");
            assertThat(resultFields).doesNotContainKey(" engine ");
            assertThat(resultFields).doesNotContainKey(" horsepower ");
        }

        @Test
        @DisplayName("should return correct DTO structure for multiple category products")
        void shouldReturnCorrectDTOStructureForMultipleCategoryProducts() {
            when(productUseCase.getProductsByCategory("SMARTPHONE"))
                    .thenReturn(List.of(smartphone1, smartphone2));

            ResponseEntity<List<ProductDetailDTO>> response =
                    productController.compareByCategory("SMARTPHONE", null);

            assertThat(response.getBody()).extracting(ProductDetailDTO::id)
                    .containsExactlyInAnyOrder(1L, 4L);
            assertThat(response.getBody()).extracting(ProductDetailDTO::name)
                    .containsExactlyInAnyOrder("Galaxy S24", "iPhone 15");
            assertThat(response.getBody()).extracting(ProductDetailDTO::description)
                    .containsExactlyInAnyOrder("Flagship Samsung phone", "Apple smartphone");
        }

        @Test
        @DisplayName("should propagate ProductNotFoundException when category not found")
        void shouldPropagateProductNotFoundExceptionWhenCategoryNotFound() {
            when(productUseCase.getProductsByCategory("UNKNOWN"))
                    .thenThrow(new ProductNotFoundException("No products found for category: UNKNOWN"));

            assertThatThrownBy(() -> productController.compareByCategory("UNKNOWN", null))
                    .isInstanceOf(ProductNotFoundException.class)
                    .hasMessageContaining("No products found for category: UNKNOWN");
        }
    }
}
