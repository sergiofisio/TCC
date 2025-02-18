package com.example.emotionharmony.pages.breath;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.emotionharmony.R;
import com.example.emotionharmony.classes.BreathingCircleView;
import com.example.emotionharmony.databinding.ActivityBreathPage1Binding;
import com.example.emotionharmony.pages.After_Login;

public class Breath_Page1 extends AppCompatActivity {

    private TextView timer, breathingMoviment;
    private long timeLeftInMillis = 600000;
    private LinearLayout breathingInstruction, Breath;
    private ActivityBreathPage1Binding binding;
    private BreathingCircleView breathCircle;
    private Handler handler = new Handler(Looper.getMainLooper());
    private int minute = 1, rpm = 12;
    private ValueAnimator animator;
    private Button btnBegin, btnEnd;
    private boolean isExerciseRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBreathPage1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        breathingInstruction = findViewById(R.id.breathInstructions);
        Breath = findViewById(R.id.Breath);
        breathCircle = findViewById(R.id.breathingCircleView);
        timer = findViewById(R.id.timer);
        breathingMoviment = findViewById(R.id.breathingMoviment);
        btnBegin = findViewById(R.id.btnBegin);
        btnEnd = findViewById(R.id.btnEnd);

        btnBegin.setOnClickListener(v -> startBreathingExercise());
        btnEnd.setOnClickListener(v -> endBreathingExercise());
    }

    private void endBreathingExercise(){
        Intent intent = new Intent(Breath_Page1.this, Page_End.class);

        startActivity(intent);

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    private void startBreathingExercise() {
        if (isExerciseRunning) return;

        isExerciseRunning = true;
        breathingInstruction.setVisibility(View.GONE);
        Breath.setVisibility(View.VISIBLE);
        breathingMoviment.setVisibility(View.VISIBLE);

        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            runOnUiThread(() -> {
                updateRPM();
                startBreathCycle();
                startTimer();
            });
        }).start();
    }

    private void startTimer() {
        CountDownTimer countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                timer.setText("Tempo esgotado!");
                stopBreathAnimation();
                isExerciseRunning = false;
            }
        }.start();
    }

    private void updateTimer() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        timer.setText(timeFormatted);
    }

    private void updateRPM() {
        if (minute <= 1) {
            rpm = 12;
        } else if (minute <= 3) {
            rpm = 10;
        } else if (minute <= 6) {
            rpm = 8;
        } else {
            rpm = 6;
        }
    }

    private void startBreathCycle() {
        if (!isExerciseRunning) return;
        updateRPM();
        long respirationDuration = 60000 / rpm;
        long halfCycleDuration = respirationDuration / 2;

        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }

        animator = ValueAnimator.ofFloat(0f, 360f);
        animator.setDuration(halfCycleDuration);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);

        animator.addUpdateListener(animation -> {
            float progress = (float) animation.getAnimatedValue();

            if (breathCircle != null) {
                breathCircle.setProgress(progress);
            }

            if (progress >= 359) {
                breathingMoviment.setText("EXPIRE");
                animateTextSize(breathingMoviment, 2f, 1f, halfCycleDuration);
            } else if (progress <= 3) {
                breathingMoviment.setText("INSPIRE");
                animateTextSize(breathingMoviment, 1f, 2f, halfCycleDuration);
            }
        });

        animator.start();

        handler.postDelayed(this::updateBreathCycle, 60000);
    }

    private void updateBreathCycle() {
        if (!isExerciseRunning) return;
        minute++;
        updateRPM();
        startBreathCycle();
    }

    private void stopBreathAnimation() {
        Log.i("Encerrar", "Respiração encerrada.");
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
        handler.removeCallbacksAndMessages(null);
        breathingMoviment.setText("FINALIZADO");
    }

    private void animateTextSize(TextView textView, float startScale, float endScale, long duration) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(textView, "scaleX", startScale, endScale);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(textView, "scaleY", startScale, endScale);

        scaleX.setDuration(duration);
        scaleY.setDuration(duration);

        scaleX.start();
        scaleY.start();
    }
}
