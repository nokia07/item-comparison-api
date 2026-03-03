# Item Comparison API

API REST para consultar y comparar productos por IDs o por categoría, con soporte de campos dinámicos.

## Características

- Comparación por lista de IDs.
- Comparación por categoría (en path param).
- Respuesta tipada con `ProductDetailDTO` (`id`, `name`, `description`, `fields`).
- `fields` opcional:
	- Si se envía: retorna solo los campos solicitados.
	- Si no se envía: retorna todos los atributos dinámicos del producto.
- Validaciones de entrada y manejo global de errores.
- Documentación OpenAPI/Swagger.
- Base de datos H2 en memoria con datos semilla.

## Stack

- Java 21
- Spring Boot 3.5.11
- Spring Web
- Spring Data JPA
- Bean Validation
- H2 Database
- Springdoc OpenAPI (`swagger-ui`)
- Lombok
- Maven

## Arquitectura

El proyecto sigue una separación por capas (estilo clean/hexagonal):

- `application/domain`: modelo y contratos de dominio.
- `application/domain/usecase`: casos de uso.
- `infrastructure/adapter/persistence`: acceso a datos (JPA + mapper).
- `infrastructure/adapter/web`: controladores REST, DTOs y manejo de errores.
- `config`: configuración de beans y Swagger.



Puedes revisar más detalle en `docs/ARCHITECTURE.md`.

## Requisitos

- JDK 21
- Maven 3.9+

## Ejecución local

```bash
mvn clean compile
mvn spring-boot:run
```

La aplicación inicia en:

- `http://localhost:8080`

## Endpoints principales

Base path:

- `/api/v1/products`

### 1) Health check

- `GET /api/v1/products/health`

Respuesta:

```json
true
```

### 2) Comparar por IDs

- `GET /api/v1/products/compare`

Query params:

- `ids` (requerido): lista de IDs, por ejemplo `ids=1,2`.
- `fields` (opcional): lista de campos a retornar en `fields`, por ejemplo `fields=price,batteryCapacity`.

Ejemplo:

```bash
curl "http://localhost:8080/api/v1/products/compare?ids=1,4&fields=price,batteryCapacity"
```

### 3) Comparar por categoría

- `GET /api/v1/products/compare/category/{category}`

Path param:

- `category` (requerido), por ejemplo: `SMARTPHONE`, `CAR`, `MOTORCYCLE`.

Query params:

- `fields` (opcional): misma lógica que en comparación por IDs.

Ejemplos:

```bash
curl "http://localhost:8080/api/v1/products/compare/category/SMARTPHONE"
```

```bash
curl "http://localhost:8080/api/v1/products/compare/category/CAR?fields=price,engine"
```

## Formato de respuesta

Los endpoints de comparación retornan una lista de objetos con esta estructura:

```json
[
	{
		"id": 1,
		"name": "Galaxy S24",
		"description": "Flagship Samsung phone",
		"fields": {
			"price": 999.00,
			"batteryCapacity": 4500
		}
	}
]
```

## Manejo de errores

Errores comunes:

- `400 Bad Request`: parámetros inválidos o faltantes.
- `404 Not Found`: productos no encontrados para IDs/categoría.
- `500 Internal Server Error`: error no controlado.

Formato de error:

```json
{
	"timestamp": "2026-03-02T12:00:00Z",
	"status": 400,
	"error": "Bad Request",
	"message": "ids parameter is required and cannot be empty",
	"path": "/api/v1/products/compare"
}
```

## Swagger y utilidades

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- H2 Console: `http://localhost:8080/h2-console`
	- JDBC URL: `jdbc:h2:mem:productsdb`
	- User: `sa`
	- Password: vacío

## Datos semilla

Al iniciar, la app carga datos desde `src/main/resources/data.sql` para estas categorías:

- `SMARTPHONE`
- `CAR`
- `MOTORCYCLE`

## Tests y build

```bash
mvn test
```

```bash
mvn clean install
```

---

## Postman Collection

Puedes importar esta colección directamente en Postman.

### Opción 1: Importar desde archivo JSON

1. En Postman, ve a **Import**.
2. Selecciona **Raw text**.
3. Pega el siguiente JSON y guarda la colección.

```json
{
	"info": {
		"name": "Item Comparison API",
		"description": "Colección para probar endpoints de comparación por IDs y categoría.",
		"schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
	},
	"item": [
		{
			"name": "Home",
			"request": {
				"method": "GET",
				"header": [],
				"url": {
					"raw": "{{baseUrl}}/",
					"host": [
						"{{baseUrl}}"
					],
					"path": [
						""
					]
				}
			}
		},
		{
			"name": "Health Check",
			"request": {
				"method": "GET",
				"header": [],
				"url": {
					"raw": "{{baseUrl}}/api/v1/products/health",
					"host": [
						"{{baseUrl}}"
					],
					"path": [
						"api",
						"v1",
						"products",
						"health"
					]
				}
			}
		},
		{
			"name": "Compare by IDs (default fields)",
			"request": {
				"method": "GET",
				"header": [],
				"url": {
					"raw": "{{baseUrl}}/api/v1/products/compare?ids=1,4",
					"host": [
						"{{baseUrl}}"
					],
					"path": [
						"api",
						"v1",
						"products",
						"compare"
					],
					"query": [
						{
							"key": "ids",
							"value": "1,4"
						}
					]
				}
			}
		},
		{
			"name": "Compare by IDs (selected fields)",
			"request": {
				"method": "GET",
				"header": [],
				"url": {
					"raw": "{{baseUrl}}/api/v1/products/compare?ids=1,5&fields=price,batteryCapacity",
					"host": [
						"{{baseUrl}}"
					],
					"path": [
						"api",
						"v1",
						"products",
						"compare"
					],
					"query": [
						{
							"key": "ids",
							"value": "1,5"
						},
						{
							"key": "fields",
							"value": "price,batteryCapacity"
						}
					]
				}
			}
		},
		{
			"name": "Compare by Category (SMARTPHONE)",
			"request": {
				"method": "GET",
				"header": [],
				"url": {
					"raw": "{{baseUrl}}/api/v1/products/compare/category/SMARTPHONE",
					"host": [
						"{{baseUrl}}"
					],
					"path": [
						"api",
						"v1",
						"products",
						"compare",
						"category",
						"SMARTPHONE"
					]
				}
			}
		},
		{
			"name": "Compare by Category (CAR + fields)",
			"request": {
				"method": "GET",
				"header": [],
				"url": {
					"raw": "{{baseUrl}}/api/v1/products/compare/category/CAR?fields=price,engine",
					"host": [
						"{{baseUrl}}"
					],
					"path": [
						"api",
						"v1",
						"products",
						"compare",
						"category",
						"CAR"
					],
					"query": [
						{
							"key": "fields",
							"value": "price,engine"
						}
					]
				}
			}
		}
	],
	"variable": [
		{
			"key": "baseUrl",
			"value": "http://localhost:8080"
		}
	]
}
```

### Opción 2: Importar Environment

Opcionalmente, puedes crear/importar este environment:

```json
{
	"name": "Item Comparison API - Local",
	"values": [
		{
			"key": "baseUrl",
			"value": "http://localhost:8080",
			"enabled": true
		}
	],
	"_postman_variable_scope": "environment",
	"_postman_exported_at": "2026-03-02T00:00:00.000Z",
	"_postman_exported_using": "Postman/11"
}
```

### Verificación rápida en Postman

- Ejecuta primero `Health Check`.
- Luego prueba `Compare by Category (SMARTPHONE)`.
- Finalmente prueba `Compare by IDs (selected fields)` para validar campos dinámicos.
