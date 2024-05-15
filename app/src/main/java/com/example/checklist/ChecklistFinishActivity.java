package com.example.checklist;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ChecklistFinishActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private ListView listViewTarefas;
    private TarefaAdapter adapter;
    private TarefaDAO tarefaDAO;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklistfinish);
        userId = getIntent().getIntExtra("USER_ID", -1);

        listViewTarefas = findViewById(R.id.listViewTarefasfinish);
        tarefaDAO = new TarefaDAO(this);

        // Obter apenas as tarefas finalizadas do usuário logado
        List<Tarefa> tarefasFinalizadas = tarefaDAO.obterTarefasFinalizadasPorUsuario(userId); // Alteração aqui

        adapter = new TarefaAdapter(this, tarefasFinalizadas);
        listViewTarefas.setAdapter(adapter);

        registerForContextMenu(listViewTarefas);

        listViewTarefas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tarefa tarefaSelecionada = (Tarefa) adapter.getItem(position);
                Intent intent = new Intent(ChecklistFinishActivity.this, AdicionarTarefaActivity.class);
                intent.putExtra("USER_ID", userId);
                intent.putExtra("TAREFA_ID", tarefaSelecionada.getId());
                intent.putExtra("TITULO", tarefaSelecionada.getTitulo());
                intent.putExtra("SUBTITULO", tarefaSelecionada.getSubtitulo());
                intent.putExtra("DATA", tarefaSelecionada.getData());
                intent.putExtra("HORA", tarefaSelecionada.getHora());
                intent.putExtra("DESCRICAO", tarefaSelecionada.getDescricao());
                intent.putExtra("TAG", tarefaSelecionada.getTag());
                startActivity(intent);
            }
        });
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
            Intent intent = new Intent(this, ChecklistMain.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.checklistFinalizados) {
            return true; // Nenhuma ação necessária, já estamos na atividade ChecklistFinishActivity
        }
        return false;
    }
}
