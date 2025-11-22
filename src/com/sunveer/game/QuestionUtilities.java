package com.sunveer.game;

import java.io.*;
import java.util.*;

class QuestionUtilities {
    private QuestionUtilities() {}

    public static List<Question> getQuestionsFromFile(String filePath) throws IOException {
        List<Question> questions = new ArrayList<>();

        FileInputStream file = new FileInputStream(filePath);
        Scanner scnr = new Scanner(file);
        while (scnr.hasNextLine()) {
            String questionText = scnr.nextLine();
            if (questionText.isBlank()) break;
            List<Answer> answers = new ArrayList<>();
            while (true) {
                String answerText;
                try {
                    answerText = scnr.nextLine();
                } catch (NoSuchElementException e) {
                    break;
                }
                if (answerText == null || answerText.isBlank()) break;
                boolean isCorrect = Boolean.parseBoolean(scnr.nextLine());
                answers.add(new Answer(answerText, isCorrect));
            }
            questions.add(new Question(questionText, answers.toArray(new Answer[0])));
        }

        return questions;
    }
}
