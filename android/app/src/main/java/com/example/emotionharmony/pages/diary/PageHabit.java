package com.example.emotionharmony.pages.diary;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.emotionharmony.R;
import com.example.emotionharmony.pages.Page_Diary;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PageHabit extends AppCompatActivity {

    private Calendar selectedDateTime = Calendar.getInstance();
    private EditText editTextDateTime;
    private EditText editTextHabitName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_habits);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );

        ImageView btnBack12 = findViewById(R.id.btnBack11);
        btnBack12.setOnClickListener(v -> {
            startActivity(new Intent(PageHabit.this, Page_Diary.class));
            finish();
        });

        editTextDateTime = findViewById(R.id.editTextDateTime);
        editTextHabitName = findViewById(R.id.txtPeso);

        // Definindo tipo de entrada que permite acentos e cedilha
        editTextHabitName.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        editTextHabitName.setHint("Digite o nome do hábito");
        editTextHabitName.setSingleLine(true);

        editTextDateTime.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            new DatePickerDialog(this, (view, y, m, d) -> {
                new TimePickerDialog(this, (view1, h, min) -> {
                    selectedDateTime.set(y, m, d, h, min);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                    String formattedDateTime = sdf.format(selectedDateTime.getTime());
                    editTextDateTime.setText(formattedDateTime);
                }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true).show();
            }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)).show();
        });

        Button btnPronto = findViewById(R.id.btnPronto);
        btnPronto.setOnClickListener(v -> {
            String habitName = editTextHabitName.getText().toString().trim();
            String habitDateStr = editTextDateTime.getText().toString().trim();

            if (!habitName.isEmpty() && !habitDateStr.isEmpty()) {
                Intent intent = new Intent(PageHabit.this, Page_Diary.class);
                intent.putExtra("habitName", habitName);
                intent.putExtra("habitDate", selectedDateTime.getTimeInMillis());
                startActivity(intent);
                finish();
            }
        });
    }
}
