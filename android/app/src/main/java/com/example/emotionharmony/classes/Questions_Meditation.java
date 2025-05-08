// Pacote onde a classe está localizada
package com.example.emotionharmony.classes;

/**
 * Classe Singleton que armazena temporariamente as respostas do usuário
 * sobre sua experiência na meditação do dia.
 */
public class Questions_Meditation {

    // Instância única da classe (padrão Singleton)
    private static Questions_Meditation instance;

    // Atributos que representam as respostas da meditação
    private String thinkToday, emotion, caracter,typeSituation, feltAfter,description;

    // Construtor privado para garantir controle de instância
    private Questions_Meditation() {}

    /**
     * Retorna a instância única da classe (Singleton).
     * Se ainda não existir, cria uma nova.
     */
    public static Questions_Meditation getInstance() {
        if (instance == null) {
            instance = new Questions_Meditation();
        }
        return instance;
    }

    // Getter e Setter para o pensamento dominante do dia
    public String getThinkToday() {
        return thinkToday;
    }

    public void setThinkToday(String thinkToday) {
        this.thinkToday = thinkToday;
    }

    // Getter e Setter para a emoção predominante
    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    // Getter e Setter para o traço de caráter
    public String getCaracter() {
        return caracter;
    }

    public void setCaracter(String caracter) {
        this.caracter = caracter;
    }

    // Getter e Setter para o tipo de situação vivenciada
    public String getTypeSituation() {
        return typeSituation;
    }

    public void setTypeSituation(String typeSituation) {
        this.typeSituation = typeSituation;
    }

    // Getter e Setter para como se sentiu após a meditação (esse campo está declarado mas sem uso no código original, pode ser adicionado se necessário)
    public String getFeltAfter() {
        return feltAfter;
    }

    public void setFeltAfter(String feltAfter) {
        this.feltAfter = feltAfter;
    }

    // Getter e Setter para a descrição livre da experiência
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
