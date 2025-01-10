package com.example.emotionharmony.classes;

public class FiirstQuestions {
    private static FiirstQuestions instance;

    private String question1, question2, emotion, description;

    private FiirstQuestions() {}

    public static FiirstQuestions getInstance() {
        if (instance == null) {
            instance = new FiirstQuestions();
        }
        return instance;
    }

    public String getQuestion1() {
        return question1;
    }

    public void setQuestion1(String question1) {
        this.question1 = question1;
    }

    public String getQuestion2() {
        return question2;
    }

    public void setQuestion2(String question2) {
        this.question2 = question2;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
