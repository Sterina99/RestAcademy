<!-- Use this file to provide workspace-specific custom instructions to Copilot. For more details, visit https://code.visualstudio.com/docs/copilot/copilot-customization#_use-a-githubcopilotinstructionsmd-file -->

# Copilot Instructions for RestAcademy

- This is a Spring Boot REST API project showcasing CRUD operations, validation, exception handling, pagination, and OpenAPI documentation.
- Prefer using Spring Boot 3.x APIs with Jakarta packages (jakarta.*).
- Use H2 in-memory DB for tests and local dev.
- Keep controllers thin; put business logic in services.
- Return appropriate HTTP status codes.
- Favor DTOs for request/response mapping.
- Use `@SpringBootTest` for integration tests and `MockMvc` for web layer testing.
