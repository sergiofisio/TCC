package com.example.emotionharmony.utils;

import android.util.Log;
import org.json.JSONObject;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import okhttp3.*;

public class GoogleCloudTTS {
    private static final String API_KEY = EnvConfig.get("GOOGLE_API");
    private static final String GOOGLE_TTS_URL = "https://texttospeech.googleapis.com/v1/text:synthesize?key=" + API_KEY;
    private static final OkHttpClient client = new OkHttpClient();

    public static CompletableFuture<String> synthesizeSpeech(String text) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                JSONObject requestBody = new JSONObject();
                requestBody.put("input", new JSONObject().put("text", text));
                requestBody.put("voice", new JSONObject()
                        .put("languageCode", "pt-BR")
                        .put("name", "pt-BR-Wavenet-B")
                        .put("ssmlGender", "MALE"));
                requestBody.put("audioConfig", new JSONObject()
                        .put("audioEncoding", "MP3"));

                Request request = new Request.Builder()
                        .url(GOOGLE_TTS_URL)
                        .post(RequestBody.create(requestBody.toString(), MediaType.get("application/json; charset=utf-8")))
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful() && response.body() != null) {
                        JSONObject responseBody = new JSONObject(response.body().string());
                        return responseBody.getString("audioContent");
                    } else {
                        Log.e("GoogleCloudTTS", "❌ Erro na requisição: " + response.code() + " - " + response.message());
                        return null;
                    }
                }
            } catch (IOException e) {
                Log.e("GoogleCloudTTS", "❌ Erro de rede: " + e.getMessage());
                return null;
            } catch (Exception e) {
                Log.e("GoogleCloudTTS", "❌ Erro ao processar JSON: " + e.getMessage());
                return null;
            }
        });
    }
}
