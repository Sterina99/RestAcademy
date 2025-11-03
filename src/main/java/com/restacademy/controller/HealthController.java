package com.restacademy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Health Check Controller
 * Provides basic health and status information about the application
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Health Check", description = "Health and status endpoints")
public class HealthController {

    /**
     * Basic health check endpoint
     * HTTP GET /api/v1/health
     */
    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Returns the health status of the application")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now());
        health.put("service", "RestAcademy API");
        health.put("version", "1.0.0");
        return ResponseEntity.ok(health);
    }

    /**
     * Application information endpoint
     * HTTP GET /api/v1/info
     */
    @GetMapping("/info")
    @Operation(summary = "Application info", description = "Returns information about the application")
    public ResponseEntity<Map<String, Object>> info() {
        Map<String, Object> info = new HashMap<>();
        info.put("application", "RestAcademy");
        info.put("description", "REST API Academy - Spring Boot showcase");
        info.put("version", "1.0.0");
        info.put("author", "REST Academy Team");
        info.put("documentation", "/swagger-ui/index.html");
        info.put("timestamp", LocalDateTime.now());
        
        Map<String, String> features = new HashMap<>();
        features.put("CRUD Operations", "Complete Create, Read, Update, Delete operations");
        features.put("Data Validation", "Jakarta Bean Validation with custom error handling");
        features.put("Pagination", "Spring Data JPA pagination and sorting");
        features.put("Custom Queries", "Repository custom queries and methods");
        features.put("Exception Handling", "Global exception handler with proper HTTP status codes");
        features.put("API Documentation", "OpenAPI 3 with Swagger UI");
        features.put("Database", "H2 in-memory database for testing");
        
        info.put("features", features);
        return ResponseEntity.ok(info);
    }

    /**
     * Welcome message endpoint
     * HTTP GET /api/v1/welcome
     */
    @GetMapping("/welcome")
    @Operation(summary = "Welcome message", description = "Returns a welcome message for the REST Academy")
    public ResponseEntity<Map<String, String>> welcome() {
        Map<String, String> welcome = new HashMap<>();
        welcome.put("message", "Welcome to REST Academy!");
        welcome.put("description", "This Spring Boot application demonstrates RESTful API best practices");
        welcome.put("documentation", "Visit /swagger-ui/index.html for interactive API documentation");
        welcome.put("h2Console", "Visit /h2-console for database access (JDBC URL: jdbc:h2:mem:testdb)");
        return ResponseEntity.ok(welcome);
    }
}
