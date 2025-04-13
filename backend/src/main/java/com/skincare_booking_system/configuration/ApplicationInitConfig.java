package com.skincare_booking_system.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.skincare_booking_system.constant.Roles;
import com.skincare_booking_system.entities.User;
import com.skincare_booking_system.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
// Class nay dung de tao account admin moi lan start application len
public class ApplicationInitConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .role(Roles.ADMIN)
                        .build();

                userRepository.save(user);
                log.warn("Admin user created with default password: admin, please change password ");
            }
        };
    }
}
