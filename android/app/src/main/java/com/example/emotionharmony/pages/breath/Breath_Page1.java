package com.example.emotionharmony.pages.breath;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.emotionharmony.R;
import com.example.emotionharmony.classes.BreathingCircleView;
import com.example.emotionharmony.classes.Questions_Breath;
import com.example.emotionharmony.databinding.ActivityBreathPage1Binding;
import com.example.emotionharmony.pages.Page_Exercicies;
import com.example.emotionharmony.utils.NavigationHelper;
import com.example.emotionharmony.utils.TTSHelper;

import java.util.LinkedList;
import java.util.Queue;

public class Breath_Page1 extends AppCompatActivity {

    private TextView timer, breathingMoviment, txtSpeechBreathInstrucao, txtSpeechBreath1, txtSpeechBreath2;
    private LinearLayout breathingInstruction, Breath;
    private BreathingCircleView breathCircle;
    private Button btnBegin;

    private long timeLeftInMillis = 600000; // 10 minutos
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable cycleUpdater;
    private int minute = 1, rpm = 12;
    private boolean isExerciseRunning = false;

    private Questions_Breath questionsBreath;
    private TTSHelper ttsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );

        ActivityBreathPage1Binding binding = ActivityBreathPage1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        breathingInstruction = findViewById(R.id.breathInstructions);
        Breath = findViewById(R.id.Breath);
        breathCircle = findViewById(R.id.breathingCircleView);
        timer = findViewById(R.id.timer);
        breathingMoviment = findViewById(R.id.breathingMoviment);
        btnBegin = findViewById(R.id.btnBegin);
        Button btnEnd = findViewById(R.id.btnEnd);
        TextView btnSair = findViewById(R.id.btnSairBreath);
        txtSpeechBreathInstrucao = findViewById(R.id.txtSpeechBreathInstrucao);
        txtSpeechBreath1 = findViewById(R.id.txtSpeechBreath1);
        txtSpeechBreath2 = findViewById(R.id.txtSpeechBreath2);

        ttsHelper = TTSHelper.getInstance(this);
        questionsBreath = Questions_Breath.getInstance();

        btnBegin.setOnClickListener(v -> speakInstructions());
        btnSair.setOnClickListener(v -> NavigationHelper.navigateTo(this, Page_Exercicies.class, false));
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

    private void endBreathingExercise() {
        NavigationHelper.navigateTo(this, Page_End.class, true);
        questionsBreath.setDescription_breath("Exercicio Geral");
        questionsBreath.setFinished_breath(true);
    }

    @SuppressLint("SetTextI18n")
    private void startCountdown() {
        if (isExerciseRunning) return;

        breathingInstruction.setVisibility(View.GONE);
        Breath.setVisibility(View.VISIBLE);
        breathingMoviment.setText("Prepare-se");

        int[] countdown = {3};
        Runnable countdownRunnable = new Runnable() {
            @Override
            public void run() {
                if (countdown[0] > 0) {
                    breathingMoviment.setText(String.valueOf(countdown[0]--));
                    handler.postDelayed(this, 1000);
                } else {
                    breathingMoviment.setText("Iniciar");
                    handler.postDelayed(() -> startBreathingExercise(), 1000);
                }
            }
        };
        handler.postDelayed(countdownRunnable, 1000);
    }

    private void startBreathingExercise() {
        isExerciseRunning = true;
        updateRPM();
        startBreathCycle();
        startTimer();

        cycleUpdater = new Runnable() {
            @Override
            public void run() {
                if (!isExerciseRunning) return;
                minute++;
                updateRPM();
                handler.postDelayed(this, 60000);
            }
        };
        handler.postDelayed(cycleUpdater, 60000);
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
                isExerciseRunning = false;
                stopBreathAnimation();
                new Handler().postDelayed(() -> endBreathingExercise(), 1000);
            }
        }.start();
    }

    private void updateTimer() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        @SuppressLint("DefaultLocale")
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        timer.setText(timeFormatted);
    }

    private void updateRPM() {
        if (minute <= 1) rpm = 12;
        else if (minute <= 3) rpm = 10;
        else if (minute <= 6) rpm = 8;
        else rpm = 6;
    }

    @SuppressLint("SetTextI18n")
    private void startBreathCycle() {
        if (!isExerciseRunning) return;

        updateRPM();
        long respirationDuration = 60000 / rpm;
        long halfCycle = respirationDuration / 2;

        runInspireAnimation(halfCycle, () -> runExpireAnimation(halfCycle, () -> {
            if (isExerciseRunning) handler.postDelayed(this::startBreathCycle, 0);
        }));
    }

    private void runInspireAnimation(long duration, Runnable onEnd) {
        breathingMoviment.setText("INSPIRE");
        animateTextSize(breathingMoviment, 1f, 2f, duration);

        ValueAnimator inspire = ValueAnimator.ofFloat(0f, 360f);
        inspire.setDuration(duration);
        inspire.addUpdateListener(animation -> {
            float angle = (float) animation.getAnimatedValue();
            if (breathCircle != null) breathCircle.setProgress(angle);
        });
        inspire.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                onEnd.run();
            }
        });
        inspire.start();
    }

    private void runExpireAnimation(long duration, Runnable onEnd) {
        breathingMoviment.setText("EXPIRE");
        animateTextSize(breathingMoviment, 2f, 1f, duration);

        ValueAnimator expire = ValueAnimator.ofFloat(360f, 0f);
        expire.setDuration(duration);
        expire.addUpdateListener(animation -> {
            float angle = (float) animation.getAnimatedValue();
            if (breathCircle != null) breathCircle.setProgress(angle);
        });
        expire.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                onEnd.run();
            }
        });
        expire.start();
    }

    @SuppressLint("SetTextI18n")
    private void stopBreathAnimation() {
        handler.removeCallbacksAndMessages(null);
        breathingMoviment.setText("FINALIZADO");
        isExerciseRunning = false;
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
