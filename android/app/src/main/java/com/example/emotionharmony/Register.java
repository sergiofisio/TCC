/**
 * Tela de registro de novos usuários.
 * Responsável por:
 * - Coletar dados do formulário
 * - Validar campos (nome, email, cpf, telefone, etc.)
 * - Aplicar máscaras aos campos de telefone e CPF
 * - Enviar dados para o backend
 * - Exibir mensagens de erro ou sucesso
 */
package com.example.emotionharmony;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.example.emotionharmony.utils.ShowPassword;
import com.example.emotionharmony.utils.Validator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Register extends AppCompatActivity {

    private CustomToast customToast;
    private EditText nome, email, cpf, telefone, emergencia, senha;
    private TextView msgEmergency;

    /**
     * Método principal da tela de registro
     */
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

    /**
     * Inicializa os componentes de interface e listeners
     */
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
        CheckBox cbShowPass = findViewById(R.id.cbShowPass);

        applyInputMasks();

        btnRegister.setOnClickListener(v -> registerUser());
        txtLogin.setOnClickListener(v -> NavigationHelper.navigateTo(Register.this, Home.class, false));
        cbShowPass.setOnCheckedChangeListener((buttonView, isChecked) -> ShowPassword.ChangeShowPassword(senha, isChecked));
    }

    /**
     * Aplica máscaras de formatação aos campos de entrada
     */
    private void applyInputMasks() {
        cpf.addTextChangedListener(MaskUtil.applyMask(cpf, "###.###.###-##"));
        telefone.addTextChangedListener(MaskUtil.applyMask(telefone, "(##)#####-####"));
        emergencia.addTextChangedListener(MaskUtil.applyMask(emergencia, "(##)#####-####"));

        emergencia.setOnFocusChangeListener((v, hasFocus) ->
                msgEmergency.setVisibility(hasFocus ? View.VISIBLE : View.GONE));
    }

    /**
     * Valida os campos e inicia o processo de registro
     */
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

    /**
     * Monta o JSON com os dados do usuário
     */
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

    /**
     * Cria objeto de telefone com tipo e número
     */
    private JSONObject createPhoneObject(String type, String phone) throws JSONException {
        return new JSONObject().put("tipo", type).put("telefone", phone);
    }

    /**
     * Envia os dados para o backend e trata a resposta
     */
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

    /**
     * Exibe uma mensagem de erro formatada com o toast customizado
     */
    private void showErrorMessage(String error) {
        customToast.show(error, Toast.LENGTH_LONG, "#FF0000", "error");
    }
}