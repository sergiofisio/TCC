package com.example.emotionharmony.utils;

import android.util.Log;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class ServerConnection {
    private static final OkHttpClient client = new OkHttpClient();
    private static String BASE_URL;

    static {
        BASE_URL = EnvConfig.get("BASE_URL");

        if (BASE_URL != null) {
            BASE_URL = BASE_URL.trim();
            if (BASE_URL.startsWith("\"") && BASE_URL.endsWith("\"")) {
                BASE_URL = BASE_URL.substring(1, BASE_URL.length() - 1);
            }
        }

        if (BASE_URL == null || BASE_URL.isEmpty()) {
            throw new IllegalStateException("❌ BASE_URL não definida no arquivo .env!");
        }
    }

    public interface ServerCallback {
        void onSuccess(String response);
        void onError(String error);
    }

    public static void getRequest(String endpoint, ServerCallback callback) {
        makeRequest(endpoint, null, false, callback);
    }

    public static void getRequestWithAuth(String endpoint, String token, ServerCallback callback) {
        makeRequest(endpoint, token, false, callback);
    }

    public static void postRequest(String endpoint, JSONObject jsonData, ServerCallback callback) {
        makeRequest(endpoint, null, true, callback, jsonData);
    }

    public static void postRequestWithAuth(String endpoint, String token, JSONObject jsonData, ServerCallback callback) {
        makeRequest(endpoint, token, true, callback, jsonData);
    }


    private static void makeRequest(String endpoint, String token, boolean isPost, ServerCallback callback, JSONObject... jsonData) {
        if (BASE_URL == null || BASE_URL.isEmpty()) {
            callback.onError("❌ BASE_URL não configurada.");
            return;
        }

        String formattedEndpoint = endpoint.startsWith("/") ? endpoint.substring(1) : endpoint;
        String url = BASE_URL.endsWith("/") ? BASE_URL + formattedEndpoint : BASE_URL + "/" + formattedEndpoint;

        Request.Builder requestBuilder = new Request.Builder().url(url);

        if (isPost && jsonData.length > 0) {
            RequestBody body = RequestBody.create(jsonData[0].toString(), MediaType.get("application/json; charset=utf-8"));
            requestBuilder.post(body);
        }

        if (token != null && !token.isEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer " + token);
        }

        Request request = requestBuilder.build();

        Log.d("ServerConnection", "🌍 Enviando requisição para: " + url);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("❌ Erro na conexão: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String responseBody = response.body().string();
                    if (!response.isSuccessful()) {
                        JSONObject jsonError = new JSONObject(responseBody);
                        String errorMessage = jsonError.has("error") ? jsonError.getString("error") : "Erro desconhecido";
                        callback.onError(errorMessage);
                    } else {
                        callback.onSuccess(responseBody);
                    }
                } catch (JSONException e) {
                    callback.onError("❌ Erro inesperado no processamento da resposta do servidor.");
                }
            }
        });
    }
}
