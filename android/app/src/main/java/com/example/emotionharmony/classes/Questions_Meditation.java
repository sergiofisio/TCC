package com.example.emotionharmony.classes;

public class Questions_Meditation {

    private static Questions_Meditation instance;

    private String thinkToday, emotion, caracter, typeSituation, feltAfter, description;

    private Questions_Meditation() {}

    public static Questions_Meditation getInstance() {
        if (instance == null) {
            instance = new Questions_Meditation();
        }
        return instance;
    }

    public String getThinkToday() {
        return thinkToday;
    }

    public void setThinkToday(String thinkToday) {
        this.thinkToday = thinkToday;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public String getCaracter() {
        return caracter;
    }

    public void setCaracter(String caracter) {
        this.caracter = caracter;
    }

    public String getTypeSituation() {
        return typeSituation;
    }

    public void setTypeSituation(String typeSituation) {
        this.typeSituation = typeSituation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
