package com.sunveer.game.question;

public class MockQuestionCreator implements QuestionCreator {

    @Override
    public Question newQuestion() {
        return new QuestionStruct("Why?", "Because.");
    }
}
