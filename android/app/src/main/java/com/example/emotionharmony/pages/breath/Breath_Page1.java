package com.example.emotionharmony.pages.breath;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
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
import com.example.emotionharmony.classes.Questions_Breath;
import com.example.emotionharmony.databinding.ActivityBreathPage1Binding;
import com.example.emotionharmony.utils.NavigationHelper;
import com.example.emotionharmony.utils.TTSHelper;

import java.util.LinkedList;
import java.util.Queue;

public class Breath_Page1 extends AppCompatActivity {

    private TextView timer, breathingMoviment, txtSpeechBreathInstrucao, txtSpeechBreath1, txtSpeechBreath2;
    private long timeLeftInMillis = 600000;
    private LinearLayout breathingInstruction, Breath;
    private BreathingCircleView breathCircle;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private int minute = 1, rpm = 12;
    private ValueAnimator animator;
    private boolean isExerciseRunning = false;
    private Questions_Breath questionsBreath;
    private TTSHelper ttsHelper;
    private Button btnBegin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityBreathPage1Binding binding = ActivityBreathPage1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        breathingInstruction = findViewById(R.id.breathInstructions);
        Breath = findViewById(R.id.Breath);
        breathCircle = findViewById(R.id.breathingCircleView);
        timer = findViewById(R.id.timer);
        breathingMoviment = findViewById(R.id.breathingMoviment);
        btnBegin = findViewById(R.id.btnBegin);
        Button btnEnd = findViewById(R.id.btnEnd);
        ttsHelper = TTSHelper.getInstance(this);
        txtSpeechBreathInstrucao = findViewById(R.id.txtSpeechBreathInstrucao);
        txtSpeechBreath1 = findViewById(R.id.txtSpeechBreath1);
        txtSpeechBreath2 = findViewById(R.id.txtSpeechBreath2);

        questionsBreath = Questions_Breath.getInstance();

        btnBegin.setOnClickListener(v -> speakInstructions());
        btnEnd.setOnClickListener(v -> endBreathingExercise());
    }

    private void speakInstructions() {
        txtSpeechBreathInstrucao.setVisibility(View.VISIBLE);
        txtSpeechBreath1.setVisibility(View.VISIBLE);
        txtSpeechBreath2.setVisibility(View.VISIBLE);
        btnBegin.setVisibility(View.GONE);
        Queue<String> speechQueue = new LinkedList<>();
        speechQueue.add(txtSpeechBreathInstrucao.getText().toString());
        speechQueue.add(txtSpeechBreath1.getText().toString());
        speechQueue.add(txtSpeechBreath2.getText().toString());

        ttsHelper.speakSequentially(speechQueue, this::startCountdown);
    }

    private void endBreathingExercise(){
        NavigationHelper.navigateTo(Breath_Page1.this, Page_End.class, true);

        questionsBreath.setDescription_breath("Exercicio Geral");
        Log.i("setFinished_breath", String.valueOf(isExerciseRunning));
        questionsBreath.setFinished_breath(true);
    }
    @SuppressLint("SetTextI18n")
    private void startCountdown() {
        if (isExerciseRunning) return;

        breathingInstruction.setVisibility(View.GONE);
        Breath.setVisibility(View.VISIBLE);
        breathingMoviment.setText("Prepare-se");

        new Handler().postDelayed(() -> {
            breathingMoviment.setText("3");
            new Handler().postDelayed(() -> {
                breathingMoviment.setText("2");
                new Handler().postDelayed(() -> {
                    breathingMoviment.setText("1");
                    new Handler().postDelayed(() -> {
                        breathingMoviment.setText("Iniciar");
                        startBreathingExercise();
                    }, 1000);
                }, 1000);
            }, 1000);
        }, 1000);
    }

    private void startBreathingExercise() {
        isExerciseRunning = true;

        updateRPM();
        startBreathCycle();
        startTimer();
    }

    private void startTimer() {
        new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimer();
            }

            @SuppressLint("SetTextI18n")
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
        @SuppressLint("DefaultLocale") String timeFormatted = String.format("%02d:%02d", minutes, seconds);
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

    @SuppressLint("SetTextI18n")
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

    @SuppressLint("SetTextI18n")
    private void stopBreathAnimation() {
        Log.i("Encerrar", "Respiração encerrada.");
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
        handler.removeCallbacksAndMessages(null);
        breathingMoviment.setText("FINALIZADO");
        isExerciseRunning = true;
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
