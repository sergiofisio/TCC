// Pacote onde a classe está localizada
package com.example.emotionharmony.classes;

/**
 * Classe singleton que representa temporariamente os dados de uma emoção escolhida pelo usuário.
 * Armazena: emoção do dia, descrição e período do dia (manhã, tarde ou noite).
 */
public class Choose_Emotion_Db {

    // Instância única da classe (padrão Singleton)
    private static Choose_Emotion_Db instance;

    // Atributos da emoção selecionada
    private String emotion_today,description, morning_afternoon_evening;

    // Construtor privado para impedir instanciamento direto
    private Choose_Emotion_Db() {}

    /**
     * Retorna a instância única da classe (Singleton).
     * Cria uma nova instância se ainda não existir.
     */
    public static Choose_Emotion_Db getInstance() {
        if (instance == null) {
            instance = new Choose_Emotion_Db();
        }
        return instance;
    }

    // Getter e Setter para emotion_today
    public String getEmotion_today() {
        return emotion_today;
    }

    public void setEmotion_today(String emotion_today) {
        this.emotion_today = emotion_today;
    }

    // Getter e Setter para description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getter e Setter para morning_afternoon_evening
    public String getMorning_afternoon_evening() {
        return morning_afternoon_evening;
    }

    public void setMorning_afternoon_evening(String morning_afternoon_evening) {
        this.morning_afternoon_evening = morning_afternoon_evening;
    }
}
