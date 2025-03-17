package com.example.emotionharmony.classes;

public class Choose_Emotion {

    private static Choose_Emotion instance;

    private String emotion_today, description, morning_afternoon_evening;
    private Choose_Emotion() {}

    public static Choose_Emotion getInstance() {
        if (instance == null) {
            instance = new Choose_Emotion();
        }
        return instance;
    }

    public String getEmotion_today() {
        return emotion_today;
    }

    public void setEmotion_today(String emotion_today) {
        this.emotion_today = emotion_today;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMorning_afternoon_evening() {
        return morning_afternoon_evening;
    }

    public void setMorning_afternoon_evening(String morning_afternoon_evening) {
        this.morning_afternoon_evening = morning_afternoon_evening;
    }
}
