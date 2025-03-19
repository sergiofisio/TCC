package com.example.emotionharmony.pages.meditation;

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

public class Meditation_Page5 extends AppCompatActivity {

    private TextView txtSpeech;
    private RadioButton[] radioButtons;
    private final String[] emotions = {"Raiva", "Desgosto", "Medo", "Tristeza", "Felicidade"};
    private String Emotion;
    private Questions_Meditation firstQuestions;
    private CustomToast toast;
    private TTSHelper ttsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meditation_page5);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtSpeech = findViewById(R.id.txtSpeech4);
        ttsHelper = TTSHelper.getInstance(this);

        new Handler().postDelayed(() -> ttsHelper.speakText(txtSpeech.getText().toString()), 1500);

        toast = new CustomToast(this);
        firstQuestions = Questions_Meditation.getInstance();
        Emotion = firstQuestions.getEmotion();

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

        for (int i = 0; i < linearLayouts.length; i++) {
            int index = i;
            linearLayouts[i].setOnClickListener(v -> handleRadioButtonSelection(index));
        }

        if (Emotion != null) {
            for (int i = 0; i < emotions.length; i++) {
                if (emotions[i].equals(Emotion)) {
                    radioButtons[i].setChecked(true);
                    break;
                }
            }
        }

        ImageView btnNext = findViewById(R.id.btnNext5);
        ImageView btnBack = findViewById(R.id.btnBack5);

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

        btnBack.setOnClickListener(v -> NavigationHelper.navigateTo(Meditation_Page5.this, Meditation_Page4.class, false));
    }

    private void handleRadioButtonSelection(int selectedIndex) {
        for (int i = 0; i < radioButtons.length; i++) {
            radioButtons[i].setChecked(i == selectedIndex);
            if (i == selectedIndex) {
                Emotion = emotions[i];
                ttsHelper.speakText(Emotion);
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (ttsHelper != null) {
            ttsHelper.release();
        }
        super.onDestroy();
    }
}