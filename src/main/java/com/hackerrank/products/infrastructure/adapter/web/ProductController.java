package com.hackerrank.products.infrastructure.adapter.web;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hackerrank.products.application.domain.model.Product;
import com.hackerrank.products.application.domain.usecase.ProductUseCase;
import com.hackerrank.products.infrastructure.adapter.web.error.ApiErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Operations for product comparison")
public class ProductController {

	private final ProductUseCase productUseCase;

	@GetMapping("/compare")
	@Operation(
			summary = "Compare products",
			description = "Compares products by ids and returns only the requested fields.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Products compared successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid request parameters",
					content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
			@ApiResponse(responseCode = "404", description = "Product not found",
					content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Unexpected server error",
					content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
	})
	public ResponseEntity<List<Map<String, Object>>> compare(
			@Parameter(
					description = "Product ids to compare. Example: 1,2",
					example = "1,2",
					required = true)
			@RequestParam(required = false)
            @NotEmpty(message = "ids parameter is required and cannot be empty")
            List<Long> ids,
			@Parameter(
					description = "Fields to return in comparison. Example: name,price,engine,batteryCapacity",
					example = "name,price,engine,batteryCapacity",
					required = true)
			@RequestParam(required = false)
            @NotEmpty(message = "fields parameter is required and cannot be empty")
            List<String> fields) {
                
        List<Product> products = productUseCase.compareProducts(ids);
		List<Map<String, Object>> response = new ArrayList<>();

		for (Product product : products) {
            Map<String, Object> attributes = product.getAttributes() != null ? product.getAttributes() : Map.of();
			Map<String, Object> row = new LinkedHashMap<>();
			row.put("id", product.getId());

			for (String field : fields) {
				String normalizedField = field.trim();
				row.put(normalizedField, resolveFieldValue(product, attributes, normalizedField));
			}

			response.add(row);
		}

		return ResponseEntity.ok(response);
	}

	private Object resolveFieldValue(Product product, Map<String, Object> attributes, String field) {
		return switch (field) {
			case "id" -> product.getId();
			case "name" -> product.getName();
			case "description" -> product.getDescription();
			case "price" -> product.getPrice();
			case "size" -> product.getSize();
			case "weight" -> product.getWeight();
			case "color" -> product.getColor();
			case "category" -> product.getCategory();
			default -> attributes.get(field);
		};
	}

}
