package com.example.emotionharmony.pages.breath;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.emotionharmony.R;

public class Page_End extends AppCompatActivity {

    private TextView txtMesma, txtMelhor;
    private Switch SwFeel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_page_end);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Init();
    }

    private void Init(){
        txtMelhor=findViewById(R.id.txtMelhor);
        txtMesma=findViewById(R.id.txtMesma);
        SwFeel=findViewById(R.id.SwFeel);

        SwFeel.setOnCheckedChangeListener((buttonView, isChecked)->{
            if(isChecked){
                txtMelhor.setTextColor(Color.parseColor("0026FF"));
                txtMesma.setTextColor(Color.parseColor("#807F7F"));
            }else{
                txtMesma.setTextColor(Color.parseColor("F44336"));
                txtMelhor.setTextColor(Color.parseColor("807F7F"));
            }
        });
    }

}