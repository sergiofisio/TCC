// Pacote da quinta etapa da meditação
package com.example.emotionharmony.pages.meditation;

// Importações necessárias
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.emotionharmony.CustomToast;
import com.example.emotionharmony.R;
import com.example.emotionharmony.classes.Questions_Meditation;
import com.example.emotionharmony.utils.NavigationHelper;
import com.example.emotionharmony.utils.TTSHelper;

/**
 * Quinta tela da meditação: o usuário identifica a emoção que sentiu após a situação.
 */
public class Meditation_Page5 extends AppCompatActivity {

    // Componentes da tela
    private TextView txtSpeech;
    private RadioButton[] radioButtons;

    // Lista de emoções possíveis
    private final String[] emotions = {"Raiva", "Desgosto", "Medo", "Tristeza", "Felicidade"};
    private String Emotion;

    // Utilitários
    private Questions_Meditation firstQuestions;
    private CustomToast toast;
    private TTSHelper ttsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Usa layout moderno com barras transparentes
        setContentView(R.layout.activity_meditation_page5);

        // Ajuste automático de padding para evitar sobreposição com status/navigation bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializa componentes visuais e utilitários
        txtSpeech = findViewById(R.id.txtSpeech4);
        ttsHelper = TTSHelper.getInstance(this);
        toast = new CustomToast(this);
        firstQuestions = Questions_Meditation.getInstance();
        Emotion = firstQuestions.getEmotion();

        // Fala o texto da pergunta na tela após pequeno atraso
        new Handler().postDelayed(() ->
                ttsHelper.speakText(txtSpeech.getText().toString()), 1500
        );

        // Conjuntos de botões e áreas clicáveis (LinearLayouts que englobam os RadioButtons)
        LinearLayout[] linearLayouts = new LinearLayout[]{
                findViewById(R.id.rdbAng),
                findViewById(R.id.rdbDesg),
                findViewById(R.id.rdbFear),
                findViewById(R.id.rdbSad),
                findViewById(R.id.rdbHappy)
        };

        radioButtons = new RadioButton[]{
                findViewById(R.id.radioAngry),
                findViewById(R.id.radioDes),
                findViewById(R.id.radioFear),
                findViewById(R.id.radioSad),
                findViewById(R.id.radioHappy)
        };

        // Configura seleção via clique nos containers (LinearLayouts)
        for (int i = 0; i < linearLayouts.length; i++) {
            int index = i;
            linearLayouts[i].setOnClickListener(v -> handleRadioButtonSelection(index));
        }

        // Marca a emoção previamente escolhida, se houver
        if (Emotion != null) {
            for (int i = 0; i < emotions.length; i++) {
                if (emotions[i].equals(Emotion)) {
                    radioButtons[i].setChecked(true);
                    break;
                }
            }
        }

        // Botões de navegação
        ImageView btnNext = findViewById(R.id.btnNext5);
        ImageView btnBack = findViewById(R.id.btnBack5);

        // Avança para a próxima tela, validando a escolha
        btnNext.setOnClickListener(v -> {
            try {
                if (Emotion == null || Emotion.isEmpty()) {
                    throw new Exception("Escolha uma emoção");
                }

                firstQuestions.setEmotion(Emotion);

                NavigationHelper.navigateTo(Meditation_Page5.this, Meditation_Page6.class, true);

            } catch (Exception e) {
                toast.show(e.getMessage(), Toast.LENGTH_LONG, "#FF0000", "error");
            }
        });

        // Volta para a etapa anterior
        btnBack.setOnClickListener(v ->
                NavigationHelper.navigateTo(Meditation_Page5.this, Meditation_Page4.class, false)
        );
    }

    /**
     * Marca o botão selecionado e armazena a emoção escolhida, além de falar o nome com TTS.
     */
    private void handleRadioButtonSelection(int selectedIndex) {
        for (int i = 0; i < radioButtons.length; i++) {
            radioButtons[i].setChecked(i == selectedIndex);
            if (i == selectedIndex) {
                Emotion = emotions[i];
                ttsHelper.speakText(Emotion);
            }
        }
    }

    /**
     * Libera recursos do TTS ao sair da tela.
     */
    @Override
    protected void onDestroy() {
        if (ttsHelper != null) {
            ttsHelper.release();
        }
        super.onDestroy();
    }
}
