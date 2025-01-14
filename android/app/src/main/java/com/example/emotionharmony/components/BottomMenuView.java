package com.example.emotionharmony.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.emotionharmony.R;

public class BottomMenuView extends ConstraintLayout {
    private View modalPanic;
    private Button btnPanic, btnNao, btnSim;

    public BottomMenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.custom_bottom_menu, this);
        initializeViews();
        setupListeners();
    }

    private void initializeViews(){
        modalPanic = findViewById(R.id.ModalPanic);
        btnPanic = findViewById(R.id.btn_panic);
        btnSim = findViewById(R.id.btnSim);
        btnNao = findViewById(R.id.btnNao);
    }

    private void setupListeners() {
        btnPanic.setOnClickListener(v -> {
            showModal(View.VISIBLE, false);
        });

        btnNao.setOnClickListener(v -> {
            showModal(View.GONE, true);
        });

        btnSim.setOnClickListener(v -> {
            performDefaultSimAction();
        });
    }

    public void showModal(int visibility, boolean enable) {
        if (visibility == View.VISIBLE) {
            modalPanic.setAlpha(0f);
            modalPanic.setVisibility(View.VISIBLE);
            modalPanic.animate().alpha(1f).setDuration(300).start();
        } else {
            modalPanic.animate().alpha(0f).setDuration(300).withEndAction(() -> {
                modalPanic.setVisibility(View.GONE);
            }).start();
        }
        btnPanic.setEnabled(enable);
    }

    private void performDefaultSimAction() {
        System.out.println("Ação padrão do botão 'Sim'");
    }

    public void setOnSimClickListener(OnClickListener listener) {
        btnSim.setOnClickListener(listener);
    }

    public void setOnNaoClickListener(OnClickListener listener) {
        btnNao.setOnClickListener(listener);
    }

    public void setOnPanicClickListener(OnClickListener listener) {
        btnPanic.setOnClickListener(listener);
    }
}
