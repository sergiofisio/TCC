package com.example.emotionharmony.pages.meditation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.emotionharmony.classes.FirstQuestions;
import com.example.emotionharmony.utils.TTSHelper;

public class After_Login_Page5 extends AppCompatActivity {

    private TextView txtSpeech;
    private FirstQuestions firstQuestions;
    private EditText txtDescription;
    private TTSHelper ttsHelper;
    private CustomToast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_after_login_page5);
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
        ImageView btnBack1 = findViewById(R.id.btnBack3), btnNext2 = findViewById(R.id.btnNext4);

        firstQuestions = FirstQuestions.getInstance();

        if(firstQuestions.getDescription()!=null){
            txtDescription.setText(firstQuestions.getDescription());
        }

        btnNext2.setOnClickListener(v -> {
            try {
                ttsHelper.stopSpeaking();
                String description = txtDescription.getText().toString().trim();

                if (description.isEmpty()) {
                    throw new Exception("Descreva o que você poderia fazer diferente");
                }

                firstQuestions.setDescription(description);



            } catch (Exception e) {
                toast.show(e.getMessage(), Toast.LENGTH_LONG, "#FF0000", "error");
            }
        });

        btnBack1.setOnClickListener(v -> {
            ttsHelper.stopSpeaking();
            Intent intent = new Intent(After_Login_Page5.this, After_Login_Page_Emotions.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
