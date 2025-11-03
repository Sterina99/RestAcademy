package com.restacademy.controller;

import com.restacademy.dto.UserCreateRequest;
import com.restacademy.dto.UserResponse;
import com.restacademy.dto.UserUpdateRequest;
import com.restacademy.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for User management operations
 * Demonstrates all basic RESTful operations (CRUD)
 */
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Management", description = "REST API for managing users - showcases all RESTful operations")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * CREATE - Create a new user
     * HTTP POST /api/v1/users
     */
    @PostMapping
    @Operation(summary = "Create a new user", description = "Creates a new user with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody UserCreateRequest userCreateRequest) {
        UserResponse createdUser = userService.createUser(userCreateRequest);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    /**
     * READ - Get all users with pagination
     * HTTP GET /api/v1/users
     */
    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieves all users with optional pagination and sorting")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    public ResponseEntity<Map<String, Object>> getAllUsers(
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field")
            @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction (asc/desc)")
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserResponse> usersPage = userService.getAllUsers(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("users", usersPage.getContent());
        response.put("currentPage", usersPage.getNumber());
        response.put("totalItems", usersPage.getTotalElements());
        response.put("totalPages", usersPage.getTotalPages());
        response.put("pageSize", usersPage.getSize());
        response.put("hasNext", usersPage.hasNext());
        response.put("hasPrevious", usersPage.hasPrevious());

        return ResponseEntity.ok(response);
    }

    /**
     * READ - Get all users without pagination
     * HTTP GET /api/v1/users/all
     */
    @GetMapping("/all")
    @Operation(summary = "Get all users without pagination", description = "Retrieves all users without pagination")
    public ResponseEntity<List<UserResponse>> getAllUsersWithoutPagination() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * READ - Get user by ID
     * HTTP GET /api/v1/users/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieves a specific user by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResponse> getUserById(
            @Parameter(description = "User ID") @PathVariable Long id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * UPDATE - Update user by ID
     * HTTP PUT /api/v1/users/{id}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Updates an existing user with new information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    public ResponseEntity<UserResponse> updateUser(
            @Parameter(description = "User ID") @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        UserResponse updatedUser = userService.updateUser(id, userUpdateRequest);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * DELETE - Delete user by ID
     * HTTP DELETE /api/v1/users/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Deletes a user by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID") @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * READ - Get users by department
     * HTTP GET /api/v1/users/department/{department}
     */
    @GetMapping("/department/{department}")
    @Operation(summary = "Get users by department", description = "Retrieves all users in a specific department")
    public ResponseEntity<List<UserResponse>> getUsersByDepartment(
            @Parameter(description = "Department name") @PathVariable String department) {
        List<UserResponse> users = userService.getUsersByDepartment(department);
        return ResponseEntity.ok(users);
    }

    /**
     * READ - Get users by age range
     * HTTP GET /api/v1/users/age-range
     */
    @GetMapping("/age-range")
    @Operation(summary = "Get users by age range", description = "Retrieves users within a specific age range")
    public ResponseEntity<List<UserResponse>> getUsersByAgeRange(
            @Parameter(description = "Minimum age") @RequestParam Integer minAge,
            @Parameter(description = "Maximum age") @RequestParam Integer maxAge) {
        List<UserResponse> users = userService.getUsersByAgeRange(minAge, maxAge);
        return ResponseEntity.ok(users);
    }

    /**
     * READ - Search users by first name
     * HTTP GET /api/v1/users/search
     */
    @GetMapping("/search")
    @Operation(summary = "Search users by first name", description = "Searches users by first name (case insensitive)")
    public ResponseEntity<List<UserResponse>> searchUsersByFirstName(
            @Parameter(description = "First name pattern") @RequestParam String firstName) {
        List<UserResponse> users = userService.searchUsersByFirstName(firstName);
        return ResponseEntity.ok(users);
    }

    /**
     * READ - Get user count by department
     * HTTP GET /api/v1/users/department/{department}/count
     */
    @GetMapping("/department/{department}/count")
    @Operation(summary = "Get user count by department", description = "Gets the number of users in a specific department")
    public ResponseEntity<Map<String, Object>> getUserCountByDepartment(
            @Parameter(description = "Department name") @PathVariable String department) {
        long count = userService.getUserCountByDepartment(department);
        Map<String, Object> response = new HashMap<>();
        response.put("department", department);
        response.put("userCount", count);
        return ResponseEntity.ok(response);
    }
}
