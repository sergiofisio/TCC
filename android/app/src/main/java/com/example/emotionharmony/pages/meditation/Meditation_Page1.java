package com.example.emotionharmony.pages.meditation;

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

public class Meditation_Page1 extends AppCompatActivity {

    private TextView txtSpeech;
    private MediaPlayer mediaPlayer;
    private TTSHelper ttsHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation_page1);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtSpeech = findViewById(R.id.txtSpeech1);
        ImageView btnNext = findViewById(R.id.btnNext1);
        Button btnSair = findViewById(R.id.btnSair);

        ttsHelper = TTSHelper.getInstance(this);

        new Handler().postDelayed(()-> ttsHelper.speakText(txtSpeech.getText().toString()), 1500);

        btnNext.setOnClickListener(v ->  NavigationHelper.navigateTo(Meditation_Page1.this, Meditation_Page2.class, true));

        btnSair.setOnClickListener(v -> NavigationHelper.navigateTo(Meditation_Page1.this, Page_Exercicies.class, false));

        findViewById(R.id.main).setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                NavigationHelper.hideKeyboard(Meditation_Page1.this);
                v.performClick();
                return true;
            }
            return false;
        });
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