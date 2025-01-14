package com.example.emotionharmony.pages.meditation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.emotionharmony.R;
import com.example.emotionharmony.classes.FirstQuestions;

public class After_Login_Page_Emotions extends AppCompatActivity {

    private LinearLayout[] linearLayouts;
    private RadioButton[] radioButtons;
    private String[] emotions = {"Raiva", "Desgosto", "Medo", "Tristeza", "Felicidade"};

    private String Emotion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_after_login_page_emotions);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FirstQuestions firstQuestions = FirstQuestions.getInstance();

        linearLayouts = new LinearLayout[]{
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

        ImageView btnNext = findViewById(R.id.btnNext), btnBack=findViewById(R.id.btnBack);

        btnNext.setOnClickListener(v ->{
            try {

                if(Emotion.isEmpty()){
                    throw new Exception("Escolha uma emoção");
                }

                firstQuestions.setEmotion(Emotion);

                Intent intent = new Intent(After_Login_Page_Emotions.this, After_Login_Page5.class);

                startActivity(intent);

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }catch(Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        btnBack.setOnClickListener(v ->{
            Intent intent = new Intent(After_Login_Page_Emotions.this, After_Login_Page3.class);

            startActivity(intent);

            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        });
    }

    private void handleRadioButtonSelection(int selectedIndex) {
        for (int i = 0; i < radioButtons.length; i++) {
            radioButtons[i].setChecked(i == selectedIndex);
            if (i == selectedIndex) {
                Emotion = emotions[i];
            }
        }
    }
}