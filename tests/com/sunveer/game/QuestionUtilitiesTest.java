package com.sunveer.game;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class QuestionUtilitiesTest {

    @Test
    void testSingleQuestionInFile() throws IOException {
        List<Question> a = QuestionUtilities.getQuestionsFromFile("tests/com/sunveer/game/singleQuestionFile.txt");

        assertEquals(1, a.size());

        Question onlyQuestion = a.getFirst();

        assertEquals("What is 1+1?", onlyQuestion.toString());

        Answer[] expectedAnswers = {
                new Answer("1", false),
                new Answer("2", true),
                new Answer("3", false),
                new Answer("4", false)
        };
        Answer[] actualAnswers = onlyQuestion.getCandidateAnswers();
        for (int i = 0; i < expectedAnswers.length; i++) {
            assertEquals(expectedAnswers[i], actualAnswers[i]);
        }
    }

    @Test
    void testMultipleQuestionsInFile() throws IOException {
        List<Question> a = QuestionUtilities.getQuestionsFromFile("tests/com/sunveer/game/multipleQuestionsFile.txt");

        assertEquals(2, a.size());

        Question firstQuestion = a.get(0);

        assertEquals("What is 1+1?", firstQuestion.toString());

        Answer[] expectedAnswers = {
                new Answer("1", false),
                new Answer("2", true),
                new Answer("3", false),
                new Answer("4", false)
        };
        Answer[] actualAnswers = firstQuestion.getCandidateAnswers();
        for (int i = 0; i < expectedAnswers.length; i++) {
            assertEquals(expectedAnswers[i], actualAnswers[i]);
        }

        Question secondQuestion = a.get(1);

        assertEquals("What is lead's symbol on the periodic table?", secondQuestion.toString());

        Answer[] expectedAnswers2 = {
                new Answer("Ld", false),
                new Answer("Ht", false),
                new Answer("Pb", true),
                new Answer("Le", false)
        };
        Answer[] actualAnswers2 = secondQuestion.getCandidateAnswers();
        for (int i = 0; i < expectedAnswers2.length; i++) {
            assertEquals(expectedAnswers2[i], actualAnswers2[i]);
        }
    }
}