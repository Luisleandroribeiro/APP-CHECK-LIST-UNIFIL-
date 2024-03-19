package com.example.checklist;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private RelativeLayout loginButtonLayout;
    private RelativeLayout registerButtonLayout;
    private ChecklistDao checklistDao; // Adicionado o ChecklistDao

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar as views
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButtonLayout = findViewById(R.id.loginButtonLayout);
        registerButtonLayout = findViewById(R.id.registerButtonLayout);

        // Inicializar o ChecklistDao
        checklistDao = ChecklistDatabase.getInstance(this).checklistDao(); // Inicialização do ChecklistDao

        // Configurar o clique do botão de registro
        registerButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Iniciar a atividade de registro quando o botão for clicado
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // Configurar o clique do botão de login
        loginButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obter o nome de usuário e senha digitados
                final String username = usernameEditText.getText().toString();
                final String password = passwordEditText.getText().toString();

                // Verificar se o nome de usuário e senha estão vazios
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                } else {
                    // Executar a autenticação em uma thread separada
                    new AsyncTask<Void, Void, Boolean>() {
                        @Override
                        protected Boolean doInBackground(Void... voids) {
                            // Autenticar o usuário em uma thread separada
                            return authenticateUser(username, password);
                        }

                        @Override
                        protected void onPostExecute(Boolean isAuthenticated) {
                            super.onPostExecute(isAuthenticated);
                            if (isAuthenticated) {
                                // Usuário autenticado com sucesso, iniciar a próxima atividade
                                Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                                // Inicia a atividade de ChecklistMain
                                Intent intent = new Intent(LoginActivity.this, ChecklistMain.class);
                                startActivity(intent);
                                finish(); // Finaliza a LoginActivity após o login bem-sucedido
                            } else {
                                // Nome de usuário ou senha incorretos, exibir mensagem de erro
                                Toast.makeText(LoginActivity.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }.execute();
                }
            }
        });
    }

    // Método para autenticar o usuário (substitua este método com sua própria lógica de autenticação)
    private boolean authenticateUser(String username, String password) {
        // Consultar o banco de dados para verificar se o usuário existe e a senha está correta
        int userCount = checklistDao.getUserCountByUsernameAndPassword(username, password);
        return userCount > 0;
    }
}
