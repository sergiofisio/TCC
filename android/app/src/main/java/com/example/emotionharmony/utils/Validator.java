package com.example.emotionharmony.utils;

import android.util.Patterns;

import java.util.List;

/**
 * Classe utilitária para validação de campos como email e CPF.
 */
public class Validator {

    /**
     * Valida uma lista de campos, lançando exceção caso algum esteja inválido.
     * @param fields Lista de campos com nome e valor.
     * @throws Exception caso algum campo esteja vazio ou inválido.
     */
    public static void validateFields(List<InputField> fields) throws Exception {
        for (InputField field : fields) {
            // Verifica se o campo está vazio
            if (field.getValue().isEmpty())
                throw new Exception(field.getName() + " não pode estar vazio");

            // Valida formato de email se o campo for Email
            if (field.getName().equalsIgnoreCase("Email") && !isValidEmail(field.getValue()))
                throw new Exception("Email inválido");

            // Valida formato do CPF se o campo for CPF
            if (field.getName().equalsIgnoreCase("CPF") && !isValidCPF(field.getValue()))
                throw new Exception("CPF inválido");
        }
    }

    /**
     * Verifica se um email é válido usando o padrão do Android.
     * @param email Email a ser validado.
     * @return true se válido, false se inválido.
     */
    public static boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Valida um número de CPF com base nos dígitos verificadores.
     * @param cpf CPF formatado ou não (com ou sem pontos/traço).
     * @return true se CPF é válido, false se inválido.
     */
    public static boolean isValidCPF(String cpf) {
        // Remove qualquer caractere que não seja número
        cpf = cpf.replaceAll("[^0-9]", "");

        // Verifica se tem exatamente 11 dígitos
        if (cpf.length() != 11) return false;

        // Elimina CPFs com todos os dígitos iguais
        if (cpf.matches("(\\d)\\1{10}")) return false;

        // Calcula o primeiro dígito verificador
        int[] pesos = {10, 9, 8, 7, 6, 5, 4, 3, 2};
        int soma = 0, resto;
        for (int i = 0; i < 9; i++) {
            soma += (cpf.charAt(i) - '0') * pesos[i];
        }
        resto = soma % 11;
        int digito1 = (resto < 2) ? 0 : 11 - resto;

        // Calcula o segundo dígito verificador
        soma = 0;
        int[] pesos2 = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
        for (int i = 0; i < 10; i++) {
            soma += (cpf.charAt(i) - '0') * pesos2[i];
        }
        resto = soma % 11;
        int digito2 = (resto < 2) ? 0 : 11 - resto;

        // Compara os dígitos calculados com os do CPF
        return (digito1 == (cpf.charAt(9) - '0')) && (digito2 == (cpf.charAt(10) - '0'));
    }
}
