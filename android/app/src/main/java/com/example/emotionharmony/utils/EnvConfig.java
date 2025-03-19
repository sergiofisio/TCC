package com.example.emotionharmony.utils;

import android.content.Context;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EnvConfig {
    private static final Properties properties = new Properties();

    public static void load(Context context) {
        try {
            Log.d("EnvConfig", "🔍 Tentando carregar o arquivo config.env de assets...");
            InputStream inputStream = context.getAssets().open("config.env");

            if (inputStream == null) {
                Log.e("EnvConfig", "❌ Erro: Arquivo config.env não encontrado!");
                return;
            }

            properties.load(inputStream);
            inputStream.close();

            Log.d("EnvConfig", "✅ Arquivo config.env carregado com sucesso.");
        } catch (IOException e) {
            Log.e("EnvConfig", "❌ Erro ao carregar o arquivo config.env: " + e.getMessage());
        }
    }

    public static String get(String key) {
        String value = properties.getProperty(key, "").trim();

        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
        }

        Log.d("EnvConfig", "🔹 Variável " + key + ": " + value);
        return value;
    }
}
