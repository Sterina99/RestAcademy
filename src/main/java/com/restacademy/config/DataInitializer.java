package com.restacademy.config;

import com.restacademy.model.User;
import com.restacademy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Data initialization component
 * Loads sample data into the database on startup
 */
@Component
@Profile("!no-data") // Only run if 'no-data' profile is NOT active
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    @Autowired
    public DataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Only load data if database is empty
        if (userRepository.count() == 0) {
            loadSampleData();
        }
    }

    private void loadSampleData() {
        // Create sample users
        User user1 = new User("John", "Doe", "john.doe@example.com", 28, "Engineering");
        User user2 = new User("Jane", "Smith", "jane.smith@example.com", 32, "Marketing");
        User user3 = new User("Mike", "Johnson", "mike.johnson@example.com", 25, "Engineering");
        User user4 = new User("Sarah", "Wilson", "sarah.wilson@example.com", 30, "HR");
        User user5 = new User("David", "Brown", "david.brown@example.com", 35, "Finance");
        User user6 = new User("Emily", "Davis", "emily.davis@example.com", 27, "Marketing");
        User user7 = new User("Chris", "Miller", "chris.miller@example.com", 29, "Engineering");
        User user8 = new User("Lisa", "Anderson", "lisa.anderson@example.com", 31, "Sales");

        // Save all users
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        userRepository.save(user5);
        userRepository.save(user6);
        userRepository.save(user7);
        userRepository.save(user8);

        System.out.println("✅ Sample data loaded successfully!");
        System.out.println("📊 Total users created: " + userRepository.count());
    }
}
