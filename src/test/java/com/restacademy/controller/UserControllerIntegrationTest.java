package com.restacademy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restacademy.dto.UserCreateRequest;
import com.restacademy.dto.UserUpdateRequest;
import com.restacademy.model.User;
import com.restacademy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@Transactional
@ActiveProfiles("test")
public class UserControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        userRepository.deleteAll(); // Clean database before each test
    }

    @Test
    void createUser_ShouldReturnCreatedUser() throws Exception {
        UserCreateRequest request = new UserCreateRequest(
            "John", "Doe", "john.doe@test.com", 30, "Engineering"
        );

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@test.com"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.department").value("Engineering"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    void createUser_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        UserCreateRequest request = new UserCreateRequest(
            "", "Doe", "invalid-email", -5, "Engineering"  // Invalid data
        );

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"));
    }

    @Test
    void getUserById_ShouldReturnUser() throws Exception {
        // Create a user first
        User user = new User("Jane", "Smith", "jane.smith@test.com", 25, "Marketing");
        User savedUser = userRepository.save(user);

        mockMvc.perform(get("/api/v1/users/{id}", savedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedUser.getId()))
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.email").value("jane.smith@test.com"));
    }

    @Test
    void getUserById_WithNonExistentId_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/users/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Resource Not Found"));
    }

    @Test
    void getAllUsers_ShouldReturnUsersList() throws Exception {
        // Create test users
        userRepository.save(new User("John", "Doe", "john@test.com", 30, "Engineering"));
        userRepository.save(new User("Jane", "Smith", "jane@test.com", 25, "Marketing"));

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users").isArray())
                .andExpect(jsonPath("$.users", hasSize(2)))
                .andExpect(jsonPath("$.totalItems").value(2))
                .andExpect(jsonPath("$.currentPage").value(0))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() throws Exception {
        // Create a user first
        User user = new User("John", "Doe", "john.doe@test.com", 30, "Engineering");
        User savedUser = userRepository.save(user);

        UserUpdateRequest request = new UserUpdateRequest(
            "John", "Smith", "john.smith@test.com", 31, "Management"
        );

        mockMvc.perform(put("/api/v1/users/{id}", savedUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedUser.getId()))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.email").value("john.smith@test.com"))
                .andExpect(jsonPath("$.age").value(31))
                .andExpect(jsonPath("$.department").value("Management"));
    }

    @Test
    void deleteUser_ShouldReturnNoContent() throws Exception {
        // Create a user first
        User user = new User("John", "Doe", "john.doe@test.com", 30, "Engineering");
        User savedUser = userRepository.save(user);

        mockMvc.perform(delete("/api/v1/users/{id}", savedUser.getId()))
                .andExpect(status().isNoContent());

        // Verify user is deleted
        mockMvc.perform(get("/api/v1/users/{id}", savedUser.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUsersByDepartment_ShouldReturnFilteredUsers() throws Exception {
        // Create test users
        userRepository.save(new User("John", "Doe", "john@test.com", 30, "Engineering"));
        userRepository.save(new User("Jane", "Smith", "jane@test.com", 25, "Marketing"));
        userRepository.save(new User("Mike", "Johnson", "mike@test.com", 28, "Engineering"));

        mockMvc.perform(get("/api/v1/users/department/{department}", "Engineering"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
