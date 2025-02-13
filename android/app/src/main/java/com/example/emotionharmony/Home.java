package com.example.emotionharmony;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.emotionharmony.pages.After_Login;
import com.example.emotionharmony.utils.InputField;
import com.example.emotionharmony.utils.ServerConnection;
import com.example.emotionharmony.utils.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    private CustomToast customToast;
    private EditText txtEmail, txtSenha;
    private Button btnLogin;
    private TextView lblCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            v.setPadding(insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom);
            return insets;
        });

        initUI();
    }

    private void initUI() {
        customToast = new CustomToast(this);

        txtEmail = findViewById(R.id.txtEmail);
        txtSenha = findViewById(R.id.txtSenha);
        btnLogin = findViewById(R.id.btnLogin);
        lblCadastro = findViewById(R.id.lblCadastro);

        btnLogin.setOnClickListener(v -> handleLogin());
        lblCadastro.setOnClickListener(v -> navigateToRegister());
    }

    private void handleLogin() {
        try {
            String email = txtEmail.getText().toString().trim();
            String senha = txtSenha.getText().toString().trim();

            List<InputField> fields = new ArrayList<>();
            fields.add(new InputField("Email", email));
            fields.add(new InputField("Senha", senha));

            Validator.validateFields(fields);

            JSONObject loginData = new JSONObject();
            loginData.put("email", email);
            loginData.put("senha", senha);

            sendLoginRequest(loginData);

        } catch (Exception e) {
            showErrorMessage(e.getMessage());
        }
    }

    private void sendLoginRequest(JSONObject loginData) {
        ServerConnection.postRequest("/login", loginData, new ServerConnection.ServerCallback() {
            @Override
            public void onSuccess(String response) {
                runOnUiThread(() -> processLoginResponse(response));
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> showErrorMessage(error));
            }
        });
    }

    private void processLoginResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            String token = jsonResponse.optString("token", "");

            if (!token.isEmpty()) {
                saveAuthToken(token);
                customToast.show("✅ Login realizado com sucesso!", Toast.LENGTH_LONG, "#11273D", "success");
                navigateToAfterLogin();
            } else {
                showErrorMessage("❌ Erro ao autenticar.");
            }
        } catch (JSONException e) {
            showErrorMessage("❌ Erro ao processar resposta do servidor.");
        }
    }

    private void saveAuthToken(String token) {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("authToken", token);
        editor.apply();
    }

    private void showErrorMessage(String error) {
        Log.e("LoginError", "❌ Erro no login: " + error);

        customToast.show(error, Toast.LENGTH_LONG, "#FF0000", "error");
    }

    private void navigateToRegister() {
        startActivity(new Intent(Home.this, Register.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    private void navigateToAfterLogin() {
        startActivity(new Intent(Home.this, After_Login.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
}
