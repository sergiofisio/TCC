package com.example.emotionharmony;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.emotionharmony.utils.NavigationHelper;
import com.example.emotionharmony.utils.ServerConnection;

import org.json.JSONObject;

public class lost_password extends AppCompatActivity {

    private CustomToast customToast;
    private EditText txtEmailLost;
    private TextView TxtLRemember;
    private Button btnLost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lost_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initUI();
    }

    private void initUI() {
        customToast = new CustomToast(this);

        txtEmailLost = findViewById(R.id.txtEmailLost);
        btnLost = findViewById(R.id.btnLost);
        TxtLRemember = findViewById(R.id.TxtLRemember);

        btnLost.setOnClickListener(v -> handleLostPassword());
        TxtLRemember.setOnClickListener(v -> NavigationHelper.navigateTo(this, Home.class, false));
    }

    private void handleLostPassword(){
        try{
            String email = txtEmailLost.getText().toString().trim();

            if(email.isEmpty()) throw new Exception("Insira o email");

            JSONObject body = new JSONObject();
            body.put("email", email);

            ServerConnection.postRequest("/recovery", body, new ServerConnection.ServerCallback() {
                @Override
                public void onSuccess(String response) {
                    Log.i("Recovery", "Resposta do servidor: " + response);

                }

                @Override
                public void onError(String error) {
                    Log.e("RecoveryError", error);
                }
            });
            showFinalMessageAndNavigate();
        }catch (Exception e){
            customToast.show(e.getMessage(), Toast.LENGTH_LONG, "#FF0000", "error");
            Log.e("Recovery", e.getMessage());
        }
    }

    private void showFinalMessageAndNavigate() {
        runOnUiThread(() -> {
            customToast.show(
                    "Se seu email estiver correto, você receberá um link... VERIFIQUE SEU EMAIL",
                    10000,
                    "#11273D",
                    "success"
            );
            NavigationHelper.navigateTo(this, Home.class, true);
        });
    }
}
