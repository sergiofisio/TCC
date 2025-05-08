package com.example.emotionharmony.utils;

import android.util.Log;
import okhttp3.*;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

/**
 * Classe utilitária para gerenciar conexões HTTP com o servidor.
 * Utiliza OkHttp para enviar requisições GET, POST, PATCH e DELETE,
 * com ou sem autenticação por token.
 */
public class ServerConnection {

    private static final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = loadBaseUrl();

    // Bloco: Carrega a base da URL da API a partir do arquivo .env
    private static String loadBaseUrl() {
        String url = EnvConfig.get("BASE_URL");

        if (url == null || url.trim().isEmpty()) {
            throw new IllegalStateException("❌ BASE_URL não definida no arquivo .env!");
        }

        return url.trim().replaceAll("^\"|\"$", "");
    }

    /**
     * Interface de retorno de chamada para respostas assíncronas.
     */
    public interface ServerCallback {
        void onSuccess(String response);
        void onError(String error);
    }

    // Bloco: Métodos de requisição pública (sem autenticação)
    public static void getRequest(String endpoint, ServerCallback callback) {
        makeRequest(endpoint, null, "GET", callback);
    }

    public static void postRequest(String endpoint, JSONObject jsonData, ServerCallback callback) {
        makeRequest(endpoint, null, "POST", callback, jsonData);
    }

    // Bloco: Métodos de requisição autenticada (com token JWT)
    public static void getRequestWithAuth(String endpoint, String token, ServerCallback callback) {
        makeRequest(endpoint, token, "GET", callback);
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

    /**
     * Bloco: Método central para construir e executar a requisição.
     *
     * @param endpoint Rota da API (sem a BASE_URL).
     * @param token Token de autenticação JWT, se necessário.
     * @param method Tipo HTTP: GET, POST, PATCH, DELETE.
     * @param callback Interface de retorno de resposta.
     * @param jsonData Corpo JSON (apenas para POST ou PATCH).
     */
    private static void makeRequest(String endpoint, String token, String method, ServerCallback callback, JSONObject... jsonData) {
        // Formata o endpoint e constrói a URL final
        String formattedEndpoint = endpoint.startsWith("/") ? endpoint.substring(1) : endpoint;
        String url = BASE_URL + (BASE_URL.endsWith("/") ? "" : "/") + formattedEndpoint;

        Request.Builder requestBuilder = new Request.Builder().url(url);

        // Define o corpo da requisição para POST ou PATCH
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

        // Adiciona cabeçalho Authorization se houver token
        if (token != null && !token.isEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer " + token);
        }

        Request request = requestBuilder.build();

        Log.d("ServerConnection", "🌍 Enviando requisição " + method + " para: " + url);

        // Executa a requisição de forma assíncrona
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onError("❌ Erro na conexão: " + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try (ResponseBody responseBody = response.body()) {
                    if (responseBody == null) {
                        callback.onError("❌ Erro: Corpo da resposta vazio.");
                        return;
                    }

                    String responseString = responseBody.string();

                    if (!response.isSuccessful()) {
                        JSONObject jsonError = new JSONObject(responseString);
                        String errorMessage = jsonError.optString("error", "Erro desconhecido");
                        callback.onError(errorMessage);
                    } else {
                        callback.onSuccess(responseString);
                    }
                } catch (IOException | JSONException e) {
                    callback.onError("❌ Erro inesperado no processamento da resposta do servidor: " + e.getMessage());
                }
            }
        });
    }
}
