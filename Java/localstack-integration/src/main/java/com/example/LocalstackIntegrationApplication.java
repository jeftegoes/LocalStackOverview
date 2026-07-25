package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LocalstackIntegrationApplication {
    public static void main(String[] args) {
        SpringApplication.run(LocalstackIntegrationApplication.class, args);
    }
}
