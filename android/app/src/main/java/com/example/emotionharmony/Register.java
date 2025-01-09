package com.example.emotionharmony;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.emotionharmony.classes.MaskUtil;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView txtLogin=findViewById(R.id.txtLogin), msgEmergency=findViewById(R.id.txtMsgEmergency);

        EditText mome=findViewById(R.id.txtName), email=findViewById(R.id.txtEmail), cpf = findViewById(R.id.txtCPF), telefone = findViewById(R.id.txtPhone), emergencia=findViewById(R.id.txtEmergency);

        cpf.addTextChangedListener(MaskUtil.applyMask(cpf, "###.###.###-##"));
        telefone.addTextChangedListener(MaskUtil.applyMask(telefone, "(##)#####-####"));
        emergencia.addTextChangedListener(MaskUtil.applyMask(emergencia, "(##)#####-####"));

        emergencia.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
               msgEmergency.setVisibility(hasFocus?View.VISIBLE:View.GONE);
            }
        });

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Home.class);
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                finish();
            }
        });
    }
}