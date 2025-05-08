package com.example.emotionharmony.utils;

import android.content.Context;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Classe responsável por carregar variáveis de ambiente do arquivo `config.env` localizado na pasta assets.
 */
public class EnvConfig {
    // Instância única de Properties usada para armazenar as variáveis carregadas
    private static final Properties properties = new Properties();

    /**
     * Bloco: Carregamento do arquivo de ambiente
     * Função: Abre e lê o arquivo config.env da pasta assets e carrega as variáveis no objeto Properties.
     */
    public static void load(Context context) {
        try {
            Log.d("EnvConfig", "🔍 Tentando carregar o arquivo config.env de assets...");

            // Abre o arquivo config.env dentro da pasta assets
            InputStream inputStream = context.getAssets().open("config.env");

            // Verifica se o arquivo foi realmente carregado
            if (inputStream == null) {
                Log.e("EnvConfig", "❌ Erro: Arquivo config.env não encontrado!");
                return;
            }

            // Carrega o conteúdo do arquivo no objeto Properties
            properties.load(inputStream);
            inputStream.close();

            Log.d("EnvConfig", "✅ Arquivo config.env carregado com sucesso.");
        } catch (IOException e) {
            // Em caso de erro na leitura do arquivo
            Log.e("EnvConfig", "❌ Erro ao carregar o arquivo config.env: " + e.getMessage());
        }
    }

    /**
     * Bloco: Recuperação de variáveis de ambiente
     * Função: Retorna o valor da variável solicitada, eliminando aspas caso existam.
     *
     * @param key Nome da variável de ambiente
     * @return Valor da variável de ambiente (ou string vazia se não encontrada)
     */
    public static String get(String key) {
        // Recupera o valor da variável e elimina espaços em branco
        String value = properties.getProperty(key, "").trim();

        // Remove aspas duplas caso o valor esteja entre elas
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
        }

        // Loga a variável acessada
        Log.d("EnvConfig", "🔹 Variável " + key + ": " + value);
        return value;
    }
}
