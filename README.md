# RestAcademy - Spring Boot REST API Showcase

A small Spring Boot project demonstrating RESTful API best practices:

- CRUD operations (Create, Read, Update, Delete)
- Data validation (Jakarta Bean Validation)
- Global exception handling with structured error responses
- Pagination and sorting with Spring Data JPA
- In-memory H2 database for local dev and tests
- OpenAPI/Swagger UI for interactive documentation
- Actuator health/info endpoints

## Requirements
- Java 17+
- Maven 3.8+

## Run
- From VS Code or terminal:
  - Windows PowerShell: `mvnw.cmd spring-boot:run`
  - Or if Maven is installed: `mvn spring-boot:run`

App starts on http://localhost:8080

- Swagger UI: http://localhost:8080/swagger-ui/index.html
- H2 Console: http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:testdb`, user: `sa`, password: `password`)

## API Endpoints
- `POST /api/v1/users` — Create
- `GET /api/v1/users` — List (pagination)
- `GET /api/v1/users/all` — List all
- `GET /api/v1/users/{id}` — Get by id
- `PUT /api/v1/users/{id}` — Update
- `DELETE /api/v1/users/{id}` — Delete
- `GET /api/v1/users/department/{department}` — Filter by dept
- `GET /api/v1/users/age-range?minAge=&maxAge=` — Filter by age
- `GET /api/v1/users/search?firstName=` — Search by first name

Health and info:
- `GET /api/v1/health`
- `GET /api/v1/info`

## Tests
Run all tests:
- Windows PowerShell: `mvnw.cmd test`
- Or: `mvn test`
