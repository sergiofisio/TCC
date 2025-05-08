package com.example.emotionharmony.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Classe utilitária para conversão de texto em fala usando Google Cloud TTS.
 * Gerencia reprodução, fila de falas e controle do MediaPlayer.
 */
public class TTSHelper {
    private static TTSHelper instance;
    private MediaPlayer mediaPlayer;
    private final Queue<String> speechQueue = new LinkedList<>();
    private boolean isSpeaking = false;
    private final Context context;

    /**
     * Construtor privado para singleton.
     */
    private TTSHelper(Context context) {
        this.context = context;
    }

    /**
     * Obtém a instância única de TTSHelper.
     */
    public static TTSHelper getInstance(Context context) {
        if (instance == null) {
            instance = new TTSHelper(context);
        }
        return instance;
    }

    /**
     * Converte e reproduz um único texto em áudio.
     */
    public void speakText(String text) {
        if (text == null || text.trim().isEmpty()) {
            Log.e("TTSHelper", "⚠️ Nenhum texto encontrado para leitura.");
            return;
        }

        Log.d("TTSHelper", "\uD83C\uDF99️ Enviando texto para conversão: " + text);
        GoogleCloudTTS.synthesizeSpeech(text).thenAccept(audioBase64 -> {
            if (audioBase64 != null) {
                playAudio(audioBase64, () -> {});
            } else {
                Log.e("TTSHelper", "❌ Falha ao obter áudio da API.");
            }
        }).exceptionally(e -> {
            Log.e("TTSHelper", "❌ Erro ao converter texto para fala: " + e.getMessage());
            return null;
        });
    }

    /**
     * Reproduz uma fila de textos em sequência.
     */
    public void speakSequentially(Queue<String> texts, Runnable onComplete) {
        if (texts == null || texts.isEmpty()) {
            if (onComplete != null) onComplete.run();
            return;
        }

        speechQueue.clear();
        speechQueue.addAll(texts);
        isSpeaking = false;

        speakNext(onComplete);
    }

    /**
     * Reproduz o próximo item da fila.
     */
    private void speakNext(Runnable onComplete) {
        if (speechQueue.isEmpty()) {
            isSpeaking = false;
            if (onComplete != null) onComplete.run();
            return;
        }

        String nextText = speechQueue.poll();
        if (nextText != null) {
            isSpeaking = true;
            GoogleCloudTTS.synthesizeSpeech(nextText).thenAccept(audioBase64 -> {
                if (audioBase64 != null) {
                    playAudio(audioBase64, () -> speakNext(onComplete));
                } else {
                    Log.e("TTSHelper", "❌ Falha ao obter áudio da API.");
                    speakNext(onComplete);
                }
            }).exceptionally(e -> {
                Log.e("TTSHelper", "❌ Erro ao converter texto para fala: " + e.getMessage());
                speakNext(onComplete);
                return null;
            });
        }
    }

    /**
     * Converte base64 em áudio e reproduz com MediaPlayer.
     */
    private void playAudio(String audioBase64, Runnable onComplete) {
        try {
            byte[] audioData = Base64.decode(audioBase64, Base64.DEFAULT);
            File tempFile = File.createTempFile("tts_audio", ".mp3", context.getCacheDir());

            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(audioData);
            }

            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(tempFile.getAbsolutePath());

            mediaPlayer.setOnPreparedListener(mp -> {
                Log.d("TTSHelper", "\uD83D\uDD0A Reprodução de áudio iniciada.");
                mp.start();
            });

            mediaPlayer.setOnCompletionListener(mp -> {
                Log.d("TTSHelper", "✅ Reprodução concluída.");
                mp.release();
                mediaPlayer = null;
                onComplete.run();
            });

            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            Log.e("TTSHelper", "❌ Erro ao reproduzir áudio: " + e.getMessage());
            onComplete.run();
        }
    }

    /**
     * Interrompe imediatamente a fala atual.
     */
    public void stopSpeaking() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
            Log.d("TTSHelper", "\uD83D\uDD07 Reprodução de áudio interrompida.");
        }
    }

    /**
     * Libera os recursos do MediaPlayer.
     */
    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}