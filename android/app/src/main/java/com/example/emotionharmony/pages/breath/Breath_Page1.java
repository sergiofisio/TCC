package com.example.emotionharmony.pages.breath;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.emotionharmony.R;
import com.example.emotionharmony.classes.BreathingCircleView;
import com.example.emotionharmony.databinding.ActivityBreathPage1Binding;

public class Breath_Page1 extends AppCompatActivity {

    private TextView timer, breathingInstruction;
    private CountDownTimer countDownTimer;
    private long timeLeftIntMillis = 600000;
    private LinearLayout instructions, Breath, modalEnd;
    private ActivityBreathPage1Binding binding;
    private BreathingCircleView breathCircle;
    private Handler handler = new Handler(Looper.getMainLooper());
    private int minute = 1,  rpm = 12;
    private ValueAnimator animator;
    private Button btnBegin, btnSim, btnNao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBreathPage1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        breathCircle = findViewById(R.id.breathingCircleView);
        timer = findViewById(R.id.timer);
        breathingInstruction = findViewById(R.id.breathingInstruction);
        btnBegin = findViewById(R.id.btnBegin);
        btnSim = findViewById(R.id.btnSim);
        btnNao = findViewById(R.id.btnNao);

        btnBegin.setOnClickListener(v->{

            startBreathingExercise();
        });


    }

    private void startBreathingExercise(){
        instructions.setVisibility(View.GONE);

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
        countDownTimer = new CountDownTimer(timeLeftIntMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftIntMillis = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                timer.setText("Tempo esgotado!");
                stopBreathAnimation();
            }
        }.start();
    }

    private void updateTimer(){
        int minutes = (int) (timeLeftIntMillis/1000)/60;
        int seconds = (int) (timeLeftIntMillis/1000)%60;

        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        timer.setText(timeFormatted);
    }

    private void updateRPM(){
        if (minute ==1){
            rpm = 12;
        } else if (minute <=3) {
            rpm = 10;
        } else if (minute <=6) {
            rpm = 8;
        }else{
            rpm =6;
        }
    }

    private void startBreathCycle(){
        long cycleDuration = 60000 / rpm;

        animator = ValueAnimator.ofFloat(0f, 360f);
        animator.setDuration(cycleDuration);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);

        animator.addUpdateListener(animation ->{
            float progress = (float) animation.getAnimatedValue();
            breathCircle.setProgress(progress);

            if (progress >= 359 ) {
                breathingInstruction.setText("EXPIRE");
                animateTextSize(breathingInstruction, 2f, 1f, cycleDuration);
            } else if(progress<=3 ) {
                breathingInstruction.setText("INSPIRE");
                animateTextSize(breathingInstruction, 1f, 2f, cycleDuration);
            }
        });

        animator.start();

        handler.postDelayed(()->{
            minute++;
            updateRPM();
            animator.setDuration(60000/rpm);
        }, 60000);
    }

    private void stopBreathAnimation() {
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
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