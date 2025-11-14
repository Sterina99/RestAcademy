package com.restacademy.service;

import com.restacademy.dto.RegisterRequest;
import com.restacademy.dto.UserCreateRequest;
import com.restacademy.dto.UserResponse;
import com.restacademy.dto.UserUpdateRequest;
import com.restacademy.model.User;
import com.restacademy.repository.UserRepository;
import com.restacademy.exception.ResourceNotFoundException;
import com.restacademy.exception.DuplicateResourceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Load user by username (email) for Spring Security
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }

    /**
     * Register a new user
     * @param registerRequest the registration request
     * @return created user response
     */
    public UserResponse registerUser(RegisterRequest registerRequest) {
        // Check if email already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + registerRequest.getEmail());
        }

        User user = new User(
            registerRequest.getFirstName(),
            registerRequest.getLastName(),
            registerRequest.getEmail(),
            passwordEncoder.encode(registerRequest.getPassword()),
            registerRequest.getAge(),
            registerRequest.getDepartment()
        );

        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser);
    }

    /**
     * Create a new user
     * @param userCreateRequest the user creation request
     * @return created user response
     */
    public UserResponse createUser(UserCreateRequest userCreateRequest) {
        // Check if email already exists
        if (userRepository.existsByEmail(userCreateRequest.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + userCreateRequest.getEmail());
        }

        User user = new User(
            userCreateRequest.getFirstName(),
            userCreateRequest.getLastName(),
            userCreateRequest.getEmail(),
            userCreateRequest.getAge(),
            userCreateRequest.getDepartment()
        );

        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser);
    }

    /**
     * Get user by ID
     * @param id the user ID
     * @return user response
     */
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return new UserResponse(user);
    }

    /**
     * Get all users with pagination
     * @param pageable pagination information
     * @return page of user responses
     */
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(UserResponse::new);
    }

    /**
     * Get all users without pagination
     * @return list of all user responses
     */
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                   .map(UserResponse::new)
                   .collect(Collectors.toList());
    }

    /**
     * Update user by ID
     * @param id the user ID
     * @param userUpdateRequest the update request
     * @return updated user response
     */
    public UserResponse updateUser(Long id, UserUpdateRequest userUpdateRequest) {
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Check if email is being changed and if it already exists
        if (!existingUser.getEmail().equals(userUpdateRequest.getEmail()) &&
            userRepository.existsByEmail(userUpdateRequest.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + userUpdateRequest.getEmail());
        }

        existingUser.setFirstName(userUpdateRequest.getFirstName());
        existingUser.setLastName(userUpdateRequest.getLastName());
        existingUser.setEmail(userUpdateRequest.getEmail());
        existingUser.setAge(userUpdateRequest.getAge());
        existingUser.setDepartment(userUpdateRequest.getDepartment());

        User updatedUser = userRepository.save(existingUser);
        return new UserResponse(updatedUser);
    }

    /**
     * Delete user by ID
     * @param id the user ID
     */
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    /**
     * Get users by department
     * @param department the department name
     * @return list of users in the department
     */
    @Transactional(readOnly = true)
    public List<UserResponse> getUsersByDepartment(String department) {
        List<User> users = userRepository.findUsersByDepartmentSorted(department);
        return users.stream()
                   .map(UserResponse::new)
                   .collect(Collectors.toList());
    }

    /**
     * Get users by age range
     * @param minAge minimum age
     * @param maxAge maximum age
     * @return list of users within age range
     */
    @Transactional(readOnly = true)
    public List<UserResponse> getUsersByAgeRange(Integer minAge, Integer maxAge) {
        List<User> users = userRepository.findByAgeBetween(minAge, maxAge);
        return users.stream()
                   .map(UserResponse::new)
                   .collect(Collectors.toList());
    }

    /**
     * Search users by first name
     * @param firstName the first name pattern
     * @return list of matching users
     */
    @Transactional(readOnly = true)
    public List<UserResponse> searchUsersByFirstName(String firstName) {
        List<User> users = userRepository.findByFirstNameContainingIgnoreCase(firstName);
        return users.stream()
                   .map(UserResponse::new)
                   .collect(Collectors.toList());
    }

    /**
     * Get user count by department
     * @param department the department name
     * @return count of users in department
     */
    @Transactional(readOnly = true)
    public long getUserCountByDepartment(String department) {
        return userRepository.countByDepartment(department);
    }
}
