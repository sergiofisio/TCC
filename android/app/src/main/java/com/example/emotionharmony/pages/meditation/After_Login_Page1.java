package com.example.emotionharmony.pages.meditation;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.emotionharmony.R;
import com.example.emotionharmony.pages.After_Login;
import com.example.emotionharmony.utils.GoogleCloudTTS;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class After_Login_Page1 extends AppCompatActivity {

    private TextView txtSpeech;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login_page1);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtSpeech = findViewById(R.id.txtSpeech);
        ImageView btnNext1 = findViewById(R.id.btnNext1);
        Button btnSair = findViewById(R.id.btnSair);

        new Handler().postDelayed(this::speakText, 1500);

        btnNext1.setOnClickListener(v -> {
            Intent intent = new Intent(After_Login_Page1.this, After_Login_Page2.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        btnSair.setOnClickListener(v -> {
            Intent intent = new Intent(After_Login_Page1.this, After_Login.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
    }

    private void speakText() {
        String text = txtSpeech.getText().toString().trim();

        if (text.isEmpty()) {
            Log.e("After_Login_Page1", "⚠️ Nenhum texto encontrado para leitura.");
            return;
        }

        Log.d("After_Login_Page1", "🎙️ Enviando texto para conversão: " + text);

        GoogleCloudTTS.synthesizeSpeech(text).thenAccept(audioBase64 -> {
            if (audioBase64 != null) {
                playAudio(audioBase64);
            } else {
                Log.e("After_Login_Page1", "❌ Falha ao obter áudio da API.");
            }
        }).exceptionally(e -> {
            Log.e("After_Login_Page1", "❌ Erro ao converter texto para fala: " + e.getMessage());
            return null;
        });
    }

    private void playAudio(String audioBase64) {
        try {
            byte[] audioData = Base64.decode(audioBase64, Base64.DEFAULT);
            File tempFile = File.createTempFile("tts_audio", ".mp3", getCacheDir());

            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(audioData);
            }

            if (mediaPlayer != null) {
                mediaPlayer.release();
            }

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(tempFile.getAbsolutePath());
            mediaPlayer.setOnPreparedListener(mp -> mediaPlayer.start());
            mediaPlayer.setOnCompletionListener(mp -> Log.d("After_Login_Page1", "✅ Reprodução concluída."));
            mediaPlayer.prepareAsync();

            Log.d("After_Login_Page1", "🔊 Reprodução de áudio iniciada.");
        } catch (IOException e) {
            Log.e("After_Login_Page1", "❌ Erro ao reproduzir áudio: " + e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
}
