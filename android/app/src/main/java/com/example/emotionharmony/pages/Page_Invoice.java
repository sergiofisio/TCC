package com.example.emotionharmony.pages;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.emotionharmony.R;
import com.example.emotionharmony.components.BottomMenuView;
import com.example.emotionharmony.utils.ServerConnection;
import com.example.emotionharmony.adapters.EmotionsExpandableAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Page_Invoice extends AppCompatActivity {

    private SharedPreferences preferences;
    private ProgressBar progressBar;
    private List<String> dateList;
    private ExpandableListView expandableListView;
    private Map<String, List<JSONObject>> emotionsMap;
    private Map<String, JSONObject> exercisesMeditationsMap;
    private final Handler uiHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_invoice);

        preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        progressBar = findViewById(R.id.progressBarRelatorio);
        expandableListView = findViewById(R.id.expandableListView);

        BottomMenuView bottomMenu = findViewById(R.id.bottomMenu);
        bottomMenu.setActivityContext(this);

        dateList = new ArrayList<>();
        emotionsMap = new HashMap<>();
        exercisesMeditationsMap = new HashMap<>();

        fetchUserInfo(() -> progressBar.setVisibility(View.GONE));
    }

    private void fetchUserInfo(Runnable onComplete) {
        String token = preferences.getString("authToken", null);
        if (token == null) {
            onComplete.run();
            return;
        }

        ServerConnection.getRequestWithAuth("/auth/user", token, new ServerConnection.ServerCallback() {
            @Override
            public void onSuccess(String response) {
                uiHandler.post(() -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray emotionsArray = jsonResponse.optJSONArray("todays_user");
                        JSONArray breathingExercisesArray = jsonResponse.optJSONArray("breaths_user");
                        JSONArray meditationsArray = jsonResponse.optJSONArray("meditations_user");

                        organizeEmotionsByDate(emotionsArray);
                        organizeExercisesAndMeditations(breathingExercisesArray, meditationsArray);
                        setupExpandableListView();

                    } catch (JSONException e) {
                        Log.e("Page_Invoice", "❌ Erro ao processar JSON", e);
                    }
                    onComplete.run();
                });
            }

            @Override
            public void onError(String error) {
                if (onComplete != null) uiHandler.post(onComplete);
            }
        });
    }

    private void organizeEmotionsByDate(JSONArray emotionsArray) {
        dateList.clear();
        emotionsMap.clear();

        if (emotionsArray == null || emotionsArray.length() == 0) return;

        Map<String, List<JSONObject>> tempMap = new HashMap<>();

        for (int i = 0; i < emotionsArray.length(); i++) {
            try {
                JSONObject emotion = emotionsArray.getJSONObject(i);
                String date = emotion.optString("created_at").split("T")[0];

                if (!tempMap.containsKey(date)) {
                    tempMap.put(date, new ArrayList<>());
                }

                Objects.requireNonNull(tempMap.get(date)).add(emotion);

            } catch (JSONException e) {
                Log.e("Page_Invoice", "❌ Erro ao organizar emoções", e);
            }
        }

        List<String> sortedDates = new ArrayList<>(tempMap.keySet());
        sortedDates.sort(Collections.reverseOrder());

        for (String date : sortedDates) {
            List<JSONObject> sortedEmotions = tempMap.get(date);
            assert sortedEmotions != null;
            sortedEmotions.sort(Comparator.comparing(o -> {
                try {
                    return getTimeOrder(o.getString("morning_afternoon_evening"));
                } catch (JSONException e) {
                    return 4;
                }
            }));

            dateList.add(date);
            emotionsMap.put(date, sortedEmotions);
        }
    }

    private void organizeExercisesAndMeditations(JSONArray breathsArray, JSONArray meditationsArray) {
        exercisesMeditationsMap.clear();

        for (String date : dateList) {
            JSONObject dailyData = new JSONObject();
            try {
                dailyData.put("morningBreaths", countByDate(breathsArray, date));
                dailyData.put("afternoonBreaths", countByDate(breathsArray, date));
                dailyData.put("nightBreaths", countByDate(breathsArray, date));

                dailyData.put("morningMeditations", countByDate(meditationsArray, date));
                dailyData.put("afternoonMeditations", countByDate(meditationsArray, date));
                dailyData.put("nightMeditations", countByDate(meditationsArray, date));

                exercisesMeditationsMap.put(date, dailyData);
            } catch (JSONException e) {
                Log.e("Page_Invoice", "❌ Erro ao organizar exercícios e meditações", e);
            }
        }
    }

    private int countByDate(JSONArray array, String date) {
        if (array == null) return 0;
        int count = 0;
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject item = array.getJSONObject(i);
                String itemDate = item.optString("created_at").split("T")[0];

                if (itemDate.equals(date)) {
                    count++;
                }
            } catch (JSONException e) {
                Log.e("Page_Invoice", "❌ Erro ao contar registros", e);
            }
        }
        return count;
    }

    private void setupExpandableListView() {
        EmotionsExpandableAdapter adapter = new EmotionsExpandableAdapter(this, dateList, emotionsMap, exercisesMeditationsMap, expandableListView);
        expandableListView.setAdapter(adapter);

        if (!dateList.isEmpty()) {
            expandableListView.expandGroup(0);
        }
    }

    private int getTimeOrder(String timeOfDay) {
        switch (timeOfDay.toLowerCase()) {
            case "manhã": return 1;
            case "tarde": return 2;
            case "noite": return 3;
            default: return 4;
        }
    }
}
