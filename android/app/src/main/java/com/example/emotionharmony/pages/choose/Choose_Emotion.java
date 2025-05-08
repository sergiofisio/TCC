// Pacote da tela de escolha da emoção
package com.example.emotionharmony.pages.choose;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
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
import com.example.emotionharmony.classes.Choose_Emotion_Db;
import com.example.emotionharmony.pages.Page_Exercicies;
import com.example.emotionharmony.utils.NavigationHelper;
import com.example.emotionharmony.utils.ServerConnection;
import com.example.emotionharmony.utils.TTSHelper;

import org.json.JSONObject;

/**
 * Tela que permite o usuário escolher uma emoção correspondente ao período do dia.
 * Usa TTS para leitura, salva localmente e envia a escolha ao servidor.
 */
public class Choose_Emotion extends AppCompatActivity {

    // UI
    private TextView txtSpeech;
    private RadioButton[] radioButtons;
    private final String[] emotions = {"Raiva", "Desgosto", "Medo", "Tristeza", "Felicidade"};
    private String Emotion;

    // Utilitários
    private TTSHelper ttsHelper;
    private CustomToast toast;
    private Choose_Emotion_Db chooseEmotionDb;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choose_emotion);

        // Ajusta o layout com padding do sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Recupera o período do dia (manhã, tarde, noite) passado por intent
        String periodo = getIntent().getStringExtra("PERIODO_DIA");

        // Inicialização de views e variáveis
        txtSpeech = findViewById(R.id.txtSpeechChoose);
        ttsHelper = TTSHelper.getInstance(this);
        chooseEmotionDb = Choose_Emotion_Db.getInstance();

        txtSpeech.setText("Qual a sua emoção pela " + periodo);

        // Fala o texto após um pequeno atraso
        new Handler().postDelayed(() -> ttsHelper.speakText(txtSpeech.getText().toString()), 1500);

        toast = new CustomToast(this);
        Emotion = chooseEmotionDb.getEmotion_today();

        // Botões de ação
        Button btnSairChoose = findViewById(R.id.btnSairChoose);
        TextView BtnFinish = findViewById(R.id.BtnFinishChoose);

        // Layouts clicáveis que envolvem os botões de emoção
        LinearLayout[] linearLayouts = new LinearLayout[]{
                findViewById(R.id.rdbAng),
                findViewById(R.id.rdbDesg),
                findViewById(R.id.rdbFear),
                findViewById(R.id.rdbSad),
                findViewById(R.id.rdbHappy)
        };

        // RadioButtons das emoções
        radioButtons = new RadioButton[]{
                findViewById(R.id.radioAngry),
                findViewById(R.id.radioDes),
                findViewById(R.id.radioFear),
                findViewById(R.id.radioSad),
                findViewById(R.id.radioHappy)
        };

        // Associa clique nos layouts com a seleção do botão correspondente
        for (int i = 0; i < linearLayouts.length; i++) {
            int index = i;
            linearLayouts[i].setOnClickListener(v -> handleRadioButtonSelection(index));
        }

        // Se já havia uma emoção escolhida, marca a correspondente
        if (Emotion != null) {
            for (int i = 0; i < emotions.length; i++) {
                if (emotions[i].equals(Emotion)) {
                    radioButtons[i].setChecked(true);
                    break;
                }
            }
        }

        // Botão sair retorna para a tela de exercícios
        btnSairChoose.setOnClickListener(v ->
                NavigationHelper.navigateTo(Choose_Emotion.this, Page_Exercicies.class, false)
        );

        // Botão finalizar valida, salva localmente e envia ao servidor
        BtnFinish.setOnClickListener(v -> {
            try {
                if (Emotion == null || Emotion.isEmpty()) {
                    throw new Exception("Escolha uma emoção");
                }

                chooseEmotionDb.setEmotion_today(Emotion);

                SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                String token = preferences.getString("authToken", null);
                if (token == null) {
                    throw new Exception("Usuário não autenticado");
                }

                JSONObject emotionData = new JSONObject();
                emotionData.put("emotion", Emotion);
                emotionData.put("description", "");
                emotionData.put("day_time", getIntent().getStringExtra("PERIODO_DIA"));

                ServerConnection.postRequestWithAuth("/emotion/add", token, emotionData, new ServerConnection.ServerCallback() {
                    @Override
                    public void onSuccess(String response) {
                        runOnUiThread(() -> {
                            toast.show("Emoção salva com sucesso!", Toast.LENGTH_LONG, "#11273D", "success");
                            NavigationHelper.navigateTo(Choose_Emotion.this, Page_Exercicies.class, true);
                        });
                    }

                    @Override
                    public void onError(String error) {
                        runOnUiThread(() -> {
                            toast.show("Erro ao salvar emoção: " + error, Toast.LENGTH_LONG, "#FF0000", "error");
                        });
                    }
                });

            } catch (Exception e) {
                toast.show(e.getMessage(), Toast.LENGTH_LONG, "#FF0000", "error");
            }
        });
    }

    /**
     * Trata a seleção de uma emoção. Marca apenas o botão escolhido e fala a emoção.
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
     * Libera o TTS ao destruir a tela.
     */
    @Override
    protected void onDestroy() {
        if (ttsHelper != null) {
            ttsHelper.release();
        }
        super.onDestroy();
    }
}
