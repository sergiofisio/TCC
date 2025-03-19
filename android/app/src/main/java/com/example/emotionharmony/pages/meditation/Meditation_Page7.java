package com.example.emotionharmony.pages.meditation;

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

import com.example.emotionharmony.R;
import com.example.emotionharmony.classes.Questions_Meditation;
import com.example.emotionharmony.pages.Page_Exercicies;
import com.example.emotionharmony.utils.ServerConnection;
import com.example.emotionharmony.utils.TTSHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class Meditation_Page7 extends AppCompatActivity {

    private Questions_Meditation questionsMeditation;
    private TTSHelper ttsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meditation_page7);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView btnFinish = findViewById(R.id.btnFinish);
        ImageView btnBack = findViewById(R.id.btnBack7);
        TextView txtSpeech = findViewById(R.id.txtSpeech6);
        ttsHelper = TTSHelper.getInstance(this);
        questionsMeditation = Questions_Meditation.getInstance();

        String typeOf = questionsMeditation.getTypeSituation();
        txtSpeech.setText(typeOf.equals("Ruim") ? getString(R.string.exercicioBad) : getString(R.string.exercicioGood));
        new Handler().postDelayed(()-> ttsHelper.speakText(txtSpeech.getText().toString()), 1500);

        btnFinish.setOnClickListener(v -> {
            ttsHelper.stopSpeaking();
            sendMeditationData();
        });

        btnBack.setOnClickListener(v -> {
            ttsHelper.stopSpeaking();
            Intent intent = new Intent(Meditation_Page7.this, Meditation_Page6.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
    }

    private JSONObject createMeditationData() throws JSONException {
        JSONObject meditationData = new JSONObject();
        meditationData.put("think_today_meditation", questionsMeditation.getThinkToday());
        meditationData.put("emotion_meditation", questionsMeditation.getEmotion());
        meditationData.put("caracter_meditation", questionsMeditation.getCaracter());
        meditationData.put("type_situation_meditation", questionsMeditation.getTypeSituation());
        meditationData.put("description_meditation", questionsMeditation.getDescription());
        return meditationData;
    }

    private void sendMeditationData() {
        try {
            SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            String token = preferences.getString("authToken", null);

            if (token == null) {
                Toast.makeText(this, "Erro: Usuário não autenticado!", Toast.LENGTH_LONG).show();
                return;
            }

            JSONObject meditationData = createMeditationData();

            ServerConnection.postRequestWithAuth("/auth/add/meditate", token, meditationData, new ServerConnection.ServerCallback() {
                @Override
                public void onSuccess(String response) {
                    runOnUiThread(() -> {
                        Toast.makeText(Meditation_Page7.this, "✅ Meditação salva com sucesso!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Meditation_Page7.this, Page_Exercicies.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        Toast.makeText(Meditation_Page7.this, "❌ Erro ao salvar meditação: " + error, Toast.LENGTH_LONG).show();
                        Log.e("Meditation_Page7", "Erro na API: " + error);
                    });
                }
            });

        } catch (JSONException e) {
            Toast.makeText(this, "❌ Erro ao processar os dados!", Toast.LENGTH_LONG).show();
            Log.e("Meditation_Page7", "Erro JSON: " + e.getMessage());
        }
    }
}
