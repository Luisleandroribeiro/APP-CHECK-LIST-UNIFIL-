package com.example.checklist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

public class PerfilActivity extends AppCompatActivity {

    private int userId;
    private TextView nomeUsuarioTextView;
    private ChecklistDao checklistDao;
    private RelativeLayout botaoChangePassword;
    private RelativeLayout botaoHelpSupport;
    private RelativeLayout botaoAboutUs;
    private RelativeLayout botaoLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        // Inicializando o DAO
        checklistDao = ChecklistDatabase.getInstance(this).checklistDao();

        // Obtendo o ID do usuário passado pela tela anterior
        userId = getIntent().getIntExtra("USER_ID", -1);

        // Referenciando elementos do layout
        nomeUsuarioTextView = findViewById(R.id.nomeususario); // Inicializando a referência
        TextView idUsuarioTextView = findViewById(R.id.id);
        ImageView botaoVoltarHome = findViewById(R.id.buttonVoltarHome);
        botaoChangePassword = findViewById(R.id.btnChangePassword);
        botaoHelpSupport = findViewById(R.id.btnHelpSupport);
        botaoAboutUs = findViewById(R.id.btnAboutUs);
        botaoLogout = findViewById(R.id.btnLogout);

        // Definindo texto do ID do usuário
        idUsuarioTextView.setText("ID: " + String.valueOf(userId));

        // Configurando os dados do usuário
        setupUserData(userId);

        // Adicionando OnClickListener ao botão de voltar
        botaoVoltarHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Voltando para a tela principal e passando o ID do usuário de volta
                Intent intent = new Intent(PerfilActivity.this, ChecklistMain.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
                finish(); // Finalizando a atividade atual para evitar empilhamento desnecessário
            }
        });

        // Adicionando OnClickListener ao botão de mudança de senha
        botaoChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegando para a atividade de mudança de senha e passando o ID do usuário
                Intent intent = new Intent(PerfilActivity.this, PasswordActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            }
        });

        // Adicionando OnClickListener ao botão de ajuda e suporte
        botaoHelpSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHelpSupportDialog();
            }
        });

        // Adicionando OnClickListener ao botão sobre nós
        botaoAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutUsDialog();
            }
        });

        // Adicionando OnClickListener ao botão de logout
        botaoLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Limpando as preferências (simulação de logout)
                getSharedPreferences("user_session", MODE_PRIVATE).edit().clear().apply();

                // Navegando para a tela de login e limpando o back stack
                Intent intent = new Intent(PerfilActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish(); // Finalizando a atividade atual
            }
        });
    }

    private void setupUserData(int userId) {
        // Observe o LiveData para obter o usuário
        checklistDao.getUserById(userId).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    // Define o nome de usuário na TextView
                    nomeUsuarioTextView.setText(user.getUsername());
                } else {
                    nomeUsuarioTextView.setText("Nome de Usuário Não Encontrado");
                }
            }
        });
    }

    private void showHelpSupportDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Help and Support")
                .setMessage("For any help or support, please contact us at: alemaole.luis@gmail.com.\n\n" +
                        "We are here to assist you with any issues or questions you may have. Our support team " +
                        "is available to help you with using the TaskBro app, troubleshooting problems, and " +
                        "providing guidance. Feel free to reach out to us anytime.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void showAboutUsDialog() {
        new AlertDialog.Builder(this)
                .setTitle("About Us")
                .setMessage("The TaskBro app was created as part of the Extension course at Unifil University " +
                        "in the ADS program by student Luis Leandro Pires Ribeiro. The purpose of this app is to " +
                        "help users manage their tasks and checklists efficiently. TaskBro aims to provide a simple " +
                        "and effective way for users to keep track of their daily tasks, ensuring they stay organized " +
                        "and productive.\n\n" +
                        "Thank you for using TaskBro!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
