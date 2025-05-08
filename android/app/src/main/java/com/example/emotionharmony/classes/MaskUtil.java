// Pacote onde a classe está localizada
package com.example.emotionharmony.classes;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Classe utilitária para aplicar máscara de entrada em campos EditText.
 * Exemplo de máscara: "###.###.###-##" para CPF.
 */
public class MaskUtil {

    /**
     * Aplica uma máscara dinâmica a um EditText.
     *
     * @param editText Campo de texto onde a máscara será aplicada.
     * @param mask     Máscara desejada (ex: "(##) #####-####").
     * @return TextWatcher que aplica a máscara enquanto o usuário digita.
     */
    public static TextWatcher applyMask(final EditText editText, final String mask) {
        return new TextWatcher() {

            boolean isUpdating;
            String oldText = "";

            // Antes da mudança (não utilizado, mas necessário para o contrato da interface)
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            // Durante a digitação
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String currentText = unmask(s.toString()); // Remove caracteres não numéricos
                StringBuilder formattedText = new StringBuilder();

                // Se já estiver atualizando, apenas atualiza o oldText e sai
                if (isUpdating) {
                    oldText = currentText;
                    isUpdating = false;
                    return;
                }

                int i = 0;
                // Aplica a máscara caractere por caractere
                for (char m : mask.toCharArray()) {
                    if (m != '#' && currentText.length() > oldText.length()) {
                        // Adiciona o caractere fixo da máscara (ex: '.', '-', '(', ')')
                        formattedText.append(m);
                        continue;
                    }
                    try {
                        // Adiciona o próximo número do texto atual
                        formattedText.append(currentText.charAt(i));
                    } catch (Exception e) {
                        break; // Se acabar o texto, para
                    }
                    i++;
                }

                // Marca como atualização para não entrar em loop
                isUpdating = true;
                editText.setText(formattedText.toString());           // Atualiza o campo
                editText.setSelection(formattedText.length());        // Move o cursor para o final
            }

            // Depois da mudança (não utilizado, mas necessário para o contrato da interface)
            @Override
            public void afterTextChanged(Editable s) {}

            /**
             * Remove todos os caracteres não numéricos da string.
             * @param s texto original com máscara
             * @return texto sem pontuação
             */
            private String unmask(String s) {
                return s.replaceAll("\\D", ""); // Remove tudo que não é dígito
            }
        };
    }
}
