// Pacote onde está localizado o adaptador
package com.example.emotionharmony.adapters;

// Importações necessárias para funcionamento do Adapter e componentes Android
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.emotionharmony.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * Adapter para exibir emoções agrupadas por data em um ExpandableListView.
 */
public class EmotionsExpandableAdapter extends BaseExpandableListAdapter {

    // Variáveis principais do adaptador
    private final Context context;
    private final List<String> dateList; // Lista de datas
    private final Map<String, List<JSONObject>> emotionsMap; // Mapeamento de emoções por data
    private final Map<String, JSONObject> exercisesMeditationsMap; // Mapeamento de exercícios e meditações por data
    private final ExpandableListView expandableListView;
    private int lastExpandedGroupPosition = 0;

    // Construtor do adaptador
    public EmotionsExpandableAdapter(Context context, List<String> dateList,
                                     Map<String, List<JSONObject>> emotionsMap,
                                     Map<String, JSONObject> exercisesMeditationsMap,
                                     ExpandableListView expandableListView) {
        this.context = context;
        this.dateList = dateList;
        this.emotionsMap = emotionsMap;
        this.exercisesMeditationsMap = exercisesMeditationsMap;
        this.expandableListView = expandableListView;
    }

    // Número total de grupos (datas)
    @Override
    public int getGroupCount() {
        return dateList.size();
    }

    // Número de filhos (emoções) por grupo
    @Override
    public int getChildrenCount(int groupPosition) {
        return Objects.requireNonNull(emotionsMap.get(dateList.get(groupPosition))).size();
    }

    // Retorna a data do grupo
    @Override
    public Object getGroup(int groupPosition) {
        return dateList.get(groupPosition);
    }

    // Retorna a emoção do filho
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return Objects.requireNonNull(emotionsMap.get(dateList.get(groupPosition))).get(childPosition);
    }

    // IDs dos grupos e filhos
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    // Indica que os IDs não são estáveis
    @Override
    public boolean hasStableIds() {
        return false;
    }

    // Criação da view do grupo (data)
    @SuppressLint("SetTextI18n")
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String date = (String) getGroup(groupPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.group_item, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.groupTitle);
        textView.setText("🗓️ " + formatDateToFriendly(date));

        // Expande/fecha o grupo ao clicar
        convertView.setOnClickListener(v -> {
            if (expandableListView.isGroupExpanded(groupPosition)) {
                expandableListView.collapseGroup(groupPosition);
            } else {
                expandableListView.expandGroup(groupPosition);
                if (lastExpandedGroupPosition != -1 && lastExpandedGroupPosition != groupPosition) {
                    expandableListView.collapseGroup(lastExpandedGroupPosition);
                }
                lastExpandedGroupPosition = groupPosition;
            }
        });

        return convertView;
    }

    // Criação da view dos filhos (emoções)
    @SuppressLint("SetTextI18n")
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        List<JSONObject> emotions = new ArrayList<>(Objects.requireNonNull(emotionsMap.get(dateList.get(groupPosition))));
        sortEmotionsByTime(emotions); // Ordena manhã -> tarde -> noite

        JSONObject emotion = emotions.get(childPosition);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.child_item, parent, false);
        }

        // Referências dos elementos da view
        ImageView imageView = convertView.findViewById(R.id.emotionIcon);
        TextView textViewTime = convertView.findViewById(R.id.emotionTime);
        TextView emotionDescription = convertView.findViewById(R.id.emotionDescription);
        TextView textView = convertView.findViewById(R.id.textView);
        TextView textViewBreath = convertView.findViewById(R.id.breathingExercisesCount);
        TextView textViewMeditation = convertView.findViewById(R.id.meditationsCount);

        // Preenche os dados da emoção
        String timeOfDay = emotion.optString("morning_afternoon_evening", "Indefinido");
        String type = emotion.optString("emotion_today", "Não realizado");

        textViewTime.setText(timeOfDay);
        emotionDescription.setText(type);
        imageView.setImageResource(getEmotionDrawable(type));

        // Exibe exercícios apenas no primeiro item (manhã)
        if (childPosition == 0) {
            String date = dateList.get(groupPosition);
            JSONObject exerciseData = exercisesMeditationsMap.getOrDefault(date, new JSONObject());

            int breathCount = exerciseData.optInt("breaths", 0);
            int meditationCount = exerciseData.optInt("meditations", 0);

            textView.setVisibility(View.VISIBLE);
            textViewBreath.setVisibility(View.VISIBLE);
            textViewMeditation.setVisibility(View.VISIBLE);

            textViewBreath.setText("Respiratórios: " + breathCount);
            textViewMeditation.setText("Meditações: " + meditationCount);
        } else {
            textView.setVisibility(View.GONE);
            textViewBreath.setVisibility(View.GONE);
            textViewMeditation.setVisibility(View.GONE);
        }

        return convertView;
    }

    // Permite que os filhos sejam selecionáveis
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    // Retorna o drawable correto com base na emoção
    private int getEmotionDrawable(String type) {
        switch (type) {
            case "Felicidade": return R.drawable.happy;
            case "Tristeza": return R.drawable.sad;
            case "Raiva": return R.drawable.angry;
            case "Desgosto": return R.drawable.desgust;
            case "Medo": return R.drawable.fear;
            default: return R.drawable.choose_emotion;
        }
    }

    // Ordena as emoções por turno do dia
    private void sortEmotionsByTime(List<JSONObject> emotions) {
        emotions.sort((o1, o2) -> {
            try {
                return getTimeOrder(o1.getString("morning_afternoon_evening")) -
                        getTimeOrder(o2.getString("morning_afternoon_evening"));
            } catch (JSONException e) {
                return 0;
            }
        });
    }

    // Define a ordem dos períodos do dia
    private int getTimeOrder(String timeOfDay) {
        switch (timeOfDay.toLowerCase()) {
            case "manhã": return 1;
            case "tarde": return 2;
            case "noite": return 3;
            default: return 4;
        }
    }

    // Formata a data de yyyy-MM-dd para "dd de MMMM de yyyy"
    private String formatDateToFriendly(String rawDate) {
        try {
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date date = parser.parse(rawDate);
            SimpleDateFormat formatter = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("pt", "BR"));
            assert date != null;
            return capitalizeFirstLetter(formatter.format(date));
        } catch (Exception e) {
            return rawDate;
        }
    }

    // Capitaliza a primeira letra da string
    private String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}
