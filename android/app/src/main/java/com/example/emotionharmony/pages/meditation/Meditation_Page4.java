package com.example.emotionharmony.pages.meditation;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.emotionharmony.CustomToast;
import com.example.emotionharmony.R;
import com.example.emotionharmony.classes.Questions_Meditation;
import com.example.emotionharmony.utils.NavigationHelper;
import com.example.emotionharmony.utils.TTSHelper;

public class Meditation_Page4 extends AppCompatActivity {

    private TextView txtRuim, txtBoa, txtSpeech;
     @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch SwSituation;
    private TTSHelper ttsHelper;
    private Questions_Meditation questionsMeditation;
    private CustomToast toast;
    private ImageView btnBack, btnNext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meditation_page4);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Init();

        btnNext = findViewById(R.id.btnNext4);
        btnBack = findViewById(R.id.btnBack4);
        txtSpeech = findViewById(R.id.txtSpeech);

        ttsHelper = TTSHelper.getInstance(this);

        new Handler().postDelayed(()-> ttsHelper.speakText(txtSpeech.getText().toString()), 1500);

        btnNext.setOnClickListener(v -> {
            try{
                String typeOf = SwSituation.isChecked()?"boa":"ruim";

                questionsMeditation.setTypeSituation(typeOf);

                NavigationHelper.navigateTo(Meditation_Page4.this, Meditation_Page5.class, true);

            }catch (Exception e){
                toast.show(e.getMessage(), Toast.LENGTH_LONG, "#FF0000", "error");
            }
        });

        btnBack.setOnClickListener(v -> NavigationHelper.navigateTo(Meditation_Page4.this, Meditation_Page3.class, false));
    }

    private void Init(){
        toast = new CustomToast(this);
        txtBoa=findViewById(R.id.txtGood);
        txtRuim=findViewById(R.id.txtBad);
        SwSituation=findViewById(R.id.SwSituation);
        btnBack=findViewById(R.id.btnBack4);
        btnNext=findViewById(R.id.btnNext5);

        questionsMeditation = Questions_Meditation.getInstance();

        if(questionsMeditation.getTypeSituation()!=null){
            SwSituation.setChecked(true);
        }

        txtSpeech = findViewById(R.id.txtSpeech3);
        ttsHelper = TTSHelper.getInstance(this);

        new Handler().postDelayed(()-> ttsHelper.speakText(txtSpeech.getText().toString()), 1500);

        SwSituation.setOnCheckedChangeListener((buttonView, isChecked)->{
            if(isChecked){
                txtBoa.setTextColor(Color.parseColor("#0026FF"));
                txtRuim.setTextColor(Color.parseColor("#807F7F"));
            }else{
                txtRuim.setTextColor(Color.parseColor("#F44336"));
                txtBoa.setTextColor(Color.parseColor("#807F7F"));
            }
        });
    }
}