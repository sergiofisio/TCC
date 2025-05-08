/**
 * Tela inicial (Home/Login) da aplicação Emotion Harmony.
 * Esta Activity permite ao usuário inserir seu e-mail e senha para autenticação.
 * Também oferece acesso ao cadastro, recuperação de senha e exibição de mensagens com CustomToast.
 */

package com.example.emotionharmony;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.emotionharmony.pages.Page_Exercicies;
import com.example.emotionharmony.utils.InputField;
import com.example.emotionharmony.utils.NavigationHelper;
import com.example.emotionharmony.utils.ServerConnection;
import com.example.emotionharmony.utils.ShowPassword;
import com.example.emotionharmony.utils.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    private CustomToast customToast;
    private EditText txtEmail, txtSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // Ajusta o layout para lidar com as barras do sistema (topo, navegação)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            v.setPadding(
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            );
            return insets;
        });

        // Inicializa todos os componentes e eventos da tela
        initUI(this);
    }

    /**
     * Inicializa os componentes de UI, eventos de clique e funcionalidades auxiliares como exibição de senha.
     */
    private void initUI(Activity activity) {
        customToast = new CustomToast(this);

        txtEmail = findViewById(R.id.txtEmail);
        txtSenha = findViewById(R.id.txtSenha);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView lblCadastro = findViewById(R.id.lblCadastro);
        CheckBox cbShowPass = findViewById(R.id.cbShowPassLogin);
        TextView TxtForgot = findViewById(R.id.TxtForgot);

        // Navegação para a tela de redefinir senha
        TxtForgot.setOnClickListener(v -> NavigationHelper.navigateTo(activity, lost_password.class, true));

        // Evento de login
        btnLogin.setOnClickListener(v -> handleLogin(activity));

        // Navegação para cadastro
        lblCadastro.setOnClickListener(v -> NavigationHelper.navigateTo(activity, Register.class, true));

        // Alternar visibilidade da senha
        cbShowPass.setOnCheckedChangeListener((buttonView, isChecked) -> ShowPassword.ChangeShowPassword(txtSenha, isChecked));
    }

    /**
     * Coleta dados do formulário e valida os campos.
     * Se válidos, envia para o servidor.
     */
    private void handleLogin(Activity activity) {
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

            sendLoginRequest(loginData, activity);
        } catch (Exception e) {
            showErrorMessage(e.getMessage());
        }
    }

    /**
     * Envia os dados de login para o endpoint /auth/login e trata a resposta.
     */
    private void sendLoginRequest(JSONObject loginData, Activity activity) {
        ServerConnection.postRequest("/auth/login", loginData, new ServerConnection.ServerCallback() {
            @Override
            public void onSuccess(String response) {
                runOnUiThread(() -> processLoginResponse(response, activity));
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> showErrorMessage(error));
            }
        });
    }

    /**
     * Processa a resposta de sucesso do login.
     * Armazena o token de autenticação e navega para a tela principal.
     */
    private void processLoginResponse(String response, Activity activity) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            String token = jsonResponse.optString("token", "");

            if (!token.isEmpty()) {
                saveAuthToken(token);
                customToast.show("✅ Login realizado com sucesso!", Toast.LENGTH_LONG, "#11273D", "success");
                NavigationHelper.navigateTo(activity, Page_Exercicies.class, true);
            } else {
                showErrorMessage("❌ Erro ao autenticar.");
            }
        } catch (JSONException e) {
            showErrorMessage("❌ Erro ao processar resposta do servidor.");
        }
    }

    /**
     * Salva o token de autenticação nas preferências do dispositivo.
     */
    private void saveAuthToken(String token) {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("authToken", token);
        editor.apply();
    }

    /**
     * Exibe mensagens de erro no log e via Toast personalizado.
     */
    private void showErrorMessage(String error) {
        Log.e("LoginError", "❌ Erro no login: " + error);
        customToast.show(error, Toast.LENGTH_LONG, "#FF0000", "error");
    }
}
