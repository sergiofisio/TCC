// Pacote da terceira etapa da meditação guiada
package com.example.emotionharmony.pages.meditation;

// Importações necessárias
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
 * Terceira tela do processo de meditação guiada.
 * Aqui o usuário reflete sobre o que poderia ter feito diferente na situação.
 */
public class Meditation_Page3 extends AppCompatActivity {

    // Componentes de interface
    private TextView txtSpeech;
    private EditText txtDescription;
    private ImageView btnBack, btnNext;

    // Utilitários
    private Questions_Meditation questionsMeditation;
    private CustomToast toast;
    private TTSHelper ttsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meditation_page3);

        // Ajusta padding automático para status/navigation bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializa utilitários
        toast = new CustomToast(this);
        ttsHelper = TTSHelper.getInstance(this);
        questionsMeditation = Questions_Meditation.getInstance();

        // Referência aos componentes de UI
        txtSpeech = findViewById(R.id.txtSpeech3);
        txtDescription = findViewById(R.id.txtDescription2);
        btnBack = findViewById(R.id.btnBack3);
        btnNext = findViewById(R.id.btnNext3);

        // Fala a pergunta na tela após um pequeno atraso
        new Handler().postDelayed(() ->
                ttsHelper.speakText(txtSpeech.getText().toString()), 1500
        );

        // Se já havia resposta preenchida, mostra no campo
        if (questionsMeditation.getThinkToday() != null) {
            txtDescription.setText(questionsMeditation.getThinkToday());
        }

        // Ação do botão "Próximo"
        btnNext.setOnClickListener(v -> {
            try {
                String description = txtDescription.getText().toString().trim();

                if (description.isEmpty()) {
                    throw new Exception("Descreva o que você poderia fazer diferente");
                }

                // Salva resposta no singleton
                questionsMeditation.setThinkToday(description);

                // Avança para a próxima etapa
                NavigationHelper.navigateTo(Meditation_Page3.this, Meditation_Page4.class, true);

            } catch (Exception e) {
                toast.show(e.getMessage(), Toast.LENGTH_LONG, "#FF0000", "error");
            }
        });

        // Ação do botão "Voltar"
        btnBack.setOnClickListener(v ->
                NavigationHelper.navigateTo(Meditation_Page3.this, Meditation_Page2.class, false)
        );

        // Esconde o teclado ao tocar fora do campo de texto
        findViewById(R.id.main).setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                NavigationHelper.hideKeyboard(Meditation_Page3.this);
                v.performClick();
                return true;
            }
            return false;
        });
    }

    /**
     * Libera recursos de voz ao destruir a tela.
     */
    @Override
    protected void onDestroy() {
        if (ttsHelper != null) {
            ttsHelper.release();
        }
        super.onDestroy();
    }
}
