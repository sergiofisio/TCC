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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.emotionharmony.R;
import com.example.emotionharmony.components.BottomMenuView;
import com.example.emotionharmony.adapters.EmotionsExpandableAdapter;
import com.example.emotionharmony.utils.ServerConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Tela de relatório (invoice) que exibe emoções, respirações e meditações organizadas por data.
 */
public class Page_Invoice extends AppCompatActivity {

    // === Declaração de variáveis ===
    private SharedPreferences preferences;
    private ProgressBar progressBar;
    private List<String> dateList;
    private ExpandableListView expandableListView;
    private Map<String, List<JSONObject>> emotionsMap;
    private Map<String, JSONObject> exercisesMeditationsMap;
    private final Handler uiHandler = new Handler(Looper.getMainLooper());

    /**
     * Inicializa a tela e configura layout e dados.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_invoice);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // === Inicializações ===
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

    // === Busca os dados do usuário da API ===
    private void fetchUserInfo(Runnable onComplete) {
        String token = preferences.getString("authToken", null);
        if (token == null) {
            onComplete.run();
            return;
        }

        ServerConnection.getRequestWithAuth("/user", token, new ServerConnection.ServerCallback() {
            @Override
            public void onSuccess(String response) {
                uiHandler.post(() -> {
                    try {
                        preferences.edit().putString("lastUserResponse", response).apply();

                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray emotionsArray = jsonResponse.optJSONArray("todays_user");
                        JSONArray breathingExercisesArray = jsonResponse.optJSONArray("breaths_user");
                        JSONArray meditationsArray = jsonResponse.optJSONArray("meditations_user");

                        organizeEmotionsByDate(emotionsArray);
                        organizeExercisesAndMeditations(breathingExercisesArray, meditationsArray);
                        setupExpandableListView();

                    } catch (JSONException e) {
                        Log.e("Page_Invoice", "Erro ao processar JSON", e);
                    }
                    onComplete.run();
                });
            }

            @Override
            public void onError(String error) {
                uiHandler.post(onComplete);
            }
        });
    }

    // === Organiza as emoções por data e por período (manhã, tarde, noite) ===
    private void organizeEmotionsByDate(JSONArray emotionsArray) {
        dateList.clear();
        emotionsMap.clear();

        Map<String, Map<String, JSONObject>> fullEmotionMap = new HashMap<>();
        Set<String> allDates = new HashSet<>();

        if (emotionsArray != null) {
            for (int i = 0; i < emotionsArray.length(); i++) {
                try {
                    JSONObject emotion = emotionsArray.getJSONObject(i);
                    String date = emotion.optString("created_at").split("T")[0];
                    String time = emotion.optString("morning_afternoon_evening").toLowerCase();
                    allDates.add(date);
                    fullEmotionMap.computeIfAbsent(date, k -> new HashMap<>()).put(time, emotion);
                } catch (JSONException e) {
                    Log.e("Page_Invoice", "Erro ao processar emoções", e);
                }
            }
        }

        collectDatesFromJSONArray(allDates);

        List<String> sortedDates = new ArrayList<>(allDates);
        sortedDates.sort(Collections.reverseOrder());

        for (String date : sortedDates) {
            dateList.add(date);
            List<JSONObject> dailyList = new ArrayList<>();
            Map<String, JSONObject> byTime = fullEmotionMap.getOrDefault(date, new HashMap<>());
            for (String time : new String[]{"manhã", "tarde", "noite"}) {
                if (byTime.containsKey(time)) {
                    dailyList.add(byTime.get(time));
                } else {
                    JSONObject missing = new JSONObject();
                    try {
                        missing.put("emotion_today", "Não realizado");
                        missing.put("morning_afternoon_evening", time);
                    } catch (JSONException e) {
                        Log.e("Page_Invoice", "Erro ao criar entrada 'não realizado'", e);
                    }
                    dailyList.add(missing);
                }
            }
            emotionsMap.put(date, dailyList);
        }
    }

    // === Coleta datas adicionais de respirações e meditações ===
    private void collectDatesFromJSONArray(Set<String> dateSet) {
        try {
            JSONObject response = new JSONObject(preferences.getString("lastUserResponse", "{}"));
            JSONArray arrays[] = {
                    response.optJSONArray("breaths_user"),
                    response.optJSONArray("meditations_user")
            };
            for (JSONArray array : arrays) {
                if (array == null) continue;
                for (int i = 0; i < array.length(); i++) {
                    String date = array.getJSONObject(i).optString("created_at").split("T")[0];
                    dateSet.add(date);
                }
            }
        } catch (JSONException e) {
            Log.e("Page_Invoice", "Erro ao extrair datas adicionais", e);
        }
    }

    // === Conta quantos registros existem para cada data ===
    private int countByDate(JSONArray array, String date) {
        if (array == null) return 0;
        int count = 0;
        for (int i = 0; i < array.length(); i++) {
            try {
                String itemDate = array.getJSONObject(i).optString("created_at").split("T")[0];
                if (itemDate.equals(date)) count++;
            } catch (JSONException e) {
                Log.e("Page_Invoice", "Erro ao contar registros", e);
            }
        }
        return count;
    }

    // === Organiza os dados de exercícios e meditações por data ===
    private void organizeExercisesAndMeditations(JSONArray breathsArray, JSONArray meditationsArray) {
        exercisesMeditationsMap.clear();
        for (String date : dateList) {
            JSONObject dailyData = new JSONObject();
            try {
                int breathCount = countByDate(breathsArray, date);
                int meditationCount = countByDate(meditationsArray, date);
                dailyData.put("breaths", breathCount);
                dailyData.put("meditations", meditationCount);
                exercisesMeditationsMap.put(date, dailyData);
            } catch (JSONException e) {
                Log.e("Page_Invoice", "Erro ao organizar exercícios e meditações", e);
            }
        }
    }

    // === Monta e exibe o ExpandableListView com os dados organizados ===
    private void setupExpandableListView() {
        EmotionsExpandableAdapter adapter = new EmotionsExpandableAdapter(this, dateList, emotionsMap, exercisesMeditationsMap, expandableListView);
        expandableListView.setAdapter(adapter);
        if (!dateList.isEmpty()) expandableListView.expandGroup(0);
    }
}
