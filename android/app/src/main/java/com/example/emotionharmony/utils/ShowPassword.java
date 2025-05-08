package com.example.emotionharmony.utils;

import android.text.InputType;
import android.widget.EditText;

/**
 * Classe utilitária para alternar a visibilidade da senha em um campo de texto (EditText).
 */
public class ShowPassword {

    /**
     * Bloco: Alterna entre mostrar e esconder o texto da senha.
     *
     * @param editText Campo de senha a ser modificado.
     * @param isChecked Se verdadeiro, mostra a senha; se falso, esconde.
     */
    public static void ChangeShowPassword(EditText editText, boolean isChecked) {
        // Se estiver marcado (checkbox ou switch), mostra a senha em texto visível.
        if (isChecked) {
            editText.setInputType(
                    InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            );
        } else {
            // Caso contrário, aplica o modo de senha (oculta os caracteres).
            editText.setInputType(
                    InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
            );
        }

        // Mantém o cursor no final do texto após a mudança de tipo.
        editText.setSelection(editText.getText().length());
    }
}
