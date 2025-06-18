package com.example.emotionharmony.pages;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.util.TypedValue;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.emotionharmony.R;
import com.example.emotionharmony.components.BottomMenuView;
import com.example.emotionharmony.databinding.ActivityPageDiaryBinding;
import com.example.emotionharmony.pages.diary.PageHabit;
import com.example.emotionharmony.pages.diary.PageWater;
import com.example.emotionharmony.pages.diary.PageWater2;
import com.example.emotionharmony.utils.NavigationHelper;
import com.example.emotionharmony.utils.ServerConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Page_Diary extends AppCompatActivity {

    private CardView    cardStartWater, cardProgressWater;
    private ProgressBar progressWater;
    private TextView    textWaterAmount;
    private Button      btnStartWater;

    private CardView    cardAddHabit;
    private LinearLayout containerLayout;
    private Button      btnStartHabit;

    private Integer     Weight;
    private String      token, sleepTimeStart, sleepTimeEnd;

    private ArrayList<Habit> habitList = new ArrayList<>();

    public static class Habit {
        int    id;
        String name;
        Date   lastTime;
        public Habit(int id, String name, Date lastTime) {
            this.id = id;
            this.name = name;
            this.lastTime = lastTime;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // fullscreen
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );

        ActivityPageDiaryBinding binding = ActivityPageDiaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // bottom menu
        BottomMenuView bottomMenu = findViewById(R.id.bottomMenu);
        bottomMenu.setActivityContext(this);

        // água
        cardStartWater    = findViewById(R.id.waterCard_start);
        cardProgressWater = findViewById(R.id.waterCard_progress);
        btnStartWater     = findViewById(R.id.btnStart_Water);
        progressWater     = findViewById(R.id.progressWater);
        textWaterAmount   = findViewById(R.id.textWaterAmount);

        btnStartWater.setOnClickListener(v ->
                startActivity(new Intent(Page_Diary.this, PageWater.class))
        );
        cardProgressWater.setOnClickListener(v ->
                NavigationHelper.navigateTo(Page_Diary.this, PageWater2.class, true)
        );

        // hábitos
        cardAddHabit    = findViewById(R.id.waterCard_habit);
        containerLayout = findViewById(R.id.linearlayout);
        btnStartHabit   = findViewById(R.id.btnStart_Habit);

        btnStartHabit.setOnClickListener(v ->
                NavigationHelper.navigateTo(Page_Diary.this, PageHabit.class, true)
        );

        // token
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        token = prefs.getString("authToken", null);

        // carrega dados
        fetchDataUser();
        handleIncomingHabit();
        loadAndDisplayHabitData();
    }

    private void handleIncomingHabit() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("habitName") && intent.hasExtra("habitDate")) {
            String name = intent.getStringExtra("habitName");
            long millis = intent.getLongExtra("habitDate", 0L);
            habitList.add(new Habit(0, name, new Date(millis)));
            cardAddHabit.setVisibility(View.GONE);
        }
    }

    private void loadAndDisplayHabitData() {
        if (habitList.isEmpty()) {
            cardAddHabit.setVisibility(View.VISIBLE);
            return;
        }
        cardAddHabit.setVisibility(View.GONE);

        // adiciona cards de hábito
        for (Habit h : habitList) {
            addHabitCard(h);
        }
    }

    private void addHabitCard(Habit habit) {
        // cria novo CardView
        CardView card = new CardView(this);

        // aplica mesmo background, raio e elevação do template de XML
        CardView template = findViewById(R.id.habitCardTemplate);
        card.setCardBackgroundColor(template.getCardBackgroundColor().getDefaultColor());
        card.setRadius(template.getRadius());
        card.setCardElevation(template.getCardElevation());

        // calcula 24dp em pixels
        float scale = getResources().getDisplayMetrics().density;
        int marginSidePx = (int)(24 * scale + 0.5f);
        int marginTopPx  = (int)(16 * scale + 0.5f);
        int padPx        = (int)(24 * scale + 0.5f);

        // define LayoutParams com MATCH_PARENT e margens
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        lp.setMargins(marginSidePx, marginTopPx, marginSidePx, 0);
        card.setLayoutParams(lp);

        // conteúdo interno com padding
        LinearLayout inner = new LinearLayout(this);
        inner.setOrientation(LinearLayout.VERTICAL);
        inner.setPadding(padPx, padPx, padPx, padPx);

        // título
        TextView tmpT = template.findViewById(R.id.habitTitle);
        TextView title = new TextView(this);
        title.setTextSize(TypedValue.COMPLEX_UNIT_PX, tmpT.getTextSize());
        title.setTextColor(tmpT.getTextColors());
        title.setTypeface(tmpT.getTypeface());
        title.setTextAlignment(tmpT.getTextAlignment());
        title.setText(habit.name);

        // info de tempo
        TextView tmpI = template.findViewById(R.id.habitTimeInfo);
        TextView info = new TextView(this);
        info.setTextSize(TypedValue.COMPLEX_UNIT_PX, tmpI.getTextSize());
        info.setTextColor(tmpI.getTextColors());
        info.setTypeface(tmpI.getTypeface());
        info.setTextAlignment(tmpI.getTextAlignment());
        info.setText("Última vez que realizou foi há:\n" + getTimeDiffText(habit.lastTime));

        inner.addView(title);
        inner.addView(info);
        card.addView(inner);

        // **NOVO**: ao clicar no card, vai para PageHabit
        card.setClickable(true);
        card.setOnClickListener(v ->
                NavigationHelper.navigateTo(Page_Diary.this, PageHabit.class, true)
        );

        // adiciona ao container
        containerLayout.addView(card);
    }

    private String getTimeDiffText(Date lastTime) {
        long diff = System.currentTimeMillis() - lastTime.getTime();
        long days = TimeUnit.MILLISECONDS.toDays(diff);
        long hrs  = TimeUnit.MILLISECONDS.toHours(diff) - days * 24;
        long min  = TimeUnit.MILLISECONDS.toMinutes(diff)
                - TimeUnit.MILLISECONDS.toHours(diff) * 60;

        if (days > 0) {
            return String.format(Locale.getDefault(),
                    "%dd %02dh %02dmin", days, hrs, min);
        } else {
            return String.format(Locale.getDefault(),
                    "%02dh %02dmin", hrs, min);
        }
    }

    private void fetchDataUser() {
        ServerConnection.getRequestWithAuth("/user", token, new ServerConnection.ServerCallback() {
            @Override public void onSuccess(String response) {
                runOnUiThread(() -> {
                    try {
                        JSONObject j = new JSONObject(response);
                        Weight         = j.optInt("weight_user", 0);
                        sleepTimeStart = j.optString("sleep_time_start");
                        sleepTimeEnd   = j.optString("sleep_time_end");
                        if (Weight > 0) loadAndDisplayWaterData();
                    } catch (JSONException e) {
                        Log.e("Page_Diary", e.getMessage());
                    }
                });
            }
            @Override public void onError(String error) {
                runOnUiThread(() ->
                        Log.e("Page_Diary", error)
                );
            }
        });
    }

    private void loadAndDisplayWaterData() {
        try {
            int totalDiario        = calculateWaterIntake(Weight);
            int horasAcordadoTotal = fetchTotalIngeridoFromDb();
            if (horasAcordadoTotal == 0) return;

            String[] hm = sleepTimeStart.substring(11,16).split(":");
            int h = Integer.parseInt(hm[0]), m = Integer.parseInt(hm[1]);

            java.util.Calendar agora   = java.util.Calendar.getInstance();
            java.util.Calendar acordou = java.util.Calendar.getInstance();
            acordou.set(java.util.Calendar.HOUR_OF_DAY, h);
            acordou.set(java.util.Calendar.MINUTE, m);
            acordou.set(java.util.Calendar.SECOND, 0);
            acordou.set(java.util.Calendar.MILLISECOND, 0);
            if (acordou.after(agora)) acordou.add(
                    java.util.Calendar.DAY_OF_YEAR, -1);

            long minutosDesde = TimeUnit.MILLISECONDS.toMinutes(
                    agora.getTimeInMillis() - acordou.getTimeInMillis()
            );

            double aguaPorHora      = (double) totalDiario / horasAcordadoTotal;
            double ingeridoPrevisto = aguaPorHora * (minutosDesde / 60.0);
            int totalIngerido       = (int) ingeridoPrevisto;

            if (totalIngerido > 0) {
                showWaterProgress(totalIngerido);
            } else {
                cardStartWater.setVisibility(View.VISIBLE);
                cardProgressWater.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.e("Page_Diary", "Erro em loadAndDisplayWaterData: " + e.getMessage());
        }
    }

    private void showWaterProgress(int totalIngerido) {
        int setMax = calculateWaterIntake(Weight);
        cardStartWater.setVisibility(View.GONE);
        cardProgressWater.setVisibility(View.VISIBLE);
        progressWater.setMax(setMax);
        progressWater.setProgress(totalIngerido);
        textWaterAmount.setText(totalIngerido + " ml");
    }

    private int fetchTotalIngeridoFromDb() {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            sdf.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));

            Date acordou = sdf.parse(sleepTimeStart);
            Date dormiu  = sdf.parse(sleepTimeEnd);
            long diff = dormiu.after(acordou)
                    ? dormiu.getTime() - acordou.getTime()
                    : dormiu.getTime() + TimeUnit.DAYS.toMillis(1) - acordou.getTime();

            long totalMin = TimeUnit.MILLISECONDS.toMinutes(diff);
            return (int)(totalMin / 60);
        } catch (Exception e) {
            Log.e("Page_Diary", "Erro ao calcular tempo acordado: " + e.getMessage());
            return 0;
        }
    }

    private int calculateWaterIntake(int weight) {
        return weight * 45;
    }
}
