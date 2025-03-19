package com.example.emotionharmony.components;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.emotionharmony.R;
import com.example.emotionharmony.pages.Page_Exercicies;
import com.example.emotionharmony.pages.Page_Diary;
import com.example.emotionharmony.pages.Page_Invoice;
import com.example.emotionharmony.pages.Page_perfil;
import com.example.emotionharmony.utils.NavigationHelper;

public class BottomMenuView extends ConstraintLayout {
    private View modalPanic, blockerView;
    private Button btnPanic, btnNao, btnSim;
    private LinearLayout btn_user, btn_exercices, btn_invoice, btn_diary;
    private Context activityContext;

    public BottomMenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.custom_bottom_menu, this);
        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        modalPanic = findViewById(R.id.ModalPanic);
        blockerView = findViewById(R.id.blockerView);
        btnPanic = findViewById(R.id.btn_panic);
        btnSim = findViewById(R.id.btnSim);
        btnNao = findViewById(R.id.btnNao);
        btn_user = findViewById(R.id.btn_user);
        btn_exercices = findViewById(R.id.btn_exercices);
        btn_invoice = findViewById(R.id.btn_invoice);
        btn_diary = findViewById(R.id.btn_diary);
    }

    private void setupListeners() {
        btnPanic.setOnClickListener(v -> showModal(View.VISIBLE, false));
        btnNao.setOnClickListener(v -> showModal(View.GONE, true));
        btnSim.setOnClickListener(v -> performDefaultSimAction());
    }

    public void showModal(int visibility, boolean enable) {
        if (visibility == View.VISIBLE) {
            modalPanic.setAlpha(0f);
            modalPanic.setVisibility(View.VISIBLE);
            blockerView.setVisibility(View.VISIBLE);
            modalPanic.animate().alpha(1f).setDuration(300).start();
        } else {
            modalPanic.animate().alpha(0f).setDuration(300).withEndAction(() -> {
                modalPanic.setVisibility(View.GONE);
                blockerView.setVisibility(View.GONE);
            }).start();

        }
        btnPanic.setEnabled(enable);
    }

    private void performDefaultSimAction() {
        SharedPreferences preferences = getContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String emergencyPhone = preferences.getString("emergency_phone", null);

        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + emergencyPhone));
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(callIntent);

        System.out.println("📞 Ligando para: " + emergencyPhone);
        showModal(View.GONE, true);
    }

    public void setOnSimClickListener(OnClickListener listener) {
        btnSim.setOnClickListener(listener);
    }

    public void setOnNaoClickListener(OnClickListener listener) {
        btnNao.setOnClickListener(listener);
    }

    public void setOnPanicClickListener(OnClickListener listener) {
        btnPanic.setOnClickListener(listener);
    }

    public void setActivityContext(Context context) {
        this.activityContext = context;
        setupNavigationListeners();
    }

    private void setupNavigationListeners() {
        if (!(activityContext instanceof android.app.Activity)) {
            return;
        }

        android.app.Activity activity = (android.app.Activity) activityContext;

        btn_user.setOnClickListener(v -> {
            if(activity.getClass().equals(Page_perfil.class)) return;
            NavigationHelper.navigateTo(activity, Page_perfil.class, true);
        });

        btn_exercices.setOnClickListener(v -> {
                    if(activity.getClass().equals(Page_Exercicies.class)) return;
                    NavigationHelper.navigateTo(activity, Page_Exercicies.class, true);
                });

        btn_invoice.setOnClickListener(v -> {
            if(activity.getClass().equals(Page_Invoice.class)) return;
            NavigationHelper.navigateTo(activity, Page_Invoice.class, true);
        });

        btn_diary.setOnClickListener(v -> {
            if(activity.getClass().equals(Page_Diary.class)) return;
            NavigationHelper.navigateTo(activity, Page_Diary.class, true);
        });
    }
}
