package com.example.emotionharmony.classes;

public class BreathingExercice {

    private static BreathingExercice instance;

    private boolean endBreath, howFeel;

    private String Commennt;

    private BreathingExercice(){}

    public static BreathingExercice getInstance(){
        if(instance==null) {
            instance = new BreathingExercice();
        }
        return instance;
    }

    public boolean isEndBreath() {
        return endBreath;
    }

    public void setEndBreath(boolean endBreath) {
        this.endBreath = endBreath;
    }

    public boolean isHowFeel() {
        return howFeel;
    }

    public void setHowFeel(boolean howFeel) {
        this.howFeel = howFeel;
    }

    public String getCommennt() {
        return Commennt;
    }

    public void setCommennt(String commennt) {
        Commennt = commennt;
    }
}
