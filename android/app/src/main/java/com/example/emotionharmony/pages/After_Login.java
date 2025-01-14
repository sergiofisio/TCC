package com.example.emotionharmony.pages;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.emotionharmony.R;
import com.example.emotionharmony.components.BottomMenuView;

public class After_Login extends AppCompatActivity {

    BottomMenuView menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_after_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        menu = findViewById(R.id.bottomMenu);

        menu.findViewById(R.id.btn_panic).setOnClickListener(v ->{
            showModal(View.VISIBLE, false);
        });

        menu.findViewById(R.id.btnNao).setOnClickListener(v->{
            showModal(View.GONE, true);
        });
    }

    private void showModal(int visibility, boolean enable){
        menu.findViewById(R.id.ModalPanic).setVisibility(visibility);
        menu.findViewById(R.id.btn_panic).setEnabled(enable);
    }
}