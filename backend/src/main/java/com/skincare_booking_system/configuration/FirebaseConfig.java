package com.skincare_booking_system.configuration;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig {
    @Value("${fcm.credentials.file.path}")
    private String credentialsFilePath;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        System.out.println("Firebase Credential Path: " + credentialsFilePath);
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(
                        GoogleCredentials.fromStream(new ClassPathResource(credentialsFilePath).getInputStream()))
                .build();
        return FirebaseApp.initializeApp(options);
    }
}
