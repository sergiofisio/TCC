package com.example.emotionharmony.utils;

import android.util.Patterns;

import java.util.List;

public class Validator {

    public static void validateFields(List<InputField> fields) throws Exception {
        for (InputField field : fields) {
            if (field.getValue().isEmpty()) throw new Exception(field.getName() + " não pode estar vazio");
            if (field.getName().equalsIgnoreCase("Email") && !isValidEmail(field.getValue())) throw new Exception("Email inválido");
            if (field.getName().equalsIgnoreCase("CPF") && !isValidCPF(field.getValue())) throw new Exception("CPF inválido");
        }
    }

    public static boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidCPF(String cpf) {
        cpf = cpf.replaceAll("[^0-9]", "");

        if (cpf.length() != 11) return false;

        if (cpf.matches("(\\d)\\1{10}")) return false;

        int[] pesos = {10, 9, 8, 7, 6, 5, 4, 3, 2};
        int soma = 0, resto;

        for (int i = 0; i < 9; i++) {
            soma += (cpf.charAt(i) - '0') * pesos[i];
        }

        resto = soma % 11;
        int digito1 = (resto < 2) ? 0 : 11 - resto;

        soma = 0;
        int[] pesos2 = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
        for (int i = 0; i < 10; i++) {
            soma += (cpf.charAt(i) - '0') * pesos2[i];
        }

        resto = soma % 11;
        int digito2 = (resto < 2) ? 0 : 11 - resto;

        return (digito1 == (cpf.charAt(9) - '0')) && (digito2 == (cpf.charAt(10) - '0'));
    }
}
