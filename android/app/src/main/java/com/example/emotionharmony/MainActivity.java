/**
 * Tela inicial da aplicação.
 * Responsável por:
 * - Verificar conexão com a internet
 * - Verificar se a URL do servidor está configurada
 * - Tentar conexão com o servidor
 * - Verificar token do usuário
 * - Redirecionar para tela correta (login ou exercícios)
 */
package com.example.emotionharmony;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.example.emotionharmony.utils.VerifyServer;

import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final org.apache.commons.logging.Log log = LogFactory.getLog(MainActivity.class);
    private int retryCount = 0;
    private static final int MAX_RETRIES = 5;
    private static final int RETRY_INTERVAL = 60000; // 1 minuto

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    /**
     * Método principal que inicia a atividade.
     * Define layout, carrega configs, verifica rede e servidor.
     */
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

        executor.execute(() -> {
            EnvConfig.load(this);

            new Handler(Looper.getMainLooper()).post(() -> {
                String baseUrl = EnvConfig.get("BASE_URL");

                if (baseUrl.isEmpty()) {
                    Log.e("MainActivity", "❌ Erro: BASE_URL não configurada.");
                    mostrarAlertaErroServidor();
                } else {
                    verificarServidor();
                }
            });
        });
    }

    /**
     * Faz requisição de verificação ao servidor e aguarda resposta.
     */
    private void verificarServidor() {
        VerifyServer.verificar(this, this::verificarToken);    }

    /**
     * Verifica se o token salvo é válido ou expirado.
     */
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

    /**
     * Redireciona para uma nova tela (Activity).
     */
    private void RedirectTo(Class<?> activityClass) {
        startActivity(new Intent(MainActivity.this, activityClass));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    /**
     * Mostra alerta para ausência de conexão com a internet.
     */
    private void mostrarAlertaConexao() {
        runOnUiThread(() ->
                new AlertDialog.Builder(this)
                        .setTitle("Sem Conexão com a Internet")
                        .setMessage("Este aplicativo precisa de conexão com a internet para funcionar. Por favor, conecte-se e tente novamente.")
                        .setPositiveButton("OK", (dialog, which) -> finishAffinity())
                        .setCancelable(false)
                        .show());
    }

    /**
     * Mostra alerta de falha ao conectar com servidor após todas as tentativas.
     */
    private void mostrarAlertaErroServidor() {
        runOnUiThread(() ->
                new AlertDialog.Builder(this)
                        .setTitle("Erro de Conexão")
                        .setMessage("Não foi possível conectar ao servidor após várias tentativas. Tente novamente mais tarde.")
                        .setPositiveButton("OK", (dialog, which) -> finishAffinity())
                        .setCancelable(false)
                        .show());
    }

    /**
     * Libera recursos de threads ao encerrar a activity.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
        scheduler.shutdown();
    }
}