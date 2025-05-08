// Pacote onde está localizada a tela de respiração
package com.example.emotionharmony.pages.breath;

// Importações necessárias para a lógica e visual da Activity
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

/**
 * Tela responsável por conduzir o exercício de respiração.
 * Exibe instruções com voz, anima um círculo de respiração e ajusta a frequência respiratória ao longo do tempo.
 */
public class Breath_Page1 extends AppCompatActivity {

    // Elementos de UI
    private TextView timer, breathingMoviment, txtSpeechBreathInstrucao, txtSpeechBreath1, txtSpeechBreath2;
    private LinearLayout breathingInstruction, Breath;
    private BreathingCircleView breathCircle;
    private Button btnBegin;

    // Controle do tempo e animações
    private long timeLeftInMillis = 600000; // 10 minutos
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable cycleUpdater;
    private int minute = 1, rpm = 12;
    private ValueAnimator animator;
    private boolean isExerciseRunning = false;

    // Dados e utilitários
    private Questions_Breath questionsBreath;
    private TTSHelper ttsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Oculta a barra de navegação para um visual imersivo
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );

        // Usa o ViewBinding para inflar a tela
        ActivityBreathPage1Binding binding = ActivityBreathPage1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Ajusta os paddings de acordo com os insets do sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Referências dos elementos de UI
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

        // Ações dos botões
        btnBegin.setOnClickListener(v -> speakInstructions());
        btnSair.setOnClickListener(v -> NavigationHelper.navigateTo(this, Page_Exercicies.class, false));
        btnEnd.setOnClickListener(v -> endBreathingExercise());
    }

    /**
     * Lê as instruções de respiração com TTS em sequência.
     */
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

    /**
     * Finaliza o exercício e salva os dados.
     */
    private void endBreathingExercise() {
        NavigationHelper.navigateTo(this, Page_End.class, true);
        questionsBreath.setDescription_breath("Exercicio Geral");
        questionsBreath.setFinished_breath(true);
    }

    /**
     * Inicia a contagem regressiva visual de 3 segundos antes do exercício.
     */
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

    /**
     * Inicia o exercício de respiração.
     */
    private void startBreathingExercise() {
        isExerciseRunning = true;
        updateRPM();
        startBreathCycle();
        startTimer();

        // Atualiza a frequência a cada minuto
        cycleUpdater = new Runnable() {
            @Override
            public void run() {
                if (!isExerciseRunning) return;
                minute++;
                updateRPM();
                startBreathCycle();
                handler.postDelayed(this, 60000);
            }
        };
        handler.postDelayed(cycleUpdater, 60000);
    }

    /**
     * Inicia o cronômetro regressivo do tempo total do exercício.
     */
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
                new Handler().postDelayed(() -> endBreathingExercise(), 1000);
            }
        }.start();
    }

    /**
     * Atualiza o texto do cronômetro formatado.
     */
    private void updateTimer() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        @SuppressLint("DefaultLocale")
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        timer.setText(timeFormatted);
    }

    /**
     * Ajusta o número de respirações por minuto conforme o tempo passa.
     */
    private void updateRPM() {
        if (minute <= 1) rpm = 12;
        else if (minute <= 3) rpm = 10;
        else if (minute <= 6) rpm = 8;
        else rpm = 6;
    }

    /**
     * Inicia a animação do ciclo de respiração (círculo + texto).
     */
    @SuppressLint("SetTextI18n")
    private void startBreathCycle() {
        if (!isExerciseRunning) return;
        updateRPM();

        long respirationDuration = 60000 / rpm;
        long halfCycleDuration = respirationDuration / 2;

        // Reinicia a animação se já estiver rodando
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }

        animator = ValueAnimator.ofFloat(0f, 360f);
        animator.setDuration(halfCycleDuration);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);

        // Define o comportamento da animação a cada frame
        animator.addUpdateListener(animation -> {
            float fraction = animation.getAnimatedFraction();
            if (fraction <= 0.01f) {
                breathingMoviment.setText("INSPIRE");
                animateTextSize(breathingMoviment, 1f, 2f, halfCycleDuration);
            } else if (fraction >= 0.99f) {
                breathingMoviment.setText("EXPIRE");
                animateTextSize(breathingMoviment, 2f, 1f, halfCycleDuration);
            }

            if (breathCircle != null) {
                float angle = 360f * fraction;
                breathCircle.setProgress(angle);
            }
        });

        animator.start();
        handler.postDelayed(this::updateBreathCycle, 60000);
    }

    /**
     * Atualiza a lógica do ciclo de respiração ao longo do tempo.
     */
    private void updateBreathCycle() {
        if (!isExerciseRunning) return;
        minute++;
        updateRPM();
        startBreathCycle();
    }

    /**
     * Para a animação e exibe o texto final.
     */
    @SuppressLint("SetTextI18n")
    private void stopBreathAnimation() {
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
        handler.removeCallbacksAndMessages(null);
        breathingMoviment.setText("FINALIZADO");
        isExerciseRunning = true;
    }

    /**
     * Anima o tamanho do texto suavemente.
     */
    private void animateTextSize(TextView textView, float startScale, float endScale, long duration) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(textView, "scaleX", startScale, endScale);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(textView, "scaleY", startScale, endScale);
        scaleX.setDuration(duration);
        scaleY.setDuration(duration);
        scaleX.start();
        scaleY.start();
    }
}
