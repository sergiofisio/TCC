// Pacote da sexta etapa da meditação
package com.example.emotionharmony.pages.meditation;

// Importações
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.emotionharmony.CustomToast;
import com.example.emotionharmony.R;
import com.example.emotionharmony.classes.Questions_Meditation;
import com.example.emotionharmony.utils.NavigationHelper;
import com.example.emotionharmony.utils.TTSHelper;

/**
 * Sexta tela da meditação: o usuário descreve qual aspecto do seu caráter influenciou
 * a reação ou a forma como ele lidou com a situação vivida.
 */
public class Meditation_Page6 extends AppCompatActivity {

    // Componentes e utilitários
    private TextView txtSpeech;
    private EditText txtDescription;
    private Questions_Meditation questionsMeditation;
    private TTSHelper ttsHelper;
    private CustomToast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meditation_page6);

        // Ajusta a margem da tela com base nas barras do sistema (status/nav)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializações
        toast = new CustomToast(this);
        txtSpeech = findViewById(R.id.txtSpeech5);
        ttsHelper = TTSHelper.getInstance(this);
        txtDescription = findViewById(R.id.txtDescEmocao);
        ImageView btnBack = findViewById(R.id.btnBack6);
        ImageView btnNext = findViewById(R.id.btnNext6);
        questionsMeditation = Questions_Meditation.getInstance();

        // Fala o conteúdo da tela após 1,5s
        new Handler().postDelayed(() ->
                ttsHelper.speakText(txtSpeech.getText().toString()), 1500
        );

        // Se já foi preenchido antes, recupera o texto salvo no singleton
        if (questionsMeditation.getCaracter() != null) {
            txtDescription.setText(questionsMeditation.getCaracter());
        }

        // Botão "Avançar"
        btnNext.setOnClickListener(v -> {
            try {
                String description = txtDescription.getText().toString().trim();

                if (description.isEmpty()) {
                    throw new Exception("Descreva o que você poderia fazer diferente");
                }

                questionsMeditation.setCaracter(description);
                NavigationHelper.navigateTo(Meditation_Page6.this, Meditation_Page7.class, true);

            } catch (Exception e) {
                toast.show(e.getMessage(), Toast.LENGTH_LONG, "#FF0000", "error");
            }
        });

        // Botão "Voltar"
        btnBack.setOnClickListener(v ->
                NavigationHelper.navigateTo(Meditation_Page6.this, Meditation_Page5.class, false)
        );

        // Esconde teclado ao tocar fora do EditText
        findViewById(R.id.main).setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                NavigationHelper.hideKeyboard(Meditation_Page6.this);
                v.performClick();
                return true;
            }
            return false;
        });
    }

    // Libera o Text-to-Speech ao destruir a tela
    @Override
    protected void onDestroy() {
        if (ttsHelper != null) {
            ttsHelper.release();
        }
        super.onDestroy();
    }
}
