package com.example.emotionharmony.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.emotionharmony.R;

public class BottomMenuView extends LinearLayout {
    public BottomMenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.custom_bottom_menu, this);

        Button btnPanic = findViewById(R.id.btn_panic);
        LinearLayout menuProfile = findViewById(R.id.btn_user);
        LinearLayout menuExercises = findViewById(R.id.btn_exercices);
        LinearLayout menuReports = findViewById(R.id.btn_invoice);
        LinearLayout menuDiary = findViewById(R.id.btn_diary);

        btnPanic.setOnClickListener(v -> {
        });

        menuProfile.setOnClickListener(v -> {
        });

        menuExercises.setOnClickListener(v -> {
        });

        menuReports.setOnClickListener(v -> {
        });

        menuDiary.setOnClickListener(v -> {
        });
    }
}
