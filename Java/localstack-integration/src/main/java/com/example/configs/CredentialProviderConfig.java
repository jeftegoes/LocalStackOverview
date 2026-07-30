package com.example.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;

@Configuration
public class CredentialProviderConfig {
    @Bean
    public DefaultCredentialsProvider defaultCredentialsProvider() {
        return DefaultCredentialsProvider.builder()
                .asyncCredentialUpdateEnabled(true)
                .build();
    }
}
