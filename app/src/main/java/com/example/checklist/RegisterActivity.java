package com.example.checklist;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private CheckBox termsCheckBox;
    private RelativeLayout registerButton;

    private ChecklistDao checklistDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializar as views
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        termsCheckBox = findViewById(R.id.termsOfUseCheckBox);
        registerButton = findViewById(R.id.registerButton);

        // Inicializar o ChecklistDao
        checklistDao = ChecklistDatabase.getInstance(this).checklistDao();

        // Configurar o clique do botão de registro
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obter os dados inseridos pelo usuário
                String username = usernameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();
                boolean termsAccepted = termsCheckBox.isChecked();

                // Verificar se os campos estão vazios
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) ||
                        TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Por favor, preencher todos os campos.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Verificar se a senha e a confirmação de senha correspondem
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "As senhas não se combinam", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Verificar se o usuário aceitou os termos de uso
                if (!termsAccepted) {
                    Toast.makeText(RegisterActivity.this, "Por favor, aceite os Termos de Uso", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Executar a operação de registro em uma thread separada
                new RegisterAsyncTask().execute(username, email, password);
            }
        });
    }

    private class RegisterAsyncTask extends AsyncTask<String, Void, Long> {
        @Override
        protected Long doInBackground(String... params) {
            String username = params[0];
            String email = params[1];
            String password = params[2];

            // Verificar se o usuário já existe no banco de dados
            int userCountByUsername = checklistDao.getUserCountByUsername(username);
            if (userCountByUsername > 0) {
                showToastOnUiThread("Nome de usuário já existe");
                return null;
            }

            // Verificar se o email já existe no banco de dados
            int userCountByEmail = checklistDao.getUserCountByEmail(email);
            if (userCountByEmail > 0) {
                showToastOnUiThread("O e-mail já existe");
                return null;
            }

            // Criar um novo objeto User com os dados inseridos pelo usuário
            User newUser = new User(username, email, password);

            // Inserir o novo usuário no banco de dados usando o DAO correspondente
            long userId = checklistDao.insertUser(newUser);

            // Retornar o ID do novo usuário
            return userId;
        }
        // Método para obter o ID do usuário atual
        private int getUserId(String username, String password) {
            return checklistDao.getUserIdByUsernameAndPassword(username, password);
        }

        @Override
        protected void onPostExecute(Long userId) {
            if (userId != null && userId != -1L) {
                // Atualizar o ID do usuário com o valor retornado pelo DAO
                User newUser = new User(usernameEditText.getText().toString().trim(),
                        emailEditText.getText().toString().trim(),
                        passwordEditText.getText().toString());
                newUser.setId(userId.intValue());

                // Exibir uma mensagem de sucesso
                showToastOnUiThread("Registro bem-sucedido");

                // Redirecionar o usuário para a tela de login
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                // Encerrar esta atividade para evitar que o usuário volte para a tela de registro ao pressionar o botão Voltar
                finish();

                // Adicionar logs para verificar se os dados estão sendo inseridos corretamente no banco de dados
                Log.d("RegisterActivity", "Novo usuário registrado:");
                Log.d("RegisterActivity", "ID do usuário: " + newUser.getId());
                Log.d("RegisterActivity", "Nome de usuário: " + newUser.getUsername());
                Log.d("RegisterActivity", "Email: " + newUser.getEmail());
                Log.d("RegisterActivity", "Senha: " + newUser.getPassword());
            }
        }

        private void showToastOnUiThread(final String message) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
