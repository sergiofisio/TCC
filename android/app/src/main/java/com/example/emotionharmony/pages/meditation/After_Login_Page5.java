package com.example.emotionharmony.pages.meditation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.emotionharmony.R;
import com.example.emotionharmony.classes.FirstQuestions;

public class After_Login_Page5 extends AppCompatActivity {

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

        EditText txtDescription = findViewById(R.id.txtDescEmocao);
        ImageView btnBack1 = findViewById(R.id.btnBack3), btnNext2 = findViewById(R.id.btnNext4);

        FirstQuestions firstQuestions = FirstQuestions.getInstance();

        btnNext2.setOnClickListener(v -> {
            try {
                String description = txtDescription.getText().toString().trim();

                if (description.isEmpty()) {
                    throw new Exception("Descreva o que você poderia fazer diferente");
                }

                firstQuestions.setDescription(description);
                String msg = "questão 1: " + firstQuestions.getQuestion1() +
                        " questão 2: " + firstQuestions.getQuestion2() +
                        " emoção: " + firstQuestions.getEmotion() +
                        " questão 3: " + firstQuestions.getDescription();
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        btnBack1.setOnClickListener(v -> {
            Intent intent = new Intent(After_Login_Page5.this, After_Login_Page_Emotions.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
    }
}
