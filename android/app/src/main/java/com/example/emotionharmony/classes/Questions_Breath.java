// Pacote onde a classe está localizada
package com.example.emotionharmony.classes;

/**
 * Classe Singleton que armazena temporariamente as respostas do usuário
 * sobre o exercício de respiração.
 */
public class Questions_Breath {

    // Instância única da classe (padrão Singleton)
    private static Questions_Breath instance;

    // Atributos relacionados às respostas do exercício de respiração
    private Boolean finished_breath, feel_bether_breath;
    private String description_breath;

    // Construtor privado para garantir que a instância só possa ser criada internamente
    private Questions_Breath() {}

    /**
     * Retorna a instância única da classe (Singleton).
     * Se ainda não existir, cria uma nova.
     */
    public static Questions_Breath getInstance() {
        if (instance == null) {
            instance = new Questions_Breath();
        }
        return instance;
    }

    // Getter e Setter para saber se finalizou o exercício de respiração
    public Boolean getFinished_breath() {
        return finished_breath;
    }

    public void setFinished_breath(Boolean finished_breath) {
        this.finished_breath = finished_breath;
    }

    // Getter e Setter para saber se sentiu melhora após a respiração
    public Boolean getFeel_bether_breath() {
        return feel_bether_breath;
    }

    public void setFeel_bether_breath(Boolean feel_bether_breath) {
        this.feel_bether_breath = feel_bether_breath;
    }

    // Getter e Setter para descrição pessoal após o exercício
    public String getDescription_breath() {
        return description_breath;
    }

    public void setDescription_breath(String description_breath) {
        this.description_breath = description_breath;
    }
}
