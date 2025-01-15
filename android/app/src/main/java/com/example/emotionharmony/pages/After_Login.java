package com.example.emotionharmony.pages;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.emotionharmony.R;
import com.example.emotionharmony.databinding.ActivityAfterLoginBinding;
import com.example.emotionharmony.pages.breath.Breath_Page1;
import com.example.emotionharmony.pages.meditation.After_Login_Page1;

public class After_Login extends AppCompatActivity {

    private ActivityAfterLoginBinding binding;

    Button btnFeel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAfterLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBreath.setOnClickListener(v ->
                navigateTo(Breath_Page1.class)
        );
        binding.btnMed.setOnClickListener(v ->
                navigateTo(After_Login_Page1.class)
        );

        }

        private void navigateTo(Class<?> TargetActivity){

            System.out.println("Ação padrão do botão "+TargetActivity.getSimpleName());

            Intent intent = new Intent(After_Login.this, TargetActivity);
            startActivity(intent);

            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            finish();
        }
}
