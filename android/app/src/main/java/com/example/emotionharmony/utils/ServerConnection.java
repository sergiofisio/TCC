package com.example.emotionharmony.utils;

import android.util.Log;
import okhttp3.*;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class ServerConnection {
    private static final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = loadBaseUrl();

    private static String loadBaseUrl() {
        String url = EnvConfig.get("BASE_URL");

        if (url == null || url.trim().isEmpty()) {
            throw new IllegalStateException("❌ BASE_URL não definida no arquivo .env!");
        }

        return url.trim().replaceAll("^\"|\"$", "");
    }

    public interface ServerCallback {
        void onSuccess(String response);
        void onError(String error);
    }

    public static void getRequest(String endpoint, ServerCallback callback) {
        makeRequest(endpoint, null, "GET", callback);
    }

    public static void getRequestWithAuth(String endpoint, String token, ServerCallback callback) {
        makeRequest(endpoint, token, "GET", callback);
    }

    public static void postRequest(String endpoint, JSONObject jsonData, ServerCallback callback) {
        makeRequest(endpoint, null, "POST", callback, jsonData);
    }

    public static void postRequestWithAuth(String endpoint, String token, JSONObject jsonData, ServerCallback callback) {
        makeRequest(endpoint, token, "POST", callback, jsonData);
    }

    public static void patchRequestWithAuth(String endpoint, String token, JSONObject jsonData, ServerCallback callback) {
        makeRequest(endpoint, token, "PATCH", callback, jsonData);
    }

    public static void deleteRequestWithAuth(String endpoint, String token, ServerCallback callback) {
        makeRequest(endpoint, token, "DELETE", callback);
    }

    private static void makeRequest(String endpoint, String token, String method, ServerCallback callback, JSONObject... jsonData) {
        String formattedEndpoint = endpoint.startsWith("/") ? endpoint.substring(1) : endpoint;
        String url = BASE_URL + (BASE_URL.endsWith("/") ? "" : "/") + formattedEndpoint;

        Request.Builder requestBuilder = new Request.Builder().url(url);

        if ("POST".equals(method) || "PATCH".equals(method)) {
            if (jsonData.length > 0) {
                RequestBody body = RequestBody.create(jsonData[0].toString(), MediaType.get("application/json; charset=utf-8"));
                if ("POST".equals(method)) {
                    requestBuilder.post(body);
                } else {
                    requestBuilder.patch(body);
                }
            }
        } else if ("DELETE".equals(method)) {
            requestBuilder.delete();
        }

        if (token != null && !token.isEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer " + token);
        }

        Request request = requestBuilder.build();

        Log.d("ServerConnection", "🌍 Enviando requisição " + method + " para: " + url);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onError("❌ Erro na conexão: " + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    if (response.body() == null) {
                        callback.onError("❌ Erro: Corpo da resposta vazio.");
                        return;
                    }

                    String responseBody = response.body().string();
                    if (!response.isSuccessful()) {
                        JSONObject jsonError = new JSONObject(responseBody);
                        String errorMessage = jsonError.optString("error", "Erro desconhecido");
                        callback.onError(errorMessage);
                    } else {
                        callback.onSuccess(responseBody);
                    }
                } catch (IOException | JSONException e) {
                    callback.onError("❌ Erro inesperado no processamento da resposta do servidor: " + e.getMessage());
                }
            }
        });
    }
}
