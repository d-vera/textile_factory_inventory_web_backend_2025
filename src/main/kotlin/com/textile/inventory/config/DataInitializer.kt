package com.textile.inventory.config

import com.textile.inventory.model.Role
import com.textile.inventory.model.User
import com.textile.inventory.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class DataInitializer {
    
    @Bean
    fun initializeData(
        userRepository: UserRepository,
        passwordEncoder: PasswordEncoder
    ): CommandLineRunner {
        return CommandLineRunner {
            // Check if users already exist
            if (userRepository.count() == 0L) {
                // Create admin user
                val admin = User(
                    username = "admin",
                    password = passwordEncoder.encode("admin123"),
                    role = Role.ADMIN,
                    enabled = true
                )
                
                // Create regular user
                val user = User(
                    username = "user",
                    password = passwordEncoder.encode("user123"),
                    role = Role.USER,
                    enabled = true
                )
                
                userRepository.save(admin)
                userRepository.save(user)
                
                println("Default users created successfully!")
                println("Admin - Username: admin, Password: admin123")
                println("User - Username: user, Password: user123")
            } else {
                println("Users already exist in the database")
            }
        }
    }
}
