package com.example.emotionharmony.classes;

public class Questions_Breath {

    private static Questions_Breath instance;
    private Boolean finished_breath, feel_bether_breath;
    private String description_breath;

    private Questions_Breath() {}

    public static Questions_Breath getInstance() {
        if (instance == null) {
            instance = new Questions_Breath();
        }
        return instance;
    }

    public Boolean getFinished_breath() {
        return finished_breath;
    }

    public void setFinished_breath(Boolean finished_breath) {
        this.finished_breath = finished_breath;
    }

    public Boolean getFeel_bether_breath() {
        return feel_bether_breath;
    }

    public void setFeel_bether_breath(Boolean feel_bether_breath) {
        this.feel_bether_breath = feel_bether_breath;
    }

    public String getDescription_breath() {
        return description_breath;
    }

    public void setDescription_breath(String description_breath) {
        this.description_breath = description_breath;
    }
}
