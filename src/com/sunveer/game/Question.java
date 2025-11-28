package com.sunveer.game;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

public record Question(String questionText, String answerText) {
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
}
