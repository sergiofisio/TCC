package com.example.emotionharmony.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

/**
 * Classe utilitária responsável por verificar a conectividade de internet do dispositivo.
 */
public class NetworkUtils {

    /**
     * Bloco: Verificação de conexão com a internet.
     *
     * Este método verifica se há uma conexão de rede ativa e se ela é válida
     * por meio de Wi-Fi, dados móveis (celular) ou cabo Ethernet.
     *
     * @param context Contexto da aplicação para acesso ao sistema de conectividade.
     * @return true se houver internet disponível; false caso contrário.
     */
    @SuppressLint("ObsoleteSdkInt")
    public static boolean isInternetAvailable(Context context) {
        // Obtém o serviço de conectividade do sistema
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Verifica se foi possível obter o gerenciador
        if (connectivityManager == null) return false;

        // Obtém a rede ativa
        Network network = connectivityManager.getActiveNetwork();
        if (network == null) return false;

        // Verifica as capacidades da rede ativa
        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);

        // Verifica se a rede possui algum dos tipos de transporte válidos
        return capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
    }
}
