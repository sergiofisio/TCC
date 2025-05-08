// Pacote onde o componente está localizado
package com.example.emotionharmony.components;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.AttributeSet;
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

/**
 * Componente de menu inferior personalizado com navegação e botão de emergência.
 * Inclui 4 ícones de navegação e um botão "Pânico" que abre modal de emergência.
 */
public class BottomMenuView extends ConstraintLayout {

    // Views do modal e botões
    private View modalPanic, blockerView;
    private Button btnPanic, btnNao, btnSim;
    private LinearLayout btn_user, btn_exercices, btn_invoice, btn_diary;

    // Contexto da activity onde o menu será usado (para navegação)
    private Context activityContext;

    /**
     * Construtor chamado quando o componente é criado via XML.
     */
    public BottomMenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.custom_bottom_menu, this);
        initializeViews();
        setupListeners();
    }

    // Referencia os elementos de layout via findViewById
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

    // Define os comportamentos dos botões principais do modal
    private void setupListeners() {
        btnPanic.setOnClickListener(v -> showModal(View.VISIBLE, false));
        btnNao.setOnClickListener(v -> showModal(View.GONE, true));
        btnSim.setOnClickListener(v -> performDefaultSimAction());
    }

    /**
     * Exibe ou oculta o modal de emergência.
     * @param visibility View.VISIBLE ou View.GONE
     * @param enable Habilita/desabilita o botão de pânico
     */
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

    /**
     * Ação padrão ao clicar em "Sim" no modal de emergência.
     * Faz uma ligação para o telefone de emergência salvo nas preferências.
     */
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

    // Permite sobrescrever a ação padrão do botão "Sim"
    public void setOnSimClickListener(OnClickListener listener) {
        btnSim.setOnClickListener(listener);
    }

    // Permite sobrescrever a ação do botão "Não"
    public void setOnNaoClickListener(OnClickListener listener) {
        btnNao.setOnClickListener(listener);
    }

    // Permite sobrescrever a ação do botão "Pânico"
    public void setOnPanicClickListener(OnClickListener listener) {
        btnPanic.setOnClickListener(listener);
    }

    /**
     * Define o contexto da activity pai (usado para navegação).
     * Deve ser chamado ao usar o menu em uma Activity.
     */
    public void setActivityContext(Context context) {
        this.activityContext = context;
        setupNavigationListeners();
    }

    /**
     * Define os listeners dos botões de navegação do menu.
     * Evita navegar para a mesma tela onde o usuário já está.
     */
    private void setupNavigationListeners() {
        if (!(activityContext instanceof android.app.Activity)) return;

        android.app.Activity activity = (android.app.Activity) activityContext;

        btn_user.setOnClickListener(v -> {
            if (activity.getClass().equals(Page_perfil.class)) return;
            NavigationHelper.navigateTo(activity, Page_perfil.class, true);
        });

        btn_exercices.setOnClickListener(v -> {
            if (activity.getClass().equals(Page_Exercicies.class)) return;
            NavigationHelper.navigateTo(activity, Page_Exercicies.class, true);
        });

        btn_invoice.setOnClickListener(v -> {
            if (activity.getClass().equals(Page_Invoice.class)) return;
            NavigationHelper.navigateTo(activity, Page_Invoice.class, true);
        });

        btn_diary.setOnClickListener(v -> {
            if (activity.getClass().equals(Page_Diary.class)) return;
            NavigationHelper.navigateTo(activity, Page_Diary.class, true);
        });
    }
}
