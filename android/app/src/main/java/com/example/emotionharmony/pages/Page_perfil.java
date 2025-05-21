package com.example.emotionharmony.pages;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.emotionharmony.CustomToast;
import com.example.emotionharmony.R;
import com.example.emotionharmony.classes.MaskUtil;
import com.example.emotionharmony.components.BottomMenuView;
import com.example.emotionharmony.databinding.ActivityPagePerfilBinding;
import com.example.emotionharmony.utils.NavigationHelper;
import com.example.emotionharmony.utils.ServerConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Tela de Perfil do Usuário onde é possível visualizar e editar informações pessoais como
 * nome, email, CPF, telefones e senha.
 */
public class Page_perfil extends AppCompatActivity {

    private CustomToast toast;
    private EditText nome, email, cpf, telefone, emergencia, senha;
    private String token;
    private String originalNome, originalEmail, originalCpf, originalTelefone, originalEmergencia;
    private int idTelefone, idEmergencia;

    /**
     * Método chamado na criação da atividade. Inicializa UI, aplica efeitos visuais e carrega dados do usuário.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );

        ActivityPagePerfilBinding binding = ActivityPagePerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomMenuView bottomMenu = findViewById(R.id.bottomMenu);
        bottomMenu.setActivityContext(this);

        initUI();
        loadUserData();
    }

    /**
     * Inicializa os elementos da interface e listeners.
     */
    private void initUI() {
        toast = new CustomToast(this);
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        token = preferences.getString("authToken", null);

        nome = findViewById(R.id.txtNomePerfil);
        email = findViewById(R.id.txtEmailPerfil);
        cpf = findViewById(R.id.txtCpfPerfil);
        telefone = findViewById(R.id.txtCelularPerfil);
        emergencia = findViewById(R.id.txtEmergenciaPerfil);
        senha = findViewById(R.id.txtSenhaPerfil);
        Button btnEdit = findViewById(R.id.btnEdit);

        applyInputMasks();
        btnEdit.setOnClickListener(v -> updateUser());
    }

    /**
     * Aplica máscaras nos campos de CPF e telefones.
     */
    private void applyInputMasks() {
        cpf.addTextChangedListener(MaskUtil.applyMask(cpf, "###.###.###-##"));
        telefone.addTextChangedListener(MaskUtil.applyMask(telefone, "(##)#####-####"));
        emergencia.addTextChangedListener(MaskUtil.applyMask(emergencia, "(##)#####-####"));
    }

