package com.example.checklist;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ChecklistMain extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private ListView listViewTarefas;
    private List<Tarefa> listaTarefas;
    private TarefaAdapter adapter; // Alteração para o TarefaAdapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);

        // Inicializa a lista de tarefas
        listaTarefas = new ArrayList<>();

        // Inicializa o ListView e o TarefaAdapter
        listViewTarefas = findViewById(R.id.listViewTarefas);
        adapter = new TarefaAdapter(this, listaTarefas); // Alteração aqui
        listViewTarefas.setAdapter(adapter);

        // Carrega as tarefas do banco de dados
        carregarTarefasDoBancoDeDados();
    }

    // Método para carregar as tarefas do banco de dados e exibi-las na lista
    private void carregarTarefasDoBancoDeDados() {
        TarefaDAO tarefaDAO = new TarefaDAO(this);
        listaTarefas.clear(); // Limpa a lista para evitar duplicatas
        listaTarefas.addAll(tarefaDAO.obterTodasTarefas()); // Adiciona todas as tarefas do banco de dados
        adapter.notifyDataSetChanged(); // Notifica o TarefaAdapter que os dados foram alterados

        // Define a visibilidade do ListView com base na presença de tarefas
        if (listaTarefas.isEmpty()) {
            listViewTarefas.setVisibility(View.GONE); // Se não houver tarefas, oculta o ListView
        } else {
            listViewTarefas.setVisibility(View.VISIBLE); // Se houver tarefas, exibe o ListView
        }
    }

    public void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.addTask) {
            // Iniciar a atividade de adicionar tarefa quando o item for clicado
            startActivity(new Intent(this, AdicionarTarefaActivity.class));
            return true;
        } else if (itemId == R.id.completeTask) {
            // Implementar ação para concluir tarefa
            return true;
        } else if (itemId == R.id.removeTask) {
            // Implementar ação para remover tarefa
            return true;
        }
        return false;
    }
}
