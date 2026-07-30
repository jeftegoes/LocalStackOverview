package com.example.facades;

import com.example.services.RandomUserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class FeedRandomUsersFacade {
    private final RandomUserService randomUserService;

    public FeedRandomUsersFacade(RandomUserService randomUserService) {
        this.randomUserService = randomUserService;
    }

    @Scheduled(fixedRate = 5000)
    public void feed() {
        randomUserService.processUsers();
    }
}