    /**
     * Carrega os dados do usuário autenticado e preenche os campos.
     */
    private void loadUserData() {
        if (token == null) {
            showErrorMessage("Erro: Usuário não autenticado!");
            return;
        }

        ServerConnection.getRequestWithAuth("/user", token, new ServerConnection.ServerCallback() {
            @Override
            public void onSuccess(String response) {
                runOnUiThread(() -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);

                        originalNome = jsonResponse.optString("name_user", "");
                        originalEmail = jsonResponse.optString("email_user", "");
                        originalCpf = jsonResponse.optString("cpf_user", "");

                        nome.setText(originalNome);
                        email.setText(originalEmail);
                        cpf.setText(originalCpf);

                        JSONArray phonesArray = jsonResponse.optJSONArray("phone_user");
                        if (phonesArray != null) {
                            for (int i = 0; i < phonesArray.length(); i++) {
                                JSONObject phoneObject = phonesArray.getJSONObject(i);
                                String type = phoneObject.optString("type_phone", "");
                                String phoneNumber = formatPhoneNumber(phoneObject);
                                int phoneId = phoneObject.optInt("id_phone", -1);

                                if (type.equals("celular")) {
                                    originalTelefone = phoneNumber;
                                    telefone.setText(phoneNumber);
                                    idTelefone = phoneId;
                                } else if (type.equals("emergencia")) {
                                    originalEmergencia = phoneNumber;
                                    emergencia.setText(phoneNumber);
                                    idEmergencia = phoneId;
                                }
                            }
                        }

                    } catch (JSONException e) {
                        showErrorMessage("Erro ao processar os dados do usuário.");
                        Log.e("Page_perfil", "❌ Erro JSON: " + e.getMessage());
                    }
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> showErrorMessage(error));
            }
        });
    }

    /**
     * Formata o telefone recebido do servidor.
     */
    private String formatPhoneNumber(JSONObject phoneObject) {
        int areaCode = phoneObject.optInt("area_code_phone", 0);
        int number = phoneObject.optInt("phone_number", 0);

        if (areaCode == 0 || number == 0) return "";
        return String.format("(%d) %d", areaCode, number);
    }

    /**
     * Compara os dados preenchidos com os originais e envia atualização se houver mudanças.
     */
    private void updateUser() {
        try {
            JSONObject userData = new JSONObject();
            boolean hasChanges = false;

            if (!nome.getText().toString().trim().equals(originalNome)) {
                userData.put("nome", nome.getText().toString().trim());
                hasChanges = true;
            }

            if (!email.getText().toString().trim().equals(originalEmail)) {
                userData.put("email", email.getText().toString().trim());
                hasChanges = true;
            }

            if (!cpf.getText().toString().trim().equals(originalCpf)) {
                userData.put("cpf", cpf.getText().toString().trim());
                hasChanges = true;
            }

            String newTelefone = telefone.getText().toString().trim();
            String newEmergencia = emergencia.getText().toString().trim();
            JSONArray phoneArray = new JSONArray();

            if (!newTelefone.equals(originalTelefone)) {
                phoneArray.put(createPhoneObject(idTelefone, "celular", newTelefone));
                hasChanges = true;
            }

            if (!newEmergencia.equals(originalEmergencia)) {
                phoneArray.put(createPhoneObject(idEmergencia, "emergencia", newEmergencia));
                hasChanges = true;
            }

            if (phoneArray.length() > 0) {
                userData.put("telefones", phoneArray);
            }

            String newPassword = senha.getText().toString().trim();
            if (!newPassword.isEmpty()) {
                userData.put("senha", newPassword);
                hasChanges = true;
            }

            if (!hasChanges) {
                showErrorMessage("Nenhuma alteração foi feita.");
                return;
            }

            sendEditRequest(userData);

        } catch (JSONException e) {
            showErrorMessage("Erro ao processar os dados do usuário.");
            Log.e("Page_perfil", "❌ Erro JSON: " + e.getMessage());
        }
    }

    /**
     * Cria um objeto JSON para telefone a ser enviado.
     */
    private JSONObject createPhoneObject(int id, String type, String phone) throws JSONException {
        JSONObject phoneObj = new JSONObject();
        phoneObj.put("id_phone", id);
        phoneObj.put("type_phone", type);
        phoneObj.put("telefone", phone);
        return phoneObj;
    }

    /**
     * Envia requisição PATCH com os dados atualizados do usuário.
     */
    private void sendEditRequest(JSONObject userData) {
        try {
            if (token == null) throw new JSONException("Erro: Usuário não autenticado!");

            ServerConnection.patchRequestWithAuth("/user/update", token, userData, new ServerConnection.ServerCallback() {
                @Override
                public void onSuccess(String response) {
                    runOnUiThread(() -> {
                        toast.show("✅ Informações atualizadas com sucesso!", Toast.LENGTH_LONG, "#11273D", "success");
                    });
                    NavigationHelper.navigateTo(Page_perfil.this, Page_Exercicies.class, true);
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> showErrorMessage(error));
                }
            });
        } catch (JSONException e) {
            toast.show("Erro ao processar os dados!", Toast.LENGTH_LONG, "#FF0000", "error");
            Log.e("Page_perfil", "Erro JSON: " + e.getMessage());
        }
    }

    /**
     * Exibe mensagens de erro no Toast personalizado.
     */
    private void showErrorMessage(String error) {
        toast.show(error, Toast.LENGTH_LONG, "#FF0000", "error");
    }
}
