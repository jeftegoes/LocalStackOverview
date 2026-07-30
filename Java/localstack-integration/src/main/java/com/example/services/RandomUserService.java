package com.example.services;

import com.example.clients.RandomUserClient;
import com.example.clients.responses.RandomUserResponse;
import com.example.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Component
public class RandomUserService {
    @Value("${random-user-queue}")
    private String randomUserQueue;

    @Value("${random-user-topic}")
    private String randomUserTopic;

    private final RandomUserClient randomUserClient;
    private final SqsClient sqsClient;
    private final SnsClient snsClient;

    public RandomUserService(RandomUserClient randomUserClient,
                             SqsClient sqsClient,
                             SnsClient snsClient) {
        this.randomUserClient = randomUserClient;
        this.sqsClient = sqsClient;
        this.snsClient = snsClient;
    }

    public void processUsers() {
        User user = this.getRandomUser();

        publishQueue(user);
        publishTopic(user);

        System.out.println(user.getFirst());
    }

    private void publishTopic(User user) {
        PublishRequest request = PublishRequest.builder()
                .message("Hi " + user.getFirst())
                .topicArn(randomUserTopic)
                .build();

        PublishResponse result = snsClient.publish(request);
        System.out.println(result.messageId() + " Message sent. Status is " + result.sdkHttpResponse().statusCode());
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
