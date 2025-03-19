package com.example.emotionharmony;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.emotionharmony.classes.MaskUtil;
import com.example.emotionharmony.utils.InputField;
import com.example.emotionharmony.utils.NavigationHelper;
import com.example.emotionharmony.utils.ServerConnection;
import com.example.emotionharmony.utils.Validator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Register extends AppCompatActivity {

    private CustomToast customToast;
    private EditText nome, email, cpf, telefone, emergencia, senha;
    private TextView msgEmergency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

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

        nome = findViewById(R.id.txtName);
        email = findViewById(R.id.txtEmail);
        cpf = findViewById(R.id.txtCPF);
        telefone = findViewById(R.id.txtPhone);
        emergencia = findViewById(R.id.txtEmergency);
        senha = findViewById(R.id.txttSenhaRegistro);
        msgEmergency = findViewById(R.id.txtMsgEmergency);
        Button btnRegister = findViewById(R.id.btnRegister);
        TextView txtLogin = findViewById(R.id.txtLogin);

        applyInputMasks();

        btnRegister.setOnClickListener(v -> registerUser());
        txtLogin.setOnClickListener(v -> NavigationHelper.navigateTo(Register.this, Home.class, false));
    }

    private void applyInputMasks() {
        cpf.addTextChangedListener(MaskUtil.applyMask(cpf, "###.###.###-##"));
        telefone.addTextChangedListener(MaskUtil.applyMask(telefone, "(##)#####-####"));
        emergencia.addTextChangedListener(MaskUtil.applyMask(emergencia, "(##)#####-####"));

        emergencia.setOnFocusChangeListener((v, hasFocus) ->
                msgEmergency.setVisibility(hasFocus ? View.VISIBLE : View.GONE));
    }

    private void registerUser() {
        msgEmergency.setVisibility(View.GONE);

        try {
            List<InputField> fields = List.of(
                    new InputField("Nome", nome.getText().toString()),
                    new InputField("Email", email.getText().toString()),
                    new InputField("CPF", cpf.getText().toString()),
                    new InputField("Telefone", telefone.getText().toString()),
                    new InputField("Contato de Emergência", emergencia.getText().toString()),
                    new InputField("Senha", senha.getText().toString())
            );

            Validator.validateFields(fields);

            String userPhone = telefone.getText().toString().trim();
            String emergencyPhone = emergencia.getText().toString().trim();

            if (userPhone.equals(emergencyPhone)) {
                showErrorMessage("O telefone de emergência deve ser diferente do telefone do usuário.");
                return;
            }

            JSONObject userData = buildUserData(userPhone, emergencyPhone);
            sendRegisterRequest(userData);

        } catch (Exception e) {
            showErrorMessage(e.getMessage());
        }
    }

    private JSONObject buildUserData(String userPhone, String emergencyPhone) throws JSONException {
        return new JSONObject()
                .put("nome", nome.getText().toString().trim())
                .put("email", email.getText().toString().trim())
                .put("cpf", cpf.getText().toString().trim())
                .put("senha", senha.getText().toString().trim())
                .put("telefones", new JSONArray()
                        .put(createPhoneObject("celular", userPhone))
                        .put(createPhoneObject("emergencia", emergencyPhone)));
    }

    private JSONObject createPhoneObject(String type, String phone) throws JSONException {
        return new JSONObject().put("tipo", type).put("telefone", phone);
    }

    private void sendRegisterRequest(JSONObject userData) {
        ServerConnection.postRequest("/register", userData, new ServerConnection.ServerCallback() {
            @Override
            public void onSuccess(String response) {
                runOnUiThread(() -> {
                    customToast.show("✅ Usuário registrado com sucesso! Redirecionando...",
                            Toast.LENGTH_LONG, "#11273D", "success");
                    NavigationHelper.navigateTo(Register.this, Home.class, true);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> showErrorMessage(error));
            }
        });
    }

    private void showErrorMessage(String error) {
        customToast.show(error, Toast.LENGTH_LONG, "#FF0000", "error");
    }
}
