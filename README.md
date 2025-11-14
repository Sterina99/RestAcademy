# RestAcademy - Spring Boot REST API Showcase

A small Spring Boot project demonstrating RESTful API best practices:

- CRUD operations (Create, Read, Update, Delete)
- Data validation (Jakarta Bean Validation)
- Global exception handling with structured error responses
- Pagination and sorting with Spring Data JPA
- In-memory H2 database for local dev and tests
- JWT-based authentication and authorization
- Spring Security integration
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

### Authentication (Public)
- `POST /api/v1/auth/register` — Register new user
- `POST /api/v1/auth/login` — Login and get JWT token

### User Management (Requires JWT)
- `POST /api/v1/users` — Create user
- `GET /api/v1/users` — List users (pagination)
- `GET /api/v1/users/all` — List all users
- `GET /api/v1/users/{id}` — Get user by id
- `PUT /api/v1/users/{id}` — Update user
- `DELETE /api/v1/users/{id}` — Delete user
- `GET /api/v1/users/department/{department}` — Filter by department
- `GET /api/v1/users/age-range?minAge=&maxAge=` — Filter by age range
- `GET /api/v1/users/search?firstName=` — Search by first name

### Health and Info (Public)
- `GET /api/v1/health` — Health check
- `GET /api/v1/info` — Application info

**Note:** All user endpoints (except auth) require a valid JWT token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

Default test credentials: email: `john.doe@example.com`, password: `password123`

## Tests
Run all tests:
- Windows PowerShell: `mvnw.cmd test`
- Or: `mvn test`
