package com.example.emotionharmony.pages.meditation;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.EditText;
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
import com.example.emotionharmony.utils.NavigationHelper;
import com.example.emotionharmony.utils.TTSHelper;

public class Meditation_Page2 extends AppCompatActivity {

    private TextView txtSpeech;
    private EditText txtDescription;
    private Questions_Meditation questionsMeditation;
    private CustomToast toast;
    private TTSHelper ttsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meditation_page2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txtSpeech = findViewById(R.id.txtSpeech2);
        ttsHelper = TTSHelper.getInstance(this);

        new Handler().postDelayed(()-> ttsHelper.speakText(txtSpeech.getText().toString()), 1500);

        toast = new CustomToast(this);

        txtDescription = findViewById(R.id.txtDescription);
        ImageView btnBack = findViewById(R.id.btnBack2);
        ImageView btnNext = findViewById(R.id.btnNext2);

        questionsMeditation = Questions_Meditation.getInstance();

        if(questionsMeditation.getDescription()!=null){
            txtDescription.setText(questionsMeditation.getDescription());
        }

        btnNext.setOnClickListener(v -> {
            try {
                String description = txtDescription.getText().toString().trim();

                if (description.isEmpty()) {
                    throw new Exception("Descreva sua situação");
                }

                questionsMeditation.setDescription(description);

                NavigationHelper.navigateTo(Meditation_Page2.this, Meditation_Page3.class, true);

            } catch (Exception e) {
                toast.show(e.getMessage(), Toast.LENGTH_LONG, "#FF0000", "error");
            }
        });

        btnBack.setOnClickListener(v -> NavigationHelper.navigateTo(Meditation_Page2.this, Meditation_Page1.class, false));

        findViewById(R.id.main).setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                NavigationHelper.hideKeyboard(Meditation_Page2.this);
                v.performClick();
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onDestroy() {
        if (ttsHelper != null) {
            ttsHelper.release();
        }
        super.onDestroy();
    }
}