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

public class CustomToast {

    private final Context context;

    public CustomToast(Context context) {
        this.context = context;
    }

    public void show(String message, int duration, String color, String type) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.custom_toast, null);

        TextView toastText = layout.findViewById(R.id.toastMessage);
        toastText.setText(message);
        toastText.setBackgroundColor(Color.parseColor(color));

        LinearLayout toastContainer = layout.findViewById(R.id.custom_toast_container);
        GradientDrawable border = new GradientDrawable();
        border.setColor(Color.parseColor(color));
        border.setStroke(5, Color.parseColor(color));
        border.setCornerRadius(10);
        toastContainer.setBackground(border);

        Toast toast = new Toast(context);

        Map<String, Integer> gravityMap = new HashMap<>();
        gravityMap.put("success", Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        gravityMap.put("error", Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);

        int gravity = gravityMap.getOrDefault(type.toLowerCase(), Gravity.CENTER);
        toast.setGravity(gravity, 0, 100);

        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
    }
}
