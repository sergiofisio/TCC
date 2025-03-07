package com.example.emotionharmony.pages.meditation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.example.emotionharmony.utils.TTSHelper;

import org.json.JSONObject;

public class Meditation_Page5 extends AppCompatActivity {

    private TextView txtSpeech;
    private Questions_Meditation questionsMeditation;
    private EditText txtDescription;
    private TTSHelper ttsHelper;
    private CustomToast toast;
    private ImageView btnBack, btnFinish;

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

        toast = new CustomToast(this);
        txtSpeech = findViewById(R.id.txtSpeech5);
        ttsHelper = TTSHelper.getInstance();

        new Handler().postDelayed(()-> ttsHelper.speakText(this, txtSpeech), 1500);

        txtDescription = findViewById(R.id.txtDescEmocao);
        btnBack = findViewById(R.id.btnBack5);
        btnFinish = findViewById(R.id.btnNext5);

        questionsMeditation = Questions_Meditation.getInstance();

        if(questionsMeditation.getDescription()!=null){
            txtDescription.setText(questionsMeditation.getDescription());
        }

        btnFinish.setOnClickListener(v -> {
            try {
                ttsHelper.stopSpeaking();
                String description = txtDescription.getText().toString().trim();

                if (description.isEmpty()) {
                    throw new Exception("Descreva o que você poderia fazer diferente");
                }

                questionsMeditation.setDescription(description);



            } catch (Exception e) {
                toast.show(e.getMessage(), Toast.LENGTH_LONG, "#FF0000", "error");
            }
        });

        btnBack.setOnClickListener(v -> {
            ttsHelper.stopSpeaking();
            Intent intent = new Intent(Meditation_Page5.this, Meditation_Page_Emotions.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        findViewById(R.id.main).setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                hideKeyboard();
            }
            return false;
        });
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onDestroy() {
        if (ttsHelper != null) {
            ttsHelper.release();
        }
        super.onDestroy();
    }

    private void saveMeditationData(){
        try {
            String description = txtDescription.getText().toString().trim();

            if (description.isEmpty()) {
                throw new Exception("Descreva o que você poderia fazer diferente");
            }

            questionsMeditation.setCaracter(description);

            JSONObject meditationData = new JSONObject();
            meditationData.put("caracter_meditation", description);

        }catch(Exception e){
            toast.show(e.getMessage(), Toast.LENGTH_LONG, "#FF0000", "error");
        }
    }
}
