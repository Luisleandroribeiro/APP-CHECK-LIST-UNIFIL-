package com.example.checklist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AdicionarTarefaActivity extends AppCompatActivity {

    private EditText tituloEditText, subtituloEditText, descricaoEditText;
    private EditText dataEditText, horaEditText;
    private Button cancelarButton, salvarButton, deletarButton;
    private ImageView imagemIconFinalizar;
    private int userId;
    private Intent intent;
    private boolean tarefaAdicionada = false; // Variável para controlar se a tarefa foi adicionada

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_tarefa);
        userId = getIntent().getIntExtra("USER_ID", -1);
        intent = getIntent();

        tituloEditText = findViewById(R.id.tituloEditText);
        subtituloEditText = findViewById(R.id.subtituloEditText);
        descricaoEditText = findViewById(R.id.descricaoEditText);
        dataEditText = findViewById(R.id.dataEditText);
        horaEditText = findViewById(R.id.horaEditText);
        cancelarButton = findViewById(R.id.cancelarButton);
        salvarButton = findViewById(R.id.salvarButton);
        deletarButton = findViewById(R.id.deletarButton);
        imagemIconFinalizar = findViewById(R.id.imagemIconFinalizar);

        // Verifique se a tarefa foi adicionada
        // Por exemplo, se você estiver recuperando essa informação de algum lugar, como um banco de dados
        if (tarefaAdicionada || intent.hasExtra("TAREFA_ID")) {
            imagemIconFinalizar.setVisibility(View.VISIBLE);
            // Adiciona o OnClickListener para o botão de finalizar apenas se a tarefa foi adicionada
            imagemIconFinalizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalizarAtividade();
                }
            });
        } else {
            imagemIconFinalizar.setVisibility(View.GONE);
        }
        cancelarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelarAdicaoTarefa();
            }
        });

        salvarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarTarefa();
            }
        });

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

        if (intent.hasExtra("TAREFA_ID")) {
            deletarButton.setVisibility(View.VISIBLE);
            deletarButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deletarTarefa();
                }
            });
        } else {
            deletarButton.setVisibility(View.INVISIBLE);
        }

        preencherCamposComDadosDaTarefa();
        verificarExibicaoBotaoFinalizar();
    }

    private void verificarExibicaoBotaoFinalizar() {
        // Verifique se a tarefa está marcada como finalizada
        boolean tarefaFinalizada = intent.getBooleanExtra("FINALIZADA", false);

        // Verifique se a atividade está sendo editada
        boolean editandoTarefa = intent.hasExtra("TAREFA_ID");

        // Se a tarefa estiver marcada como finalizada e não estiver sendo editada, deixe o botão de finalizar invisível
        if (tarefaFinalizada && !editandoTarefa) {
            imagemIconFinalizar.setVisibility(View.GONE);
        } else {
            // Caso contrário, deixe o botão de finalizar visível
            imagemIconFinalizar.setVisibility(View.VISIBLE);
            imagemIconFinalizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalizarAtividade();
                }
            });
        }
    }

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

    private void mostrarTimePicker() {
        final Calendar calendario = Calendar.getInstance();
        int hora = calendario.get(Calendar.HOUR_OF_DAY);
        int minuto = calendario.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String horaFormatada = String.format("%02d", hourOfDay);
                        String minutoFormatado = String.format("%02d", minute);
                        String horaSelecionada = horaFormatada + ":" + minutoFormatado;
                        horaEditText.setText(horaSelecionada);
                    }
                }, hora, minuto, true);
        timePickerDialog.show();
    }

    private void preencherCamposComDadosDaTarefa() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("TITULO") && intent.hasExtra("SUBTITULO")
                && intent.hasExtra("DATA") && intent.hasExtra("HORA") && intent.hasExtra("DESCRICAO")) {
            tituloEditText.setText(intent.getStringExtra("TITULO"));
            subtituloEditText.setText(intent.getStringExtra("SUBTITULO"));
            dataEditText.setText(intent.getStringExtra("DATA"));
            horaEditText.setText(intent.getStringExtra("HORA"));
            descricaoEditText.setText(intent.getStringExtra("DESCRICAO"));

            verificarExibicaoBotaoFinalizar();
        }
    }



    private void salvarTarefa() {
        String titulo = tituloEditText.getText().toString();
        String subtitulo = subtituloEditText.getText().toString();
        String data = dataEditText.getText().toString();
        String hora = horaEditText.getText().toString();
        String descricao = descricaoEditText.getText().toString();

        if (titulo.isEmpty() || subtitulo.isEmpty() || data.isEmpty() || hora.isEmpty() || descricao.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        Tarefa tarefa = new Tarefa(titulo, subtitulo, data, hora, descricao);

        if (!intent.hasExtra("TAREFA_ID")) {
            TarefaDAO tarefaDAO = new TarefaDAO(this);
            long resultado = tarefaDAO.inserirTarefa(tarefa, userId);
            if (resultado != -1) {
                Toast.makeText(this, "Tarefa salva com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Falha ao salvar a tarefa", Toast.LENGTH_SHORT).show();
            }
        } else {
            int tarefaId = intent.getIntExtra("TAREFA_ID", -1);
            if (tarefaId != -1) {
                tarefa.setId(tarefaId);
                TarefaDAO tarefaDAO = new TarefaDAO(this);
                int linhasAfetadas = tarefaDAO.atualizarTarefa(tarefa);
                if (linhasAfetadas > 0) {
                    Toast.makeText(this, "Tarefa atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Falha ao atualizar a tarefa", Toast.LENGTH_SHORT).show();
                }
            }
        }

        Intent intent = new Intent(this, ChecklistMain.class);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
        finish();
    }

    private void cancelarAdicaoTarefa() {
        Intent intent = new Intent(this, ChecklistMain.class);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
        finish();
    }

    private void deletarTarefa() {
        int tarefaId = intent.getIntExtra("TAREFA_ID", -1);
        if (tarefaId != -1) {
            TarefaDAO tarefaDAO = new TarefaDAO(this);
            int linhasAfetadas = tarefaDAO.deletarTarefa(tarefaId);
            if (linhasAfetadas > 0) {
                Toast.makeText(this, "Tarefa deletada com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Falha ao deletar a tarefa", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(this, ChecklistMain.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
            finish();
        }
    }

    private void finalizarAtividade() {
        // Aqui você pode implementar a lógica para finalizar a tarefa
        // Por exemplo, mover a tarefa para a lista de tarefas finalizadas
        // Ou atualizar o status da tarefa no banco de dados

        // Primeiro, obtenha o ID da tarefa da Intent
        int tarefaId = intent.getIntExtra("TAREFA_ID", -1);

        // Verifique se o ID da tarefa é válido
        if (tarefaId != -1) {
            // Se o ID da tarefa for válido, crie um objeto Tarefa com o ID
            Tarefa tarefa = new Tarefa();
            tarefa.setId(tarefaId);

            // Em seguida, crie uma instância de TarefaDAO para atualizar a tarefa
            TarefaDAO tarefaDAO = new TarefaDAO(this);

            // Em seguida, chame o método para atualizar a tarefa como finalizada no banco de dados
            int linhasAfetadas = tarefaDAO.marcarComoFinalizada(tarefa);

            // Verifique se a atualização foi bem-sucedida
            if (linhasAfetadas > 0) {
                // Se a atualização foi bem-sucedida, exiba uma mensagem de sucesso
                Toast.makeText(this, "Tarefa finalizada com sucesso!", Toast.LENGTH_SHORT).show();

                // Atualize a tela atual (ChecklistMain) para refletir a alteração no status da tarefa
                Intent updateIntent = new Intent(this, ChecklistMain.class);
                updateIntent.putExtra("USER_ID", userId);
                startActivity(updateIntent);
                finish();
            } else {
                // Se ocorrer um erro ao atualizar a tarefa no banco de dados, exiba uma mensagem de erro
                Toast.makeText(this, "Erro ao finalizar a tarefa", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Se o ID da tarefa não for válido, exiba uma mensagem de erro
            Toast.makeText(this, "ID da tarefa inválido", Toast.LENGTH_SHORT).show();
        }
    }
}