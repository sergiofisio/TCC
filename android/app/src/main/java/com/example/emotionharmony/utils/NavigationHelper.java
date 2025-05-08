package com.example.emotionharmony.utils;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.emotionharmony.R;

/**
 * Classe utilitária que centraliza operações de navegação e manipulação do teclado.
 * Facilita a transição entre atividades e melhora a experiência do usuário.
 */
public class NavigationHelper {

    /**
     * Bloco: Navegação entre atividades com animação personalizada.
     *
     * Para de falar o TTS (caso esteja ativo), esconde o teclado,
     * inicia a nova activity e aplica uma transição animada.
     *
     * @param currentActivity Atividade atual
     * @param targetActivity  Próxima atividade a ser exibida
     * @param slideRight      Define o tipo de animação: true = direita, false = esquerda
     */
    public static void navigateTo(Activity currentActivity, Class<?> targetActivity, boolean slideRight){
        // Para o texto falado (Text-to-Speech), se estiver ativo
        TTSHelper.getInstance(currentActivity).stopSpeaking();

        // Esconde o teclado (se estiver visível)
        hideKeyboard(currentActivity);

        // Cria e inicia a nova activity
        Intent intent = new Intent(currentActivity, targetActivity);
        currentActivity.startActivity(intent);

        // Aplica a animação de transição
        if(slideRight){
            currentActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else{
            currentActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    /**
     * Bloco: Esconde o teclado virtual da tela.
     *
     * Verifica se há uma view com foco e solicita que o InputMethodManager esconda o teclado.
     *
     * @param activity A activity onde o teclado deve ser escondido
     */
    public static void hideKeyboard(Activity activity){
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}
