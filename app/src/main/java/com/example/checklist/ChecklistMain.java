package com.example.checklist;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ChecklistMain extends AppCompatActivity {

    private ListView listViewTarefas;
    private String currentTab = "ToDo";
    private List<Tarefa> listaTarefas;
    private List<Tarefa> listaTarefasFiltradas; // Lista para armazenar as tarefas filtradas
    private TarefaAdapter adapter;
    private int userId;
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);
        userId = getIntent().getIntExtra("USER_ID", -1);
        Log.d("ChecklistMain", "User ID received: " + userId);

        // Inicializa a lista de tarefas
        listaTarefas = new ArrayList<>();
        listaTarefasFiltradas = new ArrayList<>(); // Inicializa a lista filtrada

        // Inicializa o ListView e o TarefaAdapter
        listViewTarefas = findViewById(R.id.listViewTarefas);
        searchEditText = findViewById(R.id.searchEditText);
        adapter = new TarefaAdapter(this, listaTarefasFiltradas);
        listViewTarefas.setAdapter(adapter);

        listViewTarefas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tarefa tarefaSelecionada = listaTarefasFiltradas.get(position);

                Intent intent = new Intent(ChecklistMain.this, AdicionarTarefaActivity.class);
                intent.putExtra("USER_ID", userId);
                intent.putExtra("TAREFA_ID", tarefaSelecionada.getId());
                intent.putExtra("TITULO", tarefaSelecionada.getTitulo());
                intent.putExtra("SUBTITULO", tarefaSelecionada.getSubtitulo());
                intent.putExtra("DATA", tarefaSelecionada.getData());
                intent.putExtra("HORA", tarefaSelecionada.getHora());
                intent.putExtra("DESCRICAO", tarefaSelecionada.getDescricao());
                intent.putExtra("TAG", tarefaSelecionada.getTag());
                intent.putExtra("FAVORITA", tarefaSelecionada.isFavorited());
                startActivity(intent);
            }
        });

        // Adiciona um listener ao campo de pesquisa
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Não necessário implementar
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Filtra as tarefas enquanto o texto é digitado
                filtrarTarefas(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Não necessário implementar
            }
        });

        // Configura o clique no ícone Home
        ImageView iconHome = findViewById(R.id.iconHome);
        iconHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Volta para a tela inicial (pode ser a mesma activity ou outra)
                Intent intent = new Intent(ChecklistMain.this, ChecklistMain.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
                finish(); // Opcional: finaliza a atividade atual para evitar múltiplas instâncias
            }
        });

        // Configura o clique no ícone Favorito
        ImageView iconFav = findViewById(R.id.iconFav);
        iconFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Determina a aba atual (ToDo ou Completed) e carrega as tarefas favoritas
                if (currentTab.equals("ToDo")) {
                    carregarTarefasFavoritasDoBancoDeDados("ToDo");
                } else if (currentTab.equals("Completed")) {
                    carregarTarefasFavoritasDoBancoDeDados("Completed");
                }
            }
        });

        // Carrega as tarefas do banco de dados ao iniciar a atividade
        carregarTarefasDoBancoDeDados("ToDo"); // Carrega as tarefas "ToDo" por padrão
    }

    private void carregarTarefasFavoritasDoBancoDeDados(String tipo) {
        // Método para carregar as tarefas favoritas do banco de dados com base no tipo (ToDo ou Completed)
        TarefaDAO tarefaDAO = new TarefaDAO(this);

        // Limpa a lista de tarefas
        listaTarefas.clear();

        // Obtém as tarefas favoritas do banco de dados com base no tipo
        if (tipo.equals("ToDo")) {
            // Carrega as tarefas "ToDo" que também são favoritas
            listaTarefas.addAll(tarefaDAO.obterTarefasNaoFinalizadasFavoritasPorUsuario(userId));
        } else if (tipo.equals("Completed")) {
            // Carrega as tarefas "Completed" que também são favoritas
            listaTarefas.addAll(tarefaDAO.obterTarefasFinalizadasFavoritasPorUsuario(userId));
        }

        // Atualiza a lista filtrada com as tarefas favoritas
        listaTarefasFiltradas.clear();
        listaTarefasFiltradas.addAll(listaTarefas);

        // Notifica o adapter sobre as mudanças nos dados
        adapter.notifyDataSetChanged();

        // Define a visibilidade do ListView com base na presença de tarefas
        listViewTarefas.setVisibility(listaTarefasFiltradas.isEmpty() ? View.GONE : View.VISIBLE);
    }


    public void showToDoTasks(View view) {
        // Carrega as tarefas "ToDo" ao clicar em ToDo
        currentTab = "ToDo"; // Atualiza a aba atual para "ToDo"
        carregarTarefasDoBancoDeDados("ToDo");
    }

    public void showCompletedTasks(View view) {
        currentTab = "Completed"; // Atualiza a aba atual para "Completed"
        carregarTarefasDoBancoDeDados("Completed");
    }

    private void carregarTarefasDoBancoDeDados(String tipo) {
        // Método para carregar as tarefas do banco de dados com base no tipo (ToDo ou Completed)
        TarefaDAO tarefaDAO = new TarefaDAO(this);

        // Limpa a lista de tarefas
        listaTarefas.clear();

        // Obtém as tarefas do banco de dados com base no tipo
        if (tipo.equals("ToDo")) {
            // Carrega as tarefas "ToDo" que não são favoritas
            listaTarefas.addAll(tarefaDAO.obterTarefasNaoFinalizadasPorUsuario(userId));
        } else if (tipo.equals("Completed")) {
            // Carrega as tarefas "Completed" que não são favoritas
            listaTarefas.addAll(tarefaDAO.obterTarefasFinalizadasPorUsuario(userId));
        }

        // Atualiza a lista filtrada com as tarefas não favoritas
        listaTarefasFiltradas.clear();
        listaTarefasFiltradas.addAll(listaTarefas);

        // Notifica o adapter sobre as mudanças nos dados
        adapter.notifyDataSetChanged();

        // Define a visibilidade do ListView com base na presença de tarefas
        listViewTarefas.setVisibility(listaTarefasFiltradas.isEmpty() ? View.GONE : View.VISIBLE);
    }

    private void filtrarTarefas(String texto) {
        // Método para filtrar as tarefas pelo título
        listaTarefasFiltradas.clear();
        for (Tarefa tarefa : listaTarefas) {
            if (tarefa.getTitulo().toLowerCase().contains(texto.toLowerCase())) {
                listaTarefasFiltradas.add(tarefa);
            }
        }
        adapter.notifyDataSetChanged();

        // Define a visibilidade do ListView com base na presença de tarefas
        listViewTarefas.setVisibility(listaTarefasFiltradas.isEmpty() ? View.GONE : View.VISIBLE);
    }

    public void openAdicionarTarefaActivity(View view) {
        // Iniciar a AdicionarTarefaActivity e passar o ID do usuário como extra
        Intent intent = new Intent(this, AdicionarTarefaActivity.class);
        intent.putExtra("USER_ID", userId); // Passa o ID do usuário para a próxima atividade
        startActivity(intent);
    }

    private void carregarTarefasPorTag() {
        // Método para carregar as tarefas do banco de dados agrupadas por tag
        TarefaDAO tarefaDAO = new TarefaDAO(this);

        // Limpa a lista de tarefas
        listaTarefas.clear();

        // Obtém todas as tags disponíveis no banco de dados
        List<String> tags = tarefaDAO.obterTagsPorUsuario(userId);

        // Para cada tag, obtém as tarefas associadas a ela e as adiciona à lista de tarefas
        for (String tag : tags) {
            // Obtém as tarefas associadas à tag atual
            List<Tarefa> tarefasPorTag = tarefaDAO.obterTarefasPorTag(userId, tag);

            // Adiciona as tarefas à lista de tarefas
            listaTarefas.addAll(tarefasPorTag);
        }

        // Copia a lista original para a lista filtrada
        listaTarefasFiltradas.clear();
        listaTarefasFiltradas.addAll(listaTarefas);

        // Notifica o adapter sobre as mudanças nos dados
        adapter.notifyDataSetChanged();

        // Define a visibilidade do ListView com base na presença de tarefas
        listViewTarefas.setVisibility(listaTarefasFiltradas.isEmpty() ? View.GONE : View.VISIBLE);
    }

    public void showTags(View view) {
        // Carrega as tarefas agrupadas por tag ao clicar em Tag
        carregarTarefasPorTag();
    }
}
