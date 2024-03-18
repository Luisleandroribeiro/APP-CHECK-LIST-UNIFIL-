package com.example.checklist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AdicionarTarefaActivity extends AppCompatActivity {

    private EditText dataEditText, horaEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_tarefa);

        // Inicializa os EditTexts
        dataEditText = findViewById(R.id.dataEditText);
        horaEditText = findViewById(R.id.horaEditText);

        // Define os listeners de clique nos EditTexts
        dataEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDatePicker();
            }
        });

        horaEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarTimePicker();
            }
        });
    }

    // Método para mostrar o DatePicker
    private void mostrarDatePicker() {
        final Calendar calendario = Calendar.getInstance();
        int ano = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dataSelecionada = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        dataEditText.setText(dataSelecionada);
                    }
                }, ano, mes, dia);
        datePickerDialog.show();
    }

    // Método para mostrar o TimePicker
    private void mostrarTimePicker() {
        final Calendar calendario = Calendar.getInstance();
        int hora = calendario.get(Calendar.HOUR_OF_DAY);
        int minuto = calendario.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String horaSelecionada = hourOfDay + ":" + minute;
                        horaEditText.setText(horaSelecionada);
                    }
                }, hora, minuto, true);
        timePickerDialog.show();
    }

    // Método para salvar a tarefa
    public void salvarTarefa(View view) {
        // Obter os dados da tarefa dos campos de entrada
        String titulo = tituloEditText.getText().toString();
        String subtitulo = subtituloEditText.getText().toString();
        String data = dataEditText.getText().toString();
        String hora = horaEditText.getText().toString();
        String descricao = descricaoEditText.getText().toString();

        // Criar um objeto de tarefa
        Tarefa tarefa = new Tarefa(titulo, subtitulo, data, hora, descricao);

        // Salvar a tarefa no banco de dados
        long resultado = tarefaDAO.inserirTarefa(tarefa);

        if (resultado != -1) {
            Toast.makeText(this, "Tarefa salva com sucesso!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Falha ao salvar a tarefa", Toast.LENGTH_SHORT).show();
        }
    }


    // Método para cancelar a adição da tarefa
    public void cancelar(View view) {
        // Implemente a lógica para cancelar a adição da tarefa
        finish(); // Fecha a atividade de adicionar tarefa
    }
}
