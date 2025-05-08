// Pacote da segunda tela do exercício de meditação
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
 * Tela onde o usuário descreve a situação que será trabalhada na meditação.
 * Usa leitura em voz alta e armazena a descrição no singleton Questions_Meditation.
 */
public class Meditation_Page2 extends AppCompatActivity {

    // Componentes de interface
    private TextView txtSpeech;
    private EditText txtDescription;
    private ImageView btnBack, btnNext;

    // Utilitários e singleton de dados
    private Questions_Meditation questionsMeditation;
    private CustomToast toast;
    private TTSHelper ttsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Ativa visual moderno com status/navigation transparentes
        setContentView(R.layout.activity_meditation_page2);

        // Ajuste automático de padding para evitar sobreposição da barra de status/nav
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializações dos elementos da interface
        txtSpeech = findViewById(R.id.txtSpeech2);
        txtDescription = findViewById(R.id.txtDescription);
        btnBack = findViewById(R.id.btnBack2);
        btnNext = findViewById(R.id.btnNext2);

        // Inicialização dos utilitários
        ttsHelper = TTSHelper.getInstance(this);
        toast = new CustomToast(this);
        questionsMeditation = Questions_Meditation.getInstance();

        // Fala o texto da tela após 1,5s
        new Handler().postDelayed(() ->
                ttsHelper.speakText(txtSpeech.getText().toString()), 1500
        );

        // Se já havia descrição salva (caso o usuário volte), mostra no campo
        if (questionsMeditation.getDescription() != null) {
            txtDescription.setText(questionsMeditation.getDescription());
        }

        // Avança para a próxima etapa se a descrição estiver preenchida
        btnNext.setOnClickListener(v -> {
            try {
                String description = txtDescription.getText().toString().trim();

                if (description.isEmpty()) {
                    throw new Exception("Descreva sua situação");
                }

                // Salva a descrição no singleton
                questionsMeditation.setDescription(description);

                // Avança para a próxima etapa da meditação
                NavigationHelper.navigateTo(Meditation_Page2.this, Meditation_Page3.class, true);

            } catch (Exception e) {
                toast.show(e.getMessage(), Toast.LENGTH_LONG, "#FF0000", "error");
            }
        });

        // Volta para a tela anterior
        btnBack.setOnClickListener(v ->
                NavigationHelper.navigateTo(Meditation_Page2.this, Meditation_Page1.class, false)
        );

        // Oculta o teclado se tocar fora dos campos de texto
        findViewById(R.id.main).setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                NavigationHelper.hideKeyboard(Meditation_Page2.this);
                v.performClick();
                return true;
            }
            return false;
        });
    }

    /**
     * Libera os recursos de voz ao sair da tela.
     */
    @Override
    protected void onDestroy() {
        if (ttsHelper != null) {
            ttsHelper.release();
        }
        super.onDestroy();
    }
}
