package com.example.basicauth;

import com.example.basicauth.dtos.UserRegistrationDto;
import com.example.basicauth.repositories.UserRepository;
import com.example.basicauth.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Initialize sample users in the database on application startup *
 * Here i use CommandLineRunner to initialize data after application context is loaded.
 * Alternatively, can use @PostConstruct annotation on a method instead
 *
 * @PostConstruct public void seedData() {initialization code here}
 * Differences: CommandLineRunner Runs AFTER entire application is fully initialized where @PostConstruct Runs immediately
 * after bean is created (may be before app is ready)
 */

@Component
@AllArgsConstructor
public class InitialSeeder implements CommandLineRunner {

    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // Check if users already exist to avoid duplicates
        if (userRepository.findByUsername("admin").isEmpty()) {
            userService.createUser(new UserRegistrationDto("admin", "admin123", "admin@example.com"));
            System.out.println("Created default admin user with username: admin, password: admin123");
        }

        if (userRepository.findByUsername("user").isEmpty()) {
            userService.createUser(new UserRegistrationDto("user", "user123", "user@example.com"));
            System.out.println("Created default user with username: user, password: user123");
        }
    }
}

