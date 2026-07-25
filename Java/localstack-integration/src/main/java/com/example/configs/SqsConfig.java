package com.example.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;

@Configuration
public class SqsConfig {
    @Value("${local-stack-uri}")
    private String localStackUri;

    @Bean
    public SqsClient sqsClient() {
        DefaultCredentialsProvider customizedProvider = DefaultCredentialsProvider.builder()
                .asyncCredentialUpdateEnabled(true)
                .build();

        SqsClient sqsClient = SqsClient
                .builder()
                .endpointOverride(URI.create(localStackUri))
                .region(Region.SA_EAST_1)
                .credentialsProvider(customizedProvider)
                .build();

        return sqsClient;
    }
}
