package com.jojolapa.cafemanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationProperties {

    // Add any application-specific properties here
    public static final int MIN_PASSWORD_LENGTH = 6;

}