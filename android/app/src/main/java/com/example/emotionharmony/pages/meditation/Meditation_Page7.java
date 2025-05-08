// Última etapa da sequência de meditação
package com.example.emotionharmony.pages.meditation;

// Importações necessárias
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
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
import com.example.emotionharmony.pages.Page_Exercicies;
import com.example.emotionharmony.utils.ServerConnection;
import com.example.emotionharmony.utils.TTSHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class Meditation_Page7 extends AppCompatActivity {

    // Instâncias e componentes da tela
    private Questions_Meditation questionsMeditation;
    private TTSHelper ttsHelper;
    private CustomToast toast;
    ImageView btnBack;
    TextView btnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meditation_page7);

        // Ajusta os paddings da tela conforme as barras do sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialização de variáveis e componentes
        btnFinish = findViewById(R.id.btnFinish);
        btnBack = findViewById(R.id.btnBack7);
        TextView txtSpeech = findViewById(R.id.txtSpeech6);

        ttsHelper = TTSHelper.getInstance(this);
        toast = new CustomToast(this);
        questionsMeditation = Questions_Meditation.getInstance();

        // Define o texto final da meditação conforme tipo de situação (boa ou ruim)
        String typeOf = questionsMeditation.getTypeSituation();
        txtSpeech.setText(typeOf.equals("Ruim") ? getString(R.string.exercicioBad) : getString(R.string.exercicioGood));

        // Fala o texto final após 1,5s
        new Handler().postDelayed(() ->
                ttsHelper.speakText(txtSpeech.getText().toString()), 1500
        );

        // Finalizar meditação e enviar dados
        btnFinish.setOnClickListener(v -> {
            ttsHelper.stopSpeaking();
            sendMeditationData();
        });

        // Voltar para tela anterior
        btnBack.setOnClickListener(v -> {
            ttsHelper.stopSpeaking();
            Intent intent = new Intent(Meditation_Page7.this, Meditation_Page6.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
    }

    // Monta os dados em JSON para envio ao servidor
    private JSONObject createMeditationData() throws JSONException {
        JSONObject meditationData = new JSONObject();
        meditationData.put("think_today_meditation", questionsMeditation.getThinkToday());
        meditationData.put("emotion_meditation", questionsMeditation.getEmotion());
        meditationData.put("caracter_meditation", questionsMeditation.getCaracter());
        meditationData.put("type_situation_meditation", questionsMeditation.getTypeSituation());
        meditationData.put("description_meditation", questionsMeditation.getDescription());
        return meditationData;
    }

    // Envia os dados da meditação via POST autenticado
    private void sendMeditationData() {
        try {
            SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            String token = preferences.getString("authToken", null);

            // Verifica se usuário está autenticado
            if (token == null) {
                Toast.makeText(this, "Erro: Usuário não autenticado!", Toast.LENGTH_LONG).show();
                return;
            }

            JSONObject meditationData = createMeditationData();

            // Feedback visual inicial
            toast.show("Finalizando Meditação", Toast.LENGTH_SHORT, "#11273D", "success");
            btnFinish.setEnabled(false);
            btnFinish.setTextColor(getColor(R.color.btnInactive));

            // Envia os dados ao endpoint da meditação
            ServerConnection.postRequestWithAuth("/activity/add/meditate", token, meditationData, new ServerConnection.ServerCallback() {
                @Override
                public void onSuccess(String response) {
                    runOnUiThread(() -> {
                        toast.show("Meditação Finalizada", Toast.LENGTH_LONG, "#11273D", "success");
                        Intent intent = new Intent(Meditation_Page7.this, Page_Exercicies.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        if (error.toLowerCase().contains("timeout")) {
                            sendMeditationData(); // Reenvia se erro for timeout
                        } else {
                            toast.show("❌ Erro ao salvar meditação: " + error, Toast.LENGTH_LONG, "#FF0000", "error");
                            Log.e("Page_End", "❌ Erro ao salvar meditação: " + error);
                        }
                    });
                }
            });

        } catch (JSONException e) {
            Toast.makeText(this, "❌ Erro ao processar os dados!", Toast.LENGTH_LONG).show();
            Log.e("Meditation_Page7", "Erro JSON: " + e.getMessage());
        }
    }
}
