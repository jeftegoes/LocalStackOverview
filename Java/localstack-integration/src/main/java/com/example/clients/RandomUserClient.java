package com.example.clients;

import com.example.clients.responses.RandomUserResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class RandomUserClient {
    @Value("${random-user-uri}")
    private String baseUri;

    public RandomUserResponse getRandomUser(String nationality) {
        RestClient restClient = RestClient.builder().baseUrl(baseUri).build();

        ResponseEntity<RandomUserResponse> response = restClient
                .get()
                .uri(
                        uriBuilder -> uriBuilder
                                .path("api")
                                .queryParam("nat", nationality).build())
                .header("Accept", "application/json")
                .retrieve()
                .toEntity(RandomUserResponse.class);

        return response.getBody();
    }
}
