package com.example.emotionharmony.pages.diary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.emotionharmony.R;
import com.example.emotionharmony.components.BottomMenuView;
import com.example.emotionharmony.databinding.ActivityPageWater2Binding;
import com.example.emotionharmony.pages.Page_Diary;
import com.example.emotionharmony.utils.ServerConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class PageWater2 extends AppCompatActivity {

    private ProgressBar progressBar2;
    private TextView qntWaterText;
    private TextView qntTotalWaterText;

    private Integer Weight;
    private String token, sleepTimeStart, sleepTimeEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );

        ActivityPageWater2Binding binding = ActivityPageWater2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button btnAlterar = binding.btnAlterar;
        BottomMenuView bottomMenu = findViewById(R.id.bottomMenu);
        bottomMenu.setActivityContext(this);
        ImageView btnBack33 = findViewById(R.id.BtnBack33);
        btnBack33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PageWater2.this, Page_Diary.class);
                startActivity(intent);
            }
        });
        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PageWater2.this, PageWater.class);
                startActivity(intent);
            }
        });
        // Inicializa componentes
        progressBar2 = findViewById(R.id.progressBar2);
        qntWaterText = findViewById(R.id.QntWater);
        qntTotalWaterText = findViewById(R.id.QntTotalWater);

        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        token = preferences.getString("authToken", null);

        fetchDataUser();
    }

    private void fetchDataUser() {
        ServerConnection.getRequestWithAuth("/user", token, new ServerConnection.ServerCallback() {
            @Override
            public void onSuccess(String response) {
                runOnUiThread(() -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        Weight = jsonResponse.optInt("weight_user", 0);
                        sleepTimeStart = jsonResponse.optString("sleep_time_start");
                        sleepTimeEnd = jsonResponse.optString("sleep_time_end");

                        if (Weight > 0) {
                            loadAndDisplayWaterData();
                        }
                    } catch (JSONException e) {
                        Log.e("PageWater2", "❌ Erro JSON: " + e.getMessage());
                    }
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> Log.e("PageWater2", "❌ Erro: " + error));
            }
        });
    }

    private int calculateWaterIntake(int weight) {
        return weight * 45;
    }

    private void loadAndDisplayWaterData() {
        try {
            int totalDiario = calculateWaterIntake(Weight);
            int horasAcordadoTotal = fetchTotalIngeridoFromDb();

            if (horasAcordadoTotal == 0) {
                Log.e("PageWater2", "⚠ Não foi possível calcular as horas acordado.");
                return;
            }

            String[] horaMinuto = sleepTimeStart.substring(11, 16).split(":");
            int hora = Integer.parseInt(horaMinuto[0]);
            int minuto = Integer.parseInt(horaMinuto[1]);

            Calendar agora = Calendar.getInstance();
            Calendar acordou = Calendar.getInstance();
            acordou.set(Calendar.HOUR_OF_DAY, hora);
            acordou.set(Calendar.MINUTE, minuto);
            acordou.set(Calendar.SECOND, 0);
            acordou.set(Calendar.MILLISECOND, 0);

            if (acordou.after(agora)) {
                acordou.add(Calendar.DAY_OF_YEAR, -1);
            }

            long minutosDesdeQueAcordou = TimeUnit.MILLISECONDS.toMinutes(
                    agora.getTimeInMillis() - acordou.getTimeInMillis()
            );

            double aguaPorHora = (double) totalDiario / horasAcordadoTotal;
            double ingeridoPrevisto = aguaPorHora * (minutosDesdeQueAcordou / 60.0);
            int totalIngerido = (int) ingeridoPrevisto;

            showWaterProgress(totalIngerido, totalDiario);

        } catch (Exception e) {
            Log.e("PageWater2", "❌ Erro em loadAndDisplayWaterData: " + e.getMessage());
        }
    }

    private void showWaterProgress(int totalIngerido, int totalDiario) {
        progressBar2.setMax(totalDiario);
        progressBar2.setProgress(totalIngerido);
        qntWaterText.setText(totalIngerido + " ml");
        qntTotalWaterText.setText(totalDiario + " ml");
    }

    private int fetchTotalIngeridoFromDb() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

            Date horaQueAcorda = sdf.parse(sleepTimeStart);
            Date horaQueDorme = sdf.parse(sleepTimeEnd);

            long diffMillis;
            if (horaQueDorme.after(horaQueAcorda)) {
                diffMillis = horaQueDorme.getTime() - horaQueAcorda.getTime();
            } else {
                diffMillis = (horaQueDorme.getTime() + TimeUnit.DAYS.toMillis(1)) - horaQueAcorda.getTime();
            }

            long totalMinutosAcordado = TimeUnit.MILLISECONDS.toMinutes(diffMillis);
            long horas = totalMinutosAcordado / 60;

            return (int) horas;

        } catch (Exception e) {
            Log.e("PageWater2", "❌ Erro ao calcular tempo acordado: " + e.getMessage());
            return 0;
        }
    }
}
