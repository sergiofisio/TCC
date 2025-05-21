/*
 * Página Page_Exercicies - Tela de exercícios no app Emotion Harmony
 * Função: exibe os períodos do dia (manhã, tarde, noite) e permite acessar exercícios de respiração e meditação.
 */

package com.example.emotionharmony.pages;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.core.graphics.Insets;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.emotionharmony.CustomToast;
import com.example.emotionharmony.Home;
import com.example.emotionharmony.R;
import com.example.emotionharmony.components.BottomMenuView;
import com.example.emotionharmony.databinding.ActivityPageExerciciesBinding;
import com.example.emotionharmony.pages.breath.Breath_Page1;
import com.example.emotionharmony.pages.choose.Choose_Emotion;
import com.example.emotionharmony.pages.meditation.Meditation_Page1;
import com.example.emotionharmony.utils.ServerConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Page_Exercicies extends AppCompatActivity {

    // Componentes da tela
    private ImageView imgManha, imgTarde, imgNoite;
    private SharedPreferences preferences;
    private final Handler uiHandler = new Handler(Looper.getMainLooper());
    private ProgressBar progressBar;
    private LinearLayout mainContent;
    private CustomToast toast;
    private boolean manhaEscolhida = false, tardeEscolhida = false, noiteEscolhida = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
         * Configura o layout em tela cheia com barras de navegação ocultas
         */
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );

        // Infla o layout da tela com view binding
        ActivityPageExerciciesBinding binding = ActivityPageExerciciesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /*
         * Ajusta o padding da view principal com base nos insets do sistema
         */
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializa os componentes visuais
        imgManha = findViewById(R.id.imgManha);
        imgTarde = findViewById(R.id.imgTarde);
        imgNoite = findViewById(R.id.imgNoite);
        Button btnLogout = binding.btnLogout;
        progressBar = findViewById(R.id.progressBar);
        mainContent = findViewById(R.id.linearLayout);
        toast = new CustomToast(this);
        BottomMenuView bottomMenu = findViewById(R.id.bottomMenu);
        bottomMenu.setActivityContext(this);

        preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);

        // Esconde o conteúdo principal enquanto carrega as emoções
        mainContent.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        /*
         * Busca os dados do usuário e atualiza a interface com base nas emoções do dia
         */
        fetchUserEmotions(() -> {
            progressBar.setVisibility(View.GONE);
            mainContent.setVisibility(View.VISIBLE);
        });

        // Botões para navegação entre telas
        binding.btnBreath.setOnClickListener(v -> navigateTo(Breath_Page1.class));
        binding.btnMed.setOnClickListener(v -> navigateTo(Meditation_Page1.class));
        btnLogout.setOnClickListener(v -> logout());
    }

    // Retorna a data atual formatada
    private String getDataAtual() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    // Retorna a hora atual (0 a 23)
    private int getHoraAtual() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /*
     * Configura os eventos de clique nos ícones dos períodos, se ainda não foram escolhidos
     */
    private void configurarEventosClique() {
        if (!manhaEscolhida) imgManha.setOnClickListener(v -> verificarEPermitirAcesso("manhã"));
        if (!tardeEscolhida) imgTarde.setOnClickListener(v -> verificarEPermitirAcesso("tarde"));
        if (!noiteEscolhida) imgNoite.setOnClickListener(v -> verificarEPermitirAcesso("noite"));
    }

    /*
     * Verifica se o período está disponível com base no horário e permite acesso
     */
    private void verificarEPermitirAcesso(String periodo) {
        int horaAtual = getHoraAtual();

        switch (periodo) {
            case "manhã":
                if (horaAtual >= 5) navigateToChooseEmotion(periodo);
                else mostrarAviso("Você só pode acessar o período da manhã a partir das 05:00.");
                break;
            case "tarde":
                if (horaAtual >= 12) navigateToChooseEmotion(periodo);
                else mostrarAviso("Você só pode acessar o período da tarde a partir das 12:00.");
                break;
            case "noite":
                if (horaAtual >= 18) navigateToChooseEmotion(periodo);
                else mostrarAviso("Você só pode acessar o período da noite a partir das 18:00.");
                break;
        }
    }

    /*
     * Faz requisição ao servidor para buscar as emoções registradas no dia
     */
    private void fetchUserEmotions(Runnable onComplete) {
        String token = preferences.getString("authToken", null);
        if (token == null) {
            onComplete.run();
            return;
        }

        ServerConnection.getRequestWithAuth("/user", token, new ServerConnection.ServerCallback() {
            @Override
            public void onSuccess(String response) {
                uiHandler.post(() -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);

                        /*
                         * Busca telefone de emergência e salva em SharedPreferences
                         */
                        JSONArray phones = jsonResponse.getJSONArray("phone_user");
                        for (int i = 0; i < phones.length(); i++) {
                            JSONObject phone = phones.getJSONObject(i);
                            String type = phone.getString("type_phone");

                            if (type.equalsIgnoreCase("emergencia")) {
                                String emergencyPhone = formatPhoneNumber(phone);
                                saveEmergencyPhone(emergencyPhone);
                                Log.d("emergencyPhone", "Telefone de emergência salvo: " + emergencyPhone);
                            }
                        }

                        /*
                         * Verifica as emoções registradas no dia atual
                         */
                        JSONArray emotionsArray = jsonResponse.getJSONArray("today_user");
                        JSONArray todayEmotions = new JSONArray();
                        JSONObject emotion;
                        String dataAtual = getDataAtual();

                        for (int i = 0; i < emotionsArray.length(); i++) {
                            emotion = emotionsArray.getJSONObject(i);
                            String dataEmocao = emotion.getString("created_at").split("T")[0];

                            if (dataEmocao.equals(dataAtual)) {
                                todayEmotions.put(emotion);
                            }
                        }

                        // Atualiza variáveis de controle e imagens
                        manhaEscolhida = false;
                        tardeEscolhida = false;
                        noiteEscolhida = false;

                        if (todayEmotions.length() > 0) {
                            for (int i = 0; i < todayEmotions.length(); i++) {
                                emotion = todayEmotions.getJSONObject(i);
                                String momento = emotion.getString("morning_afternoon_evening");
                                String emotionType = emotion.getString("emotion_today");

                                switch (momento) {
                                    case "manhã":
                                        atualizarImagemEmocao(imgManha, emotionType);
                                        manhaEscolhida = true;
                                        break;
                                    case "tarde":
                                        atualizarImagemEmocao(imgTarde, emotionType);
                                        tardeEscolhida = true;
                                        break;
                                    case "noite":
                                        atualizarImagemEmocao(imgNoite, emotionType);
                                        noiteEscolhida = true;
                                        break;
                                }
                            }
                        } else {
                            imgManha.setImageResource(R.drawable.choose_emotion);
                            imgTarde.setImageResource(R.drawable.choose_emotion);
                            imgNoite.setImageResource(R.drawable.choose_emotion);
                        }

                        configurarEventosClique();
                    } catch (JSONException e) {
                        Log.e("Page_exercicios", "❌ Erro ao processar JSON", e);
                    }
                    onComplete.run();
                });
            }

            @Override
            public void onError(String error) {
                if (onComplete != null) uiHandler.post(onComplete);
            }
        });
    }

    /*
     * Atualiza o ícone da emoção com base no tipo
     */
    private void atualizarImagemEmocao(ImageView imageView, String emotionType) {
        if (emotionType == null || emotionType.isEmpty()) {
            Log.e("atualizarImagemEmocao", "⚠ emotionType é null ou vazio, definindo como padrão.");
            imageView.setImageResource(R.drawable.choose_emotion);
            return;
        }

        switch (emotionType) {
            case "Felicidade":
                imageView.setImageResource(R.drawable.happy);
                break;
            case "Tristeza":
                imageView.setImageResource(R.drawable.sad);
                break;
            case "Raiva":
                imageView.setImageResource(R.drawable.angry);
                break;
            case "Desgosto":
                imageView.setImageResource(R.drawable.desgust);
                break;
            case "Medo":
                imageView.setImageResource(R.drawable.fear);
                break;
            default:
                imageView.setImageResource(R.drawable.choose_emotion);
                break;
        }
    }

    // Mostra aviso customizado com cor e estilo
    private void mostrarAviso(String mensagem) {
        toast.show(mensagem, Toast.LENGTH_LONG, "#FF0000", "error");
    }

    // Navega para tela de escolha de emoção
    private void navigateToChooseEmotion(String periodo) {
        Intent intent = new Intent(this, Choose_Emotion.class);
        intent.putExtra("PERIODO_DIA", periodo);
        startActivity(intent);
    }

    // Navega para qualquer outra tela (reseta a atual)
    private void navigateTo(Class<?> TargetActivity) {
        startActivity(new Intent(this, TargetActivity));
        finish();
    }

    // Faz logout do usuário e limpa token
    private void logout() {
        preferences.edit().remove("authToken").apply();
        navigateTo(Home.class);
    }

    // Formata número de telefone do JSON
    private String formatPhoneNumber(JSONObject phone) throws JSONException {
        int countryCode = phone.getInt("country_code_phone");
        int areaCode = phone.getInt("area_code_phone");
        int number = phone.getInt("phone_number");
        return "+" + countryCode + " (" + areaCode + ") " + number;
    }

    // Salva telefone de emergência em SharedPreferences
    private void saveEmergencyPhone(String phoneNumber) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("emergency_phone", phoneNumber);
        editor.apply();
    }
}
