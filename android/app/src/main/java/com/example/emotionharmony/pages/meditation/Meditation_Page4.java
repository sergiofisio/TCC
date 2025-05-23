// Pacote da quarta etapa da meditação guiada
package com.example.emotionharmony.pages.meditation;

// Importações necessárias
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.Switch;
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
 * Quarta tela da meditação: o usuário avalia se a situação vivida foi boa ou ruim.
 * A resposta é salva no singleton Questions_Meditation.
 */
public class Meditation_Page4 extends AppCompatActivity {

    // Componentes visuais
    private TextView txtRuim, txtBoa, txtSpeech;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch SwSituation;
    private ImageView btnBack, btnNext;

    // Utilitários
    private TTSHelper ttsHelper;
    private Questions_Meditation questionsMeditation;
    private CustomToast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meditation_page4);

        // Ajusta margens para status bar e navigation bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Init(); // Inicializa os componentes e listeners

        // Fala o texto da tela após 1,5 segundos
        new Handler().postDelayed(() ->
                ttsHelper.speakText(txtSpeech.getText().toString()), 1500
        );

        // Botão "Próximo" – salva resposta e avança
        btnNext.setOnClickListener(v -> {
            try {
                String typeOf = SwSituation.isChecked() ? "boa" : "ruim";
                questionsMeditation.setTypeSituation(typeOf);
                NavigationHelper.navigateTo(Meditation_Page4.this, Meditation_Page5.class, true);
            } catch (Exception e) {
                toast.show(e.getMessage(), Toast.LENGTH_LONG, "#FF0000", "error");
            }
        });

        // Botão "Voltar" – retorna para a etapa anterior
        btnBack.setOnClickListener(v ->
                NavigationHelper.navigateTo(Meditation_Page4.this, Meditation_Page3.class, false)
        );
    }

    /**
     * Inicializa os componentes da tela e configura o comportamento do Switch.
     */
    private void Init() {
        toast = new CustomToast(this);

        // Referências visuais
        txtBoa = findViewById(R.id.txtGood);
        txtRuim = findViewById(R.id.txtBad);
        SwSituation = findViewById(R.id.SwSituation);
        btnBack = findViewById(R.id.btnBack4);
        btnNext = findViewById(R.id.btnNext4);
        txtSpeech = findViewById(R.id.txtSpeech);

        // Instância dos dados da meditação
        questionsMeditation = Questions_Meditation.getInstance();
        ttsHelper = TTSHelper.getInstance(this);

        // Marca a opção previamente escolhida (caso exista)
        if (questionsMeditation.getTypeSituation() != null) {
            SwSituation.setChecked(questionsMeditation.getTypeSituation().equals("boa"));
        }

        // Muda cores conforme seleção do usuário
        SwSituation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                txtBoa.setTextColor(Color.parseColor("#0026FF"));
                txtRuim.setTextColor(Color.parseColor("#807F7F"));
            } else {
                txtRuim.setTextColor(Color.parseColor("#F44336"));
                txtBoa.setTextColor(Color.parseColor("#807F7F"));
            }
        });
    }
}
