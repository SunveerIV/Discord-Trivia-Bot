package com.sunveer.game.question;

import com.google.gson.Gson;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class APIQuestionCreator implements QuestionCreator {



    private static final String URL = "https://api.api-ninjas.com/v1/trivia";
    private String apiKey;

    public APIQuestionCreator(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public Question newQuestion() throws QuestionNotAvailableException {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL))
                    .header("X-Api-Key", apiKey)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String json = response.body();

            Gson gson = new Gson();

            return gson.fromJson(json, QuestionStruct[].class)[0];
        } catch (IOException | InterruptedException e) {
            throw new QuestionNotAvailableException();
        }
    }
}
