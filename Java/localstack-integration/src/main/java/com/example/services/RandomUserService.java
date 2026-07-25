package com.example.services;

import com.example.clients.RandomUserClient;
import com.example.clients.responses.RandomUserResponse;
import com.example.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Component
public class RandomUserService {
    @Value("${random-user-queue}")
    private String randomUserQueue;

    private final RandomUserClient randomUserClient;
    private final SqsClient sqsClient;

    public RandomUserService(RandomUserClient randomUserClient, SqsClient sqsClient) {
        this.randomUserClient = randomUserClient;
        this.sqsClient = sqsClient;
    }

    public void processUsers() {
        User user = this.getRandomUser();

        publishQueue(user);

        System.out.println(user.getFirst());
    }

    private void publishQueue(User user) {
        SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                .queueUrl(randomUserQueue)
                .messageBody(user.toString())
                .build();

        this.sqsClient.sendMessage(sendMsgRequest);
    }

    private User getRandomUser() {
        User user = new User();

        RandomUserResponse randomUserResponse = this.randomUserClient.getRandomUser("br");

        randomUserResponse.getResults().forEach(randomUser -> {
            RandomUserResponse.Name name = randomUser.getName();

            user.setFirst(name.getFirst());
            user.setLast(name.getLast());
            user.setTitle(name.getTitle());
        });

        return user;
    }
}
