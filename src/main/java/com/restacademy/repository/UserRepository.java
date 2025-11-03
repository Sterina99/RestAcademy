package com.restacademy.repository;

import com.restacademy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by email address
     * @param email the email to search for
     * @return Optional User if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Find users by department
     * @param department the department name
     * @return list of users in the department
     */
    List<User> findByDepartment(String department);

    /**
     * Find users by age range
     * @param minAge minimum age
     * @param maxAge maximum age
     * @return list of users within age range
     */
    List<User> findByAgeBetween(Integer minAge, Integer maxAge);

    /**
     * Check if email exists
     * @param email the email to check
     * @return true if email exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Find users by first name containing (case insensitive)
     * @param firstName the first name pattern
     * @return list of matching users
     */
    List<User> findByFirstNameContainingIgnoreCase(String firstName);

    /**
     * Custom query to find users by department with sorting
     * @param department the department name
     * @return list of users ordered by last name
     */
    @Query("SELECT u FROM User u WHERE u.department = :department ORDER BY u.lastName ASC")
    List<User> findUsersByDepartmentSorted(@Param("department") String department);

    /**
     * Count users by department
     * @param department the department name
     * @return count of users in department
     */
    long countByDepartment(String department);
}
