package com.example.emotionharmony.pages;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import android.util.TypedValue;

import com.example.emotionharmony.R;
import com.example.emotionharmony.components.BottomMenuView;
import com.example.emotionharmony.databinding.ActivityPageDiaryBinding;
import com.example.emotionharmony.pages.diary.PageHabit;
import com.example.emotionharmony.pages.diary.PageWater;
import com.example.emotionharmony.utils.NavigationHelper;

public class Page_Diary extends AppCompatActivity {

    private CardView cardStartWater;
    private CardView cardProgressWater;
    private Button btnStartWater;
    private ProgressBar progressWater;
    private TextView textWaterAmount;
    private CardView cardAddHabit;
    private LinearLayout containerLayout;

    public static class Habit {
        int id;
        String name;
        Date lastTime;

        public Habit(int id, String name, Date lastTime) {
            this.id = id;
            this.name = name;
            this.lastTime = lastTime;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configura tela cheia
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );

        ActivityPageDiaryBinding binding = ActivityPageDiaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomMenuView bottomMenu = findViewById(R.id.bottomMenu);
        bottomMenu.setActivityContext(this);

        cardStartWater    = findViewById(R.id.waterCard_start);
        cardProgressWater = findViewById(R.id.waterCard_progress);
        btnStartWater     = findViewById(R.id.btnStart_Water);
        progressWater     = findViewById(R.id.progressWater);
        textWaterAmount   = findViewById(R.id.textWaterAmount);

        // ID CORRIGIDO: waterCard_habit em vez de cardHabit_water
        cardAddHabit = findViewById(R.id.waterCard_habit);
        containerLayout = findViewById(R.id.linearlayout);

        btnStartWater.setOnClickListener(v -> {
            Intent intent = new Intent(Page_Diary.this, PageWater.class);
            startActivity(intent);
            showWaterProgress(0);
        });

        Button btnStartHabit = findViewById(R.id.btnStart_Habit);
        btnStartHabit.setOnClickListener(v ->
                NavigationHelper.navigateTo(Page_Diary.this, PageHabit.class, true)
        );


        loadAndDisplayWaterData();
        loadAndDisplayHabitData();
    }

    private ArrayList<Habit> fetchHabitsFromDb() {
        ArrayList<Habit> list = new ArrayList<>();
        list.add(new Habit(1, "Fumar", new Date(System.currentTimeMillis() - 85400000)));
        list.add(new Habit(2, "Beber", new Date(System.currentTimeMillis() - 3600000)));
        return list;
    }

    private void loadAndDisplayHabitData() {
        ArrayList<Habit> habits = fetchHabitsFromDb();

        if (habits.isEmpty()) {
            cardAddHabit.setVisibility(View.VISIBLE);
        } else {
            cardAddHabit.setVisibility(View.GONE);
            for (Habit habit : habits) {
                addHabitCard(habit);
            }
        }
    }

    private void addHabitCard(Habit habit) {
        // Clonar o template
        CardView template = findViewById(R.id.habitCardTemplate);
        CardView card = new CardView(this);

        // Copiar atributos do template
        card.setCardBackgroundColor(template.getCardBackgroundColor().getDefaultColor());
        card.setRadius(template.getRadius());
        card.setCardElevation(template.getCardElevation());

        // Configurar layout params
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) template.getLayoutParams();
        card.setLayoutParams(new LinearLayout.LayoutParams(params));

        // Clonar o conteúdo interno
        LinearLayout templateLayout = (LinearLayout) template.getChildAt(0);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(
                templateLayout.getPaddingLeft(),
                templateLayout.getPaddingTop(),
                templateLayout.getPaddingRight(),
                templateLayout.getPaddingBottom()
        );
        layout.setGravity(templateLayout.getGravity());

        // Clonar TextViews
        TextView title = new TextView(this);
        TextView templateTitle = templateLayout.findViewById(R.id.habitTitle);
        title.setTextSize(TypedValue.COMPLEX_UNIT_PX, templateTitle.getTextSize());
        title.setTextColor(templateTitle.getTextColors());
        title.setTypeface(templateTitle.getTypeface());
        title.setTextAlignment(templateTitle.getTextAlignment());
        title.setText(habit.name);

        TextView timeInfo = new TextView(this);
        TextView templateTime = templateLayout.findViewById(R.id.habitTimeInfo);
        timeInfo.setTextSize(TypedValue.COMPLEX_UNIT_PX, templateTime.getTextSize());
        timeInfo.setTextColor(templateTime.getTextColors());
        timeInfo.setTypeface(templateTime.getTypeface());
        timeInfo.setTextAlignment(templateTime.getTextAlignment());
        timeInfo.setText("Última vez que realizou foi há:\n" + getTimeDiffText(habit.lastTime));

        // Adicionar elementos ao layout
        layout.addView(title);
        layout.addView(timeInfo);
        card.addView(layout);

        // Adicionar ao container
        containerLayout.addView(card);
    }

    private String getTimeDiffText(Date lastTime) {
        long diffMillis = System.currentTimeMillis() - lastTime.getTime();
        long hours = TimeUnit.MILLISECONDS.toHours(diffMillis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diffMillis) % 60;
        return String.format(Locale.getDefault(), "%02dh %02dmin", hours, minutes);
    }

    private void loadAndDisplayWaterData() {
        int totalIngerido = fetchTotalIngeridoFromDb();
        if (totalIngerido > 0) {
            showWaterProgress(totalIngerido);
        } else {
            cardStartWater.setVisibility(View.VISIBLE);
            cardProgressWater.setVisibility(View.GONE);
        }
    }

    private void showWaterProgress(int totalIngerido) {
        cardStartWater.setVisibility(View.GONE);
        cardProgressWater.setVisibility(View.VISIBLE);
        progressWater.setMax(2000);
        progressWater.setProgress(totalIngerido);
        textWaterAmount.setText(totalIngerido + " ml");
    }

    private int fetchTotalIngeridoFromDb() {
        return 700;
    }
}