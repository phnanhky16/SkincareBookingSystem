package com.skincare_booking_system.configuration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class ConfigCloudinary {

    @Bean
    public Cloudinary configKey() {
        Map<String, String> config = new HashMap();
        config.put("cloud_name", "drjerfrgw");
        config.put("api_key", "477216371893122");
        config.put("api_secret", "KvVYCEthQ4NlkZWHvFIVTpOFLPk");
        return new Cloudinary(config);
    }
}
