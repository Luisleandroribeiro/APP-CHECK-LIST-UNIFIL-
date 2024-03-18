package com.example.checklist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room; // Importe a classe Room

public class MainActivity extends AppCompatActivity {
    private ChecklistDatabase db; // Declare a variável para o banco de dados

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar o banco de dados
        db = Room.databaseBuilder(getApplicationContext(),
                ChecklistDatabase.class, "checklist-db").build();

        Button registerButton = findViewById(R.id.startButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar para a tela de login
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
