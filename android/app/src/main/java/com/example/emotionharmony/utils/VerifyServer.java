package com.example.emotionharmony.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class VerifyServer {

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private static int retryCount = 0;
    private static final int MAX_RETRIES = 5;
    private static final long RETRY_INTERVAL = 60000; // 1 minuto

    public interface OnVerificationSuccess {
        void run();
    }

    public static void verificar(Context context, OnVerificationSuccess onSuccess) {
        executor.execute(() -> ServerConnection.getRequest("/", new ServerConnection.ServerCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    boolean init = json.optBoolean("init", false);
                    if (init) {
                        retryCount = 0; // reset ao ter sucesso
                        runOnMainThread(onSuccess::run);
                    } else {
                        tentarNovamente(context, onSuccess);
                    }
                } catch (JSONException e) {
                    Log.e("VerifyServer", "Erro ao processar JSON: " + e.getMessage());
                    tentarNovamente(context, onSuccess);
                }
            }

            @Override
            public void onError(String error) {
                Log.e("VerifyServer", "Erro ao conectar: " + error);
                tentarNovamente(context, onSuccess);
            }
        }));
    }

    private static void tentarNovamente(Context context, OnVerificationSuccess onSuccess) {
        retryCount++;
        if (retryCount < MAX_RETRIES) {
            Log.w("VerifyServer", "⚠️ Tentativa " + retryCount + " falhou. Tentando novamente em " + (RETRY_INTERVAL / 1000) + " segundos...");
            scheduler.schedule(() -> verificar(context, onSuccess), RETRY_INTERVAL, TimeUnit.MILLISECONDS);
        } else {
            Log.e("VerifyServer", "❌ Não foi possível conectar após " + MAX_RETRIES + " tentativas.");
            mostrarErroFinal(context);
        }
    }

    private static void mostrarErroFinal(Context context) {
        runOnMainThread(() -> new AlertDialog.Builder(context)
                .setTitle("Erro de Conexão")
                .setMessage("Não foi possível conectar ao servidor após várias tentativas. Tente novamente mais tarde.")
                .setPositiveButton("OK", (dialog, which) -> System.exit(0)) // ou ((Activity) context).finishAffinity()
                .setCancelable(false)
                .show());
    }

    private static void runOnMainThread(Runnable action) {
        new Handler(Looper.getMainLooper()).post(action);
    }
}
