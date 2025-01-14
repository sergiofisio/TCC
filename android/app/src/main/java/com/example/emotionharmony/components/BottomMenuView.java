package com.example.emotionharmony.components;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.emotionharmony.R;

public class BottomMenuView extends ConstraintLayout {

    public BottomMenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.custom_bottom_menu, this);
    }
}
