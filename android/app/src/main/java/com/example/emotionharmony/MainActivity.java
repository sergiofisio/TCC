package com.example.emotionharmony;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.emotionharmony.pages.Page_Exercicies;
import com.example.emotionharmony.utils.EnvConfig;
import com.example.emotionharmony.utils.NetworkUtils;
import com.example.emotionharmony.utils.ServerConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private int retryCount = 0;
    private static final int MAX_RETRIES = 5;
    private static final int RETRY_INTERVAL = 60000;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EnvConfig.load(this);

        ProgressBar carregamento = findViewById(R.id.carregamento);
        TextView txtCarregando = findViewById(R.id.txtCarregando);

        carregamento.setVisibility(View.VISIBLE);
        txtCarregando.setText("Carregando...");

        if (!NetworkUtils.isInternetAvailable(this)) {
            Log.e("MainActivity", "❌ Sem conexão com a internet!");
            mostrarAlertaConexao();
            return;
        }

        String baseUrl = EnvConfig.get("BASE_URL");

        if (baseUrl.isEmpty()) {
            Log.e("MainActivity", "❌ Erro: BASE_URL não configurada.");
        } else {
            verificarServidor();
        }
    }

    private void verificarServidor() {
        executor.execute(() -> ServerConnection.getRequest("/", new ServerConnection.ServerCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean init = jsonResponse.getBoolean("init");

                    runOnUiThread(() -> {
                        if (init) {
                            Log.d("MainActivity", "✅ Servidor está pronto. Continuando...");
                            verificarToken();
                        } else {
                            tentarNovamente();
                        }
                    });
                } catch (JSONException e) {
                    Log.e("MainActivity", "❌ Erro ao processar JSON: " + e.getMessage());
                    runOnUiThread(MainActivity.this::tentarNovamente);
                }
            }

            @Override
            public void onError(String error) {
                Log.e("MainActivity", "❌ Erro ao conectar no servidor: " + error);
                runOnUiThread(MainActivity.this::tentarNovamente);
            }
        }));
    }

    private void tentarNovamente() {
        retryCount++;

        if (retryCount < MAX_RETRIES) {
            Log.w("MainActivity", "⚠️ Tentativa " + retryCount + " falhou. Tentando novamente em 1 minuto...");
            scheduler.schedule(this::verificarServidor, RETRY_INTERVAL, TimeUnit.MILLISECONDS);
        } else {
            Log.e("MainActivity", "❌ Não foi possível conectar ao servidor após " + MAX_RETRIES + " tentativas.");
            mostrarAlertaErroServidor();
        }
    }

    private void verificarToken() {
        executor.execute(() -> {
            SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            String token = preferences.getString("authToken", null);

            if (token == null || token.isEmpty()) {
                Log.w("VerifyToken", "⚠️ Nenhum token encontrado. Redirecionando para login...");
                runOnUiThread(() -> RedirectTo(Home.class));
                return;
            }

            ServerConnection.getRequestWithAuth("/auth/verify", token, new ServerConnection.ServerCallback() {
                @Override
                public void onSuccess(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean isValid = jsonResponse.getBoolean("verifyToken");
                        boolean passwordChanged = jsonResponse.getBoolean("passwordChanged");

                        runOnUiThread(() -> {
                            if (!isValid || passwordChanged) {
                                String motivo = !isValid ? "⚠️ Token inválido" : "ℹ️ Senha foi alterada";
                                Log.w("VerifyToken", motivo + ". Redirecionando para login...");
                                RedirectTo(Home.class);
                            } else {
                                Log.d("VerifyToken", "✅ Token válido. Redirecionando para exercícios...");
                                RedirectTo(Page_Exercicies.class);
                            }
                        });

                    } catch (JSONException e) {
                        Log.e("VerifyToken", "❌ Erro ao processar JSON: " + e.getMessage());
                        runOnUiThread(() -> RedirectTo(Home.class));
                    }
                }

                @Override
                public void onError(String error) {
                    Log.e("VerifyToken", "❌ Erro na requisição: " + error);
                    runOnUiThread(() -> RedirectTo(Home.class));
                }
            });
        });
    }

    private void RedirectTo(Class<?> activityClass) {
        startActivity(new Intent(MainActivity.this, activityClass));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    private void mostrarAlertaConexao() {
        runOnUiThread(() ->
                new AlertDialog.Builder(this)
                        .setTitle("Sem Conexão com a Internet")
                        .setMessage("Este aplicativo precisa de conexão com a internet para funcionar. Por favor, conecte-se e tente novamente.")
                        .setPositiveButton("OK", (dialog, which) -> finishAffinity())
                        .setCancelable(false)
                        .show());
    }

    private void mostrarAlertaErroServidor() {
        runOnUiThread(() ->
                new AlertDialog.Builder(this)
                        .setTitle("Erro de Conexão")
                        .setMessage("Não foi possível conectar ao servidor após várias tentativas. Tente novamente mais tarde.")
                        .setPositiveButton("OK", (dialog, which) -> finishAffinity())
                        .setCancelable(false)
                        .show());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
        scheduler.shutdown();
    }
}
