package com.example.checklist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private CheckBox termsCheckBox;
    private RelativeLayout registerButton;
    private ImageView buttonVoltarRegister;
    private TextView termsOfUseTextView;  // Novo TextView para os termos de uso

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
        buttonVoltarRegister = findViewById(R.id.buttonVoltarRegister);
        termsOfUseTextView = findViewById(R.id.termsOfUseTextView);  // Inicializar o novo TextView

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

        // Configurar o clique do botão de voltar
        buttonVoltarRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Voltar para a tela de login
                finish();
            }
        });

        // Configurar o clique do TextView dos termos de uso
        termsOfUseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mostrar os termos de uso em um dialog
                showTermsOfUseDialog();
            }
        });
    }

    private void showTermsOfUseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Terms of Use");
        builder.setMessage("1. Aceitação dos Termos\n\n" +
                "Ao criar uma conta e utilizar o aplicativo TaskBro, você concorda em cumprir e estar legalmente vinculado aos seguintes termos e condições a seguir. Estes Termos de Uso aplicam-se a todos os visitantes, usuários e outros que acessam ou utilizam o aplicativo.\n\n" +
                "2. Uso do Aplicativo\n\n" +
                "Você concorda em usar o aplicativo apenas para fins legais e de acordo com os Termos de Uso. Você é responsável por garantir que seu uso do aplicativo esteja em conformidade com todas as leis, regras e regulamentos aplicáveis.\n\n" +
                "3. Registro de Conta\n\n" +
                "Para acessar certas funcionalidades do aplicativo, você pode ser solicitado a criar uma conta. Você deve fornecer informações precisas, completas e atualizadas durante o registro e manter a confidencialidade da sua conta e senha. Você é responsável por todas as atividades que ocorrem sob sua conta.\n\n" +
                "4. Conteúdo do Usuário\n\n" +
                "Você é o único responsável pelo conteúdo que você carrega, publica ou exibe no aplicativo. Você não deve postar conteúdo que seja ilegal, ofensivo, prejudicial ou viole os direitos de terceiros.\n\n" +
                "5. Propriedade Intelectual\n\n" +
                "O aplicativo e seu conteúdo original, recursos e funcionalidades são e continuarão sendo propriedade exclusiva do aplicativo TaskBro e seus licenciadores. O aplicativo está protegido por direitos autorais, marcas registradas e outras leis de propriedade intelectual.\n\n" +
                "6. Privacidade\n\n" +
                "Sua privacidade é importante para nós. Nossa Política de Privacidade explica como coletamos, usamos e protegemos suas informações pessoais quando você usa o aplicativo. Recomendamos que você leia nossa Política de Privacidade cuidadosamente.\n\n" +
                "7. Rescisão\n\n" +
                "Podemos encerrar ou suspender sua conta imediatamente, sem aviso prévio ou responsabilidade, por qualquer motivo, incluindo, sem limitação, se você violar os Termos de Uso. Após a rescisão, seu direito de usar o aplicativo cessará imediatamente.\n\n" +
                "8. Limitação de Responsabilidade\n\n" +
                "Na máxima extensão permitida pela lei aplicável, o aplicativo TaskBro não será responsável por quaisquer danos indiretos, incidentais, especiais, consequentes ou punitivos, incluindo, sem limitação, perda de lucros, dados, uso, fundo de comércio ou outras perdas intangíveis, resultantes de (i) seu uso ou incapacidade de usar o aplicativo; (ii) qualquer acesso não autorizado ou uso dos nossos servidores e/ou qualquer informação pessoal armazenada neles.\n\n" +
                "9. Alterações nos Termos\n\n" +
                "Reservamo-nos o direito de modificar ou substituir estes Termos de Uso a qualquer momento. Se uma revisão for material, tentaremos fornecer um aviso com pelo menos 30 dias de antecedência antes que quaisquer novos termos entrem em vigor. O que constitui uma mudança material será determinado a nosso exclusivo critério.\n\n" +
                "10. Contato\n\n" +
                "Se você tiver alguma dúvida sobre estes Termos de Uso, entre em contato conosco.");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
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
