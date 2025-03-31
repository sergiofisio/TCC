package com.example.emotionharmony.adapters;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EmotionsExpandableAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private final List<String> dateList;
    private final Map<String, List<JSONObject>> emotionsMap;
    private final Map<String, JSONObject> exercisesMeditationsMap;
    private final ExpandableListView expandableListView;
    private int lastExpandedGroupPosition = 0;

    public EmotionsExpandableAdapter(Context context, List<String> dateList, Map<String, List<JSONObject>> emotionsMap,
                                     Map<String, JSONObject> exercisesMeditationsMap, ExpandableListView expandableListView) {
        this.context = context;
        this.dateList = dateList;
        this.emotionsMap = emotionsMap;
        this.exercisesMeditationsMap = exercisesMeditationsMap;
        this.expandableListView = expandableListView;
    }

    @Override
    public int getGroupCount() {
        return dateList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return Objects.requireNonNull(emotionsMap.get(dateList.get(groupPosition))).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return dateList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return Objects.requireNonNull(emotionsMap.get(dateList.get(groupPosition))).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String date = (String) getGroup(groupPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.group_item, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.groupTitle);
        textView.setText("📅 " + date);
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

    @SuppressLint("SetTextI18n")
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        List<JSONObject> emotions = new ArrayList<>(emotionsMap.get(dateList.get(groupPosition)));
        sortEmotionsByTime(emotions);

        JSONObject emotion = emotions.get(childPosition);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.child_item, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.emotionIcon);
        TextView textViewBreath = convertView.findViewById(R.id.breathingExercisesCount), textViewTime = convertView.findViewById(R.id.emotionTime), emotionDescription = convertView.findViewById(R.id.emotionDescription), textViewMeditation = convertView.findViewById(R.id.meditationsCount);

        String timeOfDay = emotion.optString("morning_afternoon_evening", "Indefinido");
        String type = emotion.optString("emotion_today", "Desconhecido");
        textViewTime.setText(timeOfDay);
        emotionDescription.setText(type);
        imageView.setImageResource(getEmotionDrawable(type));

        String date = dateList.get(groupPosition);
        JSONObject exerciseData = exercisesMeditationsMap.getOrDefault(date, new JSONObject());

        assert exerciseData != null;
        int breathCount = exerciseData.optInt(getBreathKey(timeOfDay), 0);
        int meditationCount = exerciseData.optInt(getMeditationKey(timeOfDay), 0);

        textViewBreath.setText("Respiratórios: " + breathCount);
        textViewMeditation.setText("Meditações: " + meditationCount);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private int getEmotionDrawable(String type) {
        switch (type) {
            case "Felicidade":
                return R.drawable.happy;
            case "Tristeza":
                return R.drawable.sad;
            case "Raiva":
                return R.drawable.angry;
            case "Desgosto":
                return R.drawable.desgust;
            case "Medo":
                return R.drawable.fear;
            default:
                return R.drawable.choose_emotion;
        }
    }

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

    private int getTimeOrder(String timeOfDay) {
        switch (timeOfDay.toLowerCase()) {
            case "manhã":
                return 1;
            case "tarde":
                return 2;
            case "noite":
                return 3;
            default:
                return 4;
        }
    }

    private String getBreathKey(String timeOfDay) {
        switch (timeOfDay.toLowerCase()) {
            case "manhã":
                return "morningBreaths";
            case "tarde":
                return "afternoonBreaths";
            case "noite":
                return "nightBreaths";
            default:
                return "breaths_unknown";
        }
    }

    private String getMeditationKey(String timeOfDay) {
        switch (timeOfDay.toLowerCase()) {
            case "manhã":
                return "morningMeditations";
            case "tarde":
                return "afternoonMeditations";
            case "noite":
                return "nightMeditations";
            default:
                return "meditations_unknown";
        }
    }
}
