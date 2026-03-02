package com.hackerrank.products.infrastructure.adapter.web;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hackerrank.products.application.domain.model.Product;
import com.hackerrank.products.application.domain.usecase.ProductUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {

	private final ProductUseCase productUseCase;

	@GetMapping("/compare")
	public ResponseEntity<List<Map<String, Object>>> compare(
			@RequestParam List<Long> ids,
			@RequestParam List<String> fields) {
                
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
