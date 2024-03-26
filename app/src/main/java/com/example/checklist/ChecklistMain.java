package com.example.checklist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
    private int userId; // Variável de instância para armazenar o ID do usuário


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);
        userId = getIntent().getIntExtra("USER_ID", -1);
        Log.d("ChecklistMain", "User ID received: " + userId);

        // Inicializa a lista de tarefas
        listaTarefas = new ArrayList<>();

        // Inicializa o ListView e o TarefaAdapter
        listViewTarefas = findViewById(R.id.listViewTarefas);
        adapter = new TarefaAdapter(this, listaTarefas); // Alteração aqui
        listViewTarefas.setAdapter(adapter);

        // Define o ouvinte de clique para o ListView
        listViewTarefas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Obtém a tarefa selecionada
                Tarefa tarefaSelecionada = listaTarefas.get(position);

                // Inicia a AdicionarTarefaActivity para edição da tarefa selecionada
                Intent intent = new Intent(ChecklistMain.this, AdicionarTarefaActivity.class);
                intent.putExtra("USER_ID", userId); // Passa o ID do usuário
                intent.putExtra("TAREFA_ID", tarefaSelecionada.getId()); // Passa o ID da tarefa
                intent.putExtra("TITULO", tarefaSelecionada.getTitulo()); // Passa o título da tarefa
                intent.putExtra("SUBTITULO", tarefaSelecionada.getSubtitulo()); // Passa o subtítulo da tarefa
                intent.putExtra("DATA", tarefaSelecionada.getData()); // Passa a data da tarefa
                intent.putExtra("HORA", tarefaSelecionada.getHora()); // Passa a hora da tarefa
                intent.putExtra("DESCRICAO", tarefaSelecionada.getDescricao()); // Passa a descrição da tarefa

                startActivity(intent);
            }
        });

        // Carrega as tarefas do banco de dados
        carregarTarefasDoBancoDeDados();
    }
    public void showMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.checklistAbertos) {
            // Ação para a primeira opção do menu
            Intent intent = new Intent(this, ChecklistMain.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.checklistFinalizados) {
            // Ação para a segunda opção do menu
            Intent intent = new Intent(this, ChecklistFinishActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
            return true;
        }
        return false;
    }



    // Método para carregar as tarefas do banco de dados e exibi-las na lista
    private void carregarTarefasDoBancoDeDados() {
        TarefaDAO tarefaDAO = new TarefaDAO(this);
        listaTarefas.clear(); // Limpa a lista para evitar duplicatas
        listaTarefas.addAll(tarefaDAO.obterTarefasPorUsuario(userId)); // Obtenha apenas as tarefas do usuário
        adapter.notifyDataSetChanged(); // Notifica o TarefaAdapter que os dados foram alterados

        // Define a visibilidade do ListView com base na presença de tarefas
        if (listaTarefas.isEmpty()) {
            listViewTarefas.setVisibility(View.GONE); // Se não houver tarefas, oculta o ListView
        } else {
            listViewTarefas.setVisibility(View.VISIBLE); // Se houver tarefas, exibe o ListView
        }
    }


    public void openAdicionarTarefaActivity(View view) {
        // Iniciar a AdicionarTarefaActivity e passar o ID do usuário como extra
        Intent intent = new Intent(this, AdicionarTarefaActivity.class);
        intent.putExtra("USER_ID", userId); // Passa o ID do usuário para a próxima atividade
        Log.d("ChecklistMainMenu", "User ID in menu: " + userId); // Log para verificar se o userId está sendo passado para o menu
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarTarefasDoBancoDeDados();
        Log.d("ChecklistMain", "Total de tarefas: " + listaTarefas.size());
    }

}
