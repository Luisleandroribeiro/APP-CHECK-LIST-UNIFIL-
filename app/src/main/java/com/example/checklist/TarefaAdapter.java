package com.example.checklist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TarefaAdapter extends ArrayAdapter<Tarefa> {

    private TarefaDAO tarefaDAO;

    public TarefaAdapter(Context context, List<Tarefa> tarefas) {
        super(context, 0, tarefas);
        tarefaDAO = new TarefaDAO(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Verifica se uma view existente está sendo reutilizada, caso contrário, infla a view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_tarefa, parent, false);
        }

        // Obtém a tarefa para essa posição
        Tarefa tarefa = getItem(position);

        // Configura os elementos da view com os dados da tarefa
        TextView tituloTextView = convertView.findViewById(R.id.titleTextView);
        tituloTextView.setText(tarefa.getTitulo());

        TextView subtitleTextView = convertView.findViewById(R.id.subtitleTextView);
        subtitleTextView.setText(tarefa.getSubtitulo());

        TextView dateTextView = convertView.findViewById(R.id.dateTextView);
        dateTextView.setText(tarefa.getData());

        TextView deadlineTextView = convertView.findViewById(R.id.deadlineTextView);
        deadlineTextView.setText(tarefa.getHora());

        return convertView;
    }
}
