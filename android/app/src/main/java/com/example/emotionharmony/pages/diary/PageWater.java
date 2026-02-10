package com.example.emotionharmony.pages.diary;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.content.Intent;
import android.widget.ImageView;
import android.app.TimePickerDialog;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Locale;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.example.emotionharmony.CustomToast;
import com.example.emotionharmony.components.BottomMenuView;
import com.example.emotionharmony.databinding.ActivityPageWaterBinding;
import com.example.emotionharmony.pages.Page_Diary;
import com.example.emotionharmony.utils.NavigationHelper;
import com.example.emotionharmony.utils.ServerConnection;

import org.json.JSONException;
import org.json.JSONObject;

public class PageWater extends AppCompatActivity {
    private CustomToast customToast;
    private String token;
    private EditText txtPeso, txtHoraInicio, txtHoraFim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Configura o layout em tela cheia com barras de navegação ocultas
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );

        // 2. Infla o layout da tela APENAS UMA VEZ com View Binding
        ActivityPageWaterBinding binding = ActivityPageWaterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 3. Inicializa as Views usando o objeto de binding
        txtPeso = binding.txtPeso;
        txtHoraInicio = binding.txtHoraInicio;
        txtHoraFim = binding.txtHoraFim;
        Button btnPronto = binding.btnPronto;
        ImageView btnBack11 = binding.btnBack11;
        BottomMenuView bottomMenu = binding.bottomMenu; // Acessando o BottomMenu via binding

        // Configura o menu inferior para esta tela, ativando a navegação entre seções
        bottomMenu.setActivityContext(this);

        customToast = new CustomToast(this);
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        token = preferences.getString("authToken", null);

        // Carrega os dados do usuário para preencher os campos
        loadUserData();

        // Listeners para os campos de hora com TimePickerDialog
        txtHoraInicio.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    PageWater.this,
                    (view, hourOfDay, minute1) -> {
                        String time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute1);
                        txtHoraInicio.setText(time);
                    }, hour, minute, true
            );
            timePickerDialog.show();
        });

        txtHoraFim.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    PageWater.this,
                    (view, hourOfDay, minute1) -> {
                        String time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute1);
                        txtHoraFim.setText(time);
                    }, hour, minute, true
            );
            timePickerDialog.show();
        });

        // Listener para o botão "Voltar" (agora apenas um)
        btnBack11.setOnClickListener(v -> {
            Intent intent = new Intent(PageWater.this, Page_Diary.class);
            startActivity(intent);
        });

        // Listener para o botão "Pronto"
        btnPronto.setOnClickListener(v -> {
            String peso = txtPeso.getText().toString().trim();
            String horaInicio = txtHoraInicio.getText().toString().trim();
            String horaFim = txtHoraFim.getText().toString().trim();

            try {
                if (peso.isEmpty()) {
                    throw new Exception("Informe o peso");
                }

                // A validação de hora de início E hora de fim vazias pode ser ajustada
                if (horaInicio.isEmpty() || horaFim.isEmpty()) {
                    throw new Exception("Informe a hora de início e fim.");
                }

                JSONObject userData = new JSONObject();
                // ATENÇÃO: Verifique se a classe UserData tem os setters adequados

                userData.put("peso", peso);
                userData.put("hora_inicio", horaInicio);
                userData.put("hora_fim", horaFim);

                sendEditRequest(userData);

            } catch (Exception e) {
                showErrorMessage(e.getMessage());
            }
        });
    }

    private void showErrorMessage(String error) {
        Log.e("WaterPageError", "❌ Erro na Página da Água: " + error);
        customToast.show(error, Toast.LENGTH_LONG, "#FF0000", "error");
    }

    private void loadUserData() {
        if (token == null) {
            showErrorMessage("Erro: Usuário não autenticado!");
            return;
        }

        ServerConnection.getRequestWithAuth("/user", token, new ServerConnection.ServerCallback() {
            @Override
            public void onSuccess(String response) {
                runOnUiThread(() -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);

                        // Define o peso
                        txtPeso.setText(String.valueOf(jsonResponse.optInt("weight_user", 0)));

                        String sleepTimeStartString = jsonResponse.optString("sleep_time_start", "");
                        String sleepTimeEndString = jsonResponse.optString("sleep_time_end", "");

                        // Formato de entrada esperado do backend (ISO 8601 com 'Z' para UTC)
                        // Ex: "2000-01-01T07:00:00.000Z"
                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Define o fuso horário de entrada como UTC

                        // Formato de saída para o EditText (apenas HH:mm)
                        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                        // outputFormat.setTimeZone(TimeZone.getDefault()); // Por padrão, usa o fuso horário local do dispositivo

                        // --- Processar e exibir hora de início ---
                        if (!sleepTimeStartString.isEmpty()) {
                            try {
                                Date startDate = inputFormat.parse(sleepTimeStartString);
                                assert startDate != null;
                                txtHoraInicio.setText(outputFormat.format(startDate));
                            } catch (ParseException e) {
                                Log.e("WaterPageError", "Erro ao parsear 'sleep_time_start': " + e.getMessage());
                                txtHoraInicio.setText(""); // Limpa o campo em caso de erro de parsing
                            }
                        } else {
                            txtHoraInicio.setText(""); // Limpa se a string estiver vazia
                        }

                        // --- Processar e exibir hora de fim ---
                        if (!sleepTimeEndString.isEmpty()) {
                            try {
                                Date endDate = inputFormat.parse(sleepTimeEndString);
                                assert endDate != null;
                                txtHoraFim.setText(outputFormat.format(endDate));
                            } catch (ParseException e) {
                                Log.e("WaterPageError", "Erro ao parsear 'sleep_time_end': " + e.getMessage());
                                txtHoraFim.setText(""); // Limpa o campo em caso de erro de parsing
                            }
                        } else {
                            txtHoraFim.setText(""); // Limpa se a string estiver vazia
                        }

                    } catch (JSONException e) {
                        showErrorMessage("Erro ao processar os dados do usuário.");
                        Log.e("WaterPageError", "❌ Erro JSON na resposta: " + e.getMessage());
                    }
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> showErrorMessage(error));
            }
        });
    }

    private void sendEditRequest(JSONObject userData) {
        try {
            if (token == null) throw new JSONException("Erro: Usuário não autenticado!");

            ServerConnection.patchRequestWithAuth("/user/update", token, userData, new ServerConnection.ServerCallback() {
                @Override
                public void onSuccess(String response) {
                    runOnUiThread(() -> customToast.show("✅ Informações atualizadas com sucesso!", Toast.LENGTH_LONG, "#11273D", "success"));
                    NavigationHelper.navigateTo(PageWater.this, Page_Diary.class, true);
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> showErrorMessage(error));
                }
            });
        } catch (JSONException e) {
            customToast.show("Erro ao processar os dados!", Toast.LENGTH_LONG, "#FF0000", "error");
            Log.e("WaterPageError", "Erro JSON ao enviar requisição: " + e.getMessage());
        }
    }
}