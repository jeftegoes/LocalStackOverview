package com.example.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

import java.net.URI;

@Configuration
public class SnsConfig {
    @Value("${local-stack-uri}")
    private String localStackUri;

    private final DefaultCredentialsProvider defaultCredentialsProvider;

    public SnsConfig(DefaultCredentialsProvider defaultCredentialsProvider) {
        this.defaultCredentialsProvider = defaultCredentialsProvider;
    }

    @Bean
    public SnsClient snsClient() {
        return SnsClient
                .builder()
                .endpointOverride(URI.create(localStackUri))
                .region(Region.SA_EAST_1)
                .credentialsProvider(defaultCredentialsProvider)
                .build();
    }
}
