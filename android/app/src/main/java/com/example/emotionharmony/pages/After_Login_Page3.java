package com.example.emotionharmony.pages;

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
import com.example.emotionharmony.classes.FiirstQuestions;

public class After_Login_Page3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_after_login_page3);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText txtDescription = findViewById(R.id.txtDescription2);
        ImageView btnBack1 = findViewById(R.id.btnBack2), btnNext2 = findViewById(R.id.btnNext3);

        FiirstQuestions firstQuestions = FiirstQuestions.getInstance();

        btnNext2.setOnClickListener(v -> {
            try {
                String description = txtDescription.getText().toString().trim();

                if (description.isEmpty()) {
                    throw new Exception("Descreva o que você poderia fazer diferente");
                }

                firstQuestions.setQuestion2(description);

                Intent intent = new Intent(After_Login_Page3.this, After_Login_Page_Emotions.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        btnBack1.setOnClickListener(v -> {
            Intent intent = new Intent(After_Login_Page3.this, After_Login_Page2.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
    }
}
