package com.sunveer.game;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class QuestionCreator {



    private static final String URL = "https://api.api-ninjas.com/v1/trivia";
    private static String API_KEY;

    static {
        initializeApiKey();
    }

    private static void initializeApiKey() {
        try {
            Dotenv dotenv = Dotenv.load();
            API_KEY = dotenv.get("API_NINJAS_KEY");
            System.out.println("API Ninjas Key Loaded Successfully!");
        } catch (DotenvException e) {
            System.err.println("Dotenv could not be loaded. Stopping Process.");
            System.exit(1);
        }

        if (API_KEY == null) {
            System.err.println("API Ninjas Key could not be loaded. Stopping Process.");
            System.exit(1);
        }
    }

    public static Question newQuestion() throws QuestionNotAvailableException {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL))
                    .header("X-Api-Key", API_KEY)
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
