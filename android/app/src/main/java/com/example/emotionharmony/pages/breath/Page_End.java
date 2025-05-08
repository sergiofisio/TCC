// Pacote da página final do exercício de respiração
package com.example.emotionharmony.pages.breath;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import com.example.emotionharmony.classes.Questions_Breath;
import com.example.emotionharmony.pages.Page_Exercicies;
import com.example.emotionharmony.utils.NavigationHelper;
import com.example.emotionharmony.utils.ServerConnection;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Tela de encerramento do exercício de respiração.
 * Permite ao usuário avaliar se sentiu melhora e envia os dados para o servidor.
 */
public class Page_End extends AppCompatActivity {

    // Elementos da interface
    private TextView txtMesma, txtMelhor, btnFinish;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch SwFeel;
    private ImageView btnBack;

    // Instâncias de utilitários
    private Questions_Breath questionsBreath;
    private CustomToast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_page_end);

        // Ajusta o padding da tela para não sobrepor a barra de status/nav
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializa os elementos da tela
        Init();

        // Ação ao finalizar exercício
        btnFinish.setOnClickListener(v -> {
            questionsBreath.setFeel_bether_breath(SwFeel.isChecked());
            sendBreathData();
        });

        // Botão voltar para a tela de respiração
        btnBack.setOnClickListener(v -> NavigationHelper.navigateTo(Page_End.this, Breath_Page1.class, false));
    }

    /**
     * Inicializa os componentes e define comportamentos do Switch.
     */
    private void Init() {
        txtMelhor = findViewById(R.id.txtGood);
        txtMesma = findViewById(R.id.txtBad);
        SwFeel = findViewById(R.id.SwSituation);
        btnBack = findViewById(R.id.btnBackBreath);
        btnFinish = findViewById(R.id.btnEndBreath);
        toast = new CustomToast(this);

        questionsBreath = Questions_Breath.getInstance();

        // O switch só pode ser ativado se o exercício foi finalizado
        SwFeel.setChecked(questionsBreath.getFinished_breath());
        if (!questionsBreath.getFinished_breath()) {
            SwFeel.setEnabled(false);
            txtMesma.setTextColor(Color.parseColor("#F44336"));
            txtMelhor.setTextColor(Color.parseColor("#807F7F"));
        }

        // Atualiza cores dos textos com base no estado do switch
        SwFeel.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                txtMelhor.setTextColor(Color.parseColor("#0026FF"));
                txtMesma.setTextColor(Color.parseColor("#807F7F"));
            } else {
                txtMesma.setTextColor(Color.parseColor("#F44336"));
                txtMelhor.setTextColor(Color.parseColor("#807F7F"));
            }
        });
    }

    /**
     * Monta o JSON com os dados do exercício de respiração.
     */
    private JSONObject createBreathData() throws JSONException {
        JSONObject breathData = new JSONObject();
        breathData.put("finished_breath", questionsBreath.getFinished_breath());
        breathData.put("felt_betther_breath", questionsBreath.getFeel_bether_breath());
        breathData.put("description_breath",
                !questionsBreath.getFinished_breath()
                        ? "Não Finalizado"
                        : questionsBreath.getDescription_breath()
        );
        return breathData;
    }

    /**
     * Envia os dados do exercício para o servidor via requisição autenticada.
     */
    private void sendBreathData() {
        try {
            SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            String token = preferences.getString("authToken", null);

            if (token == null) throw new JSONException("Erro: Usuário não autenticado!");

            JSONObject breathData = createBreathData();
            toast.show("Finalizando exercício", Toast.LENGTH_SHORT, "#11273D", "success");

            // Desativa botões temporariamente durante o envio
            btnFinish.setEnabled(false);
            btnFinish.setTextColor(getColor(R.color.btnInactive));
            btnBack.setEnabled(false);

            ServerConnection.postRequestWithAuth("/activity/add/breath", token, breathData, new ServerConnection.ServerCallback() {
                @Override
                public void onSuccess(String response) {
                    runOnUiThread(() -> {
                        toast.show("Respiração Finalizada", Toast.LENGTH_LONG, "#11273D", "success");
                        NavigationHelper.navigateTo(Page_End.this, Page_Exercicies.class, true);

                        // Reativa os botões
                        btnFinish.setEnabled(true);
                        btnBack.setEnabled(true);
                        btnFinish.setTextColor(getColor(R.color.black));
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        if (error.toLowerCase().contains("timeout")) {
                            sendBreathData();  // Reenvia em caso de timeout
                        } else {
                            toast.show("❌ Erro ao salvar Respiração: " + error, Toast.LENGTH_LONG, "#FF0000", "error");
                            Log.e("Page_End", "❌ Erro ao salvar meditação: " + error);
                            btnBack.setEnabled(true);
                        }
                    });
                }
            });

        } catch (JSONException e) {
            toast.show("Erro ao processar os dados!", Toast.LENGTH_LONG, "#FF0000", "error");
            Log.e("Page_End", "Erro JSON: " + e.getMessage());
        }
    }
}
