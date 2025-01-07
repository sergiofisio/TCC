package com.example.emotionharmony;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.os.HandlerCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ProgressBar carregamento = findViewById(R.id.carregamento);
        TextView txtCarregando = findViewById(R.id.txtCarregando);

        carregamento.setVisibility(View.VISIBLE);

        LoadDatabase(carregamento, txtCarregando);
    }

    private void LoadDatabase(ProgressBar carregamento, TextView txtCarregando) {
        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            HandlerCompat.createAsync(Looper.getMainLooper()).post(() -> {
                carregamento.setVisibility(View.GONE);
                txtCarregando.setVisibility(View.GONE);

                Intent intent = new Intent(MainActivity.this, Home.class);
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                finish();
            });
        }).start();
    }
}