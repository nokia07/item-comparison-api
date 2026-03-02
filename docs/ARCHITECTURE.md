# Item Comparison API - Architecture

## Style
Hexagonal Architecture (Clean Architecture inspired)

## Layers

- domain: Business models and rules
- application: Use cases
- infrastructure: Database, JPA, REST Controllers

## Rules

- Controllers must not contain business logic
- Use cases orchestrate domain
- Domain must not depend on Spring
- Error handling centralized with @ControllerAdvice
