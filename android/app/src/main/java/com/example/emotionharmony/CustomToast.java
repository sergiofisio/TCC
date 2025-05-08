package com.example.emotionharmony;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe responsável por exibir Toasts personalizados com cor e posição ajustáveis.
 */
public class CustomToast {

    private final Context context;

    /**
     * Construtor que recebe o contexto para a criação do Toast.
     *
     * @param context o contexto da aplicação/atividade atual
     */
    public CustomToast(Context context) {
        this.context = context;
    }

    /**
     * Exibe um Toast customizado com mensagem, duração, cor de fundo e tipo (posição).
     *
     * @param message mensagem a ser exibida no Toast
     * @param duration duração do Toast (Toast.LENGTH_SHORT ou Toast.LENGTH_LONG)
     * @param color cor de fundo do Toast em formato hexadecimal (ex: "#FF0000")
     * @param type tipo de Toast que define a posição ("success" = topo, "error" = fundo)
     */
    public void show(String message, int duration, String color, String type) {
        // Infla o layout personalizado
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.custom_toast, null);

        // Define a mensagem e cor de fundo do texto
        TextView toastText = layout.findViewById(R.id.toastMessage);
        toastText.setText(message);
        toastText.setBackgroundColor(Color.parseColor(color));

        // Define o fundo com borda arredondada e cor personalizada
        LinearLayout toastContainer = layout.findViewById(R.id.custom_toast_container);
        GradientDrawable border = new GradientDrawable();
        border.setColor(Color.parseColor(color));
        border.setStroke(5, Color.parseColor(color));
        border.setCornerRadius(10);
        toastContainer.setBackground(border);

        // Cria o Toast e configura sua posição com base no tipo
        Toast toast = new Toast(context);
        Map<String, Integer> gravityMap = new HashMap<>();
        gravityMap.put("success", Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        gravityMap.put("error", Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);

        int gravity = gravityMap.getOrDefault(type.toLowerCase(), Gravity.CENTER);
        toast.setGravity(gravity, 0, 100);

        // Define duração e layout, e exibe o Toast
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
    }
}