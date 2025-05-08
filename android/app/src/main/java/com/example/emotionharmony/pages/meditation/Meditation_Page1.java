// Pacote da tela de meditação
package com.example.emotionharmony.pages.meditation;

// Importações necessárias
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.emotionharmony.R;
import com.example.emotionharmony.pages.Page_Exercicies;
import com.example.emotionharmony.utils.NavigationHelper;
import com.example.emotionharmony.utils.TTSHelper;

/**
 * Primeira tela do exercício de meditação guiada.
 * Apresenta uma introdução com leitura em voz alta e permite avançar ou sair.
 */
public class Meditation_Page1 extends AppCompatActivity {

    // Componentes da UI e utilitários
    private TextView txtSpeech;
    private MediaPlayer mediaPlayer;
    private TTSHelper ttsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation_page1);

        // Ajusta margens para não sobrepor a status bar ou barra de navegação
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Referência ao texto que será lido em voz alta
        txtSpeech = findViewById(R.id.txtSpeech1);
        ImageView btnNext = findViewById(R.id.btnNext1);      // Botão "próximo"
        Button btnSair = findViewById(R.id.btnSair);          // Botão "sair"

        // Inicializa o helper de voz
        ttsHelper = TTSHelper.getInstance(this);

        // Aguarda 1,5s e fala o texto da tela
        new Handler().postDelayed(() ->
                ttsHelper.speakText(txtSpeech.getText().toString()), 1500
        );

        // Avança para a próxima etapa da meditação
        btnNext.setOnClickListener(v ->
                NavigationHelper.navigateTo(Meditation_Page1.this, Meditation_Page2.class, true)
        );

        // Sai da meditação e volta à tela principal de exercícios
        btnSair.setOnClickListener(v ->
                NavigationHelper.navigateTo(Meditation_Page1.this, Page_Exercicies.class, false)
        );

        // Fecha o teclado caso o usuário toque fora do campo de texto
        findViewById(R.id.main).setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                NavigationHelper.hideKeyboard(Meditation_Page1.this);
                v.performClick();
                return true;
            }
            return false;
        });
    }

    /**
     * Libera o media player ao sair da tela, se estiver em uso.
     */
    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
}
