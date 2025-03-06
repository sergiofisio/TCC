package com.example.emotionharmony.pages.meditation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.emotionharmony.R;
import com.example.emotionharmony.pages.After_Login;
import com.example.emotionharmony.utils.TTSHelper;

public class After_Login_Page1 extends AppCompatActivity {

    private TextView txtSpeech;
    private TTSHelper ttsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login_page1);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtSpeech = findViewById(R.id.txtSpeech1);
        ImageView btnNext1 = findViewById(R.id.btnNext1);
        Button btnSair = findViewById(R.id.btnSair);

        ttsHelper = TTSHelper.getInstance();

        new Handler().postDelayed(()-> ttsHelper.speakText(this, txtSpeech), 1500);

        btnNext1.setOnClickListener(v -> {
            ttsHelper.stopSpeaking();
            hideKeyboard();
            Intent intent = new Intent(After_Login_Page1.this, After_Login_Page2.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        btnSair.setOnClickListener(v -> {
            ttsHelper.stopSpeaking();
            hideKeyboard();
            Intent intent = new Intent(After_Login_Page1.this, After_Login.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        findViewById(R.id.main).setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                hideKeyboard();
            }
            return false;
        });
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onDestroy() {
        if (ttsHelper != null) {
            ttsHelper.release();
        }
        super.onDestroy();
    }
}
