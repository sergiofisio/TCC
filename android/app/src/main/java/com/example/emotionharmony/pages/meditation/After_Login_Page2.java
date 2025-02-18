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

import com.example.emotionharmony.CustomToast;
import com.example.emotionharmony.R;
import com.example.emotionharmony.classes.FirstQuestions;

public class After_Login_Page2 extends AppCompatActivity {

    private EditText txtDescription;
    private FirstQuestions firstQuestions;

    private CustomToast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_after_login_page2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toast = new CustomToast(this);

        txtDescription = findViewById(R.id.txtDescription);
        ImageView btnBack1 = findViewById(R.id.btnBack1), btnNext2 = findViewById(R.id.btnNext2);

        firstQuestions = FirstQuestions.getInstance();

        if(firstQuestions.getQuestion1()!=null){
            txtDescription.setText(firstQuestions.getQuestion1());
        }

        btnNext2.setOnClickListener(v -> {
            try {
                String description = txtDescription.getText().toString().trim();

                if (description.isEmpty()) {
                    throw new Exception("Descreva sua situação");
                }

                firstQuestions.setQuestion1(description);

                Intent intent = new Intent(After_Login_Page2.this, After_Login_Page3.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            } catch (Exception e) {
                toast.show(e.getMessage(), Toast.LENGTH_LONG, "#FF0000", "error");
            }
        });

        btnBack1.setOnClickListener(v -> {
            Intent intent = new Intent(After_Login_Page2.this, After_Login_Page1.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        });
    }
}
