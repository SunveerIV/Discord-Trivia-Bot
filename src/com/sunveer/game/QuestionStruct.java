package com.sunveer.game;

import com.google.gson.annotations.SerializedName;

class QuestionStruct implements Question{
    @SerializedName("question")
    public String questionText;

    @SerializedName("answer")
    public String answerText;

    QuestionStruct(String questionText, String answerText) {
        this.questionText = questionText;
        this.answerText = answerText;
    }

    @Override
    public String questionText() {
        return questionText;
    }

    @Override
    public String answerText() {
        return answerText;
    }
}