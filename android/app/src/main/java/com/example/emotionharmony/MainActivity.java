package com.example.emotionharmony;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.emotionharmony.pages.After_Login;
import com.example.emotionharmony.utils.EnvConfig;
import com.example.emotionharmony.utils.ServerConnection;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private String baseUrl;

    interface VerifyTokenCallback {
        void onResult(boolean isValid);
    }

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

        baseUrl = EnvConfig.get("BASE_URL");

        if (baseUrl.isEmpty()) {
            Log.e("MainActivity", "❌ Erro: BASE_URL não configurada.");
        } else {
            VerifyToken(isValid -> {
                if (isValid) {
                    Log.d("MainActivity", "✅ Token válido! Redirecionando...");
                    RedirectTo(After_Login.class);
                } else {
                    Log.e("MainActivity", "❌ Token inválido! Redirecionando para login...");
                    RedirectTo(Home.class);
                }
            });
        }

        String apiKey = EnvConfig.get("GOOGLE_API");
        Log.d("GoogleCloudTTS", "🔑 API Key carregada: " + apiKey);

    }

    private void VerifyToken(VerifyTokenCallback callback) {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String token = preferences.getString("authToken", null);

        if (token == null) {
            Log.e("VerifyToken", "❌ Nenhum token salvo. Redirecionando para login...");
            callback.onResult(false);
            return;
        }

        ServerConnection.getRequestWithAuth("/verify", token, new ServerConnection.ServerCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean isValid = jsonResponse.getBoolean("verifyToken");

                    Log.d("VerifyToken", "🔐 Token verificado: " + isValid);
                    callback.onResult(isValid);
                } catch (JSONException e) {
                    Log.e("VerifyToken", "❌ Erro ao processar JSON: " + e.getMessage());
                    callback.onResult(false);
                }
            }

            @Override
            public void onError(String error) {
                Log.e("VerifyToken", "❌ Erro na requisição: " + error);
                callback.onResult(false);
            }
        });
    }

    private void RedirectTo(Class<?> activityClass) {
        runOnUiThread(() -> {

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                startActivity(new Intent(MainActivity.this, activityClass));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }, 5000);
        });
    }
}
