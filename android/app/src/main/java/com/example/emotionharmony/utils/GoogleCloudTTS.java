package com.example.emotionharmony.utils;

import android.util.Log;
import org.json.JSONObject;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import okhttp3.*;

/**
 * Classe utilitária para utilizar a API do Google Cloud Text-to-Speech.
 * Envia um texto e retorna o áudio codificado em base64 no formato MP3.
 */
public class GoogleCloudTTS {

    // Bloco: Configuração da API
    // Obtém a chave da API do Google a partir do arquivo de ambiente
    private static final String API_KEY = EnvConfig.get("GOOGLE_API");

    // URL completa da API do Google TTS
    private static final String GOOGLE_TTS_URL = "https://texttospeech.googleapis.com/v1/text:synthesize?key=" + API_KEY;

    // Cliente HTTP usado para enviar requisições
    private static final OkHttpClient client = new OkHttpClient();

    /**
     * Bloco: Requisição de síntese de fala
     * Função: Envia o texto para a API do Google TTS e retorna o áudio em base64 de forma assíncrona.
     *
     * @param text Texto que será convertido em fala.
     * @return CompletableFuture com o conteúdo de áudio em base64 ou null em caso de erro.
     */
    public static CompletableFuture<String> synthesizeSpeech(String text) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Monta o corpo da requisição JSON
                JSONObject requestBody = new JSONObject();
                requestBody.put("input", new JSONObject().put("text", text));  // Texto a ser falado

                // Configuração da voz (idioma, tipo e nome)
                requestBody.put("voice", new JSONObject()
                        .put("languageCode", "pt-BR")
                        .put("name", "pt-BR-Wavenet-B")
                        .put("ssmlGender", "MALE"));

                // Configuração de saída (formato do áudio)
                requestBody.put("audioConfig", new JSONObject()
                        .put("audioEncoding", "MP3"));

                // Cria a requisição HTTP POST
                Request request = new Request.Builder()
                        .url(GOOGLE_TTS_URL)
                        .post(RequestBody.create(requestBody.toString(), MediaType.get("application/json; charset=utf-8")))
                        .build();

                // Executa a requisição e processa a resposta
                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful() || response.body() == null) {
                        Log.e("GoogleCloudTTS", "❌ Erro na requisição: " + response.code() + " - " + response.message());
                        return null;
                    }

                    // Converte o corpo da resposta para JSON
                    JSONObject responseBody = new JSONObject(response.body().string());

                    // Verifica se a resposta contém o campo "audioContent"
                    if (!responseBody.has("audioContent")) {
                        Log.e("GoogleCloudTTS", "❌ Resposta da API não contém áudio!");
                        return null;
                    }

                    Log.d("GoogleCloudTTS", "✅ Resposta da API recebida.");
                    // Retorna o conteúdo de áudio codificado em base64
                    return responseBody.getString("audioContent");
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
