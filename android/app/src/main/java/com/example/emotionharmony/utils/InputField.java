package com.example.emotionharmony.utils;

/**
 * Classe modelo simples (POJO) usada para representar um campo de entrada (formulário).
 * Pode ser útil para validação, exibição dinâmica ou envio de dados.
 */
public class InputField {

    // Bloco: Atributos
    // Nome do campo (ex: "email", "senha", etc.)
    private final String name;

    // Valor atual do campo
    private final String value;

    /**
     * Bloco: Construtor
     * Construtor que inicializa o nome e o valor do campo.
     *
     * @param name Nome do campo
     * @param value Valor inserido no campo
     */
    public InputField(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Bloco: Getter - Nome
     * Retorna o nome do campo.
     *
     * @return nome do campo
     */
    public String getName() {
        return name;
    }

    /**
     * Bloco: Getter - Valor
     * Retorna o valor do campo.
     *
     * @return valor inserido
     */
    public String getValue() {
        return value;
    }
}
