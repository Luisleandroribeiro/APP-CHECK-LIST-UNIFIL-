package com.example.checklist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class TarefaDAO extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tarefas.db";
    private static final int DATABASE_VERSION = 5;
    private static final String TABLE_NAME = "tarefas";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITULO = "titulo";
    private static final String COLUMN_SUBTITULO = "subtitulo";
    private static final String COLUMN_DATA = "data";
    private static final String COLUMN_HORA = "hora";
    private static final String COLUMN_DESCRICAO = "descricao";
    private static final String COLUMN_FINALIZADA = "finalizada";

    private static final String COLUMN_USER_ID = "user_id";


    public TarefaDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITULO + " TEXT, " +
                COLUMN_SUBTITULO + " TEXT, " +
                COLUMN_DATA + " TEXT, " +
                COLUMN_HORA + " TEXT, " +
                COLUMN_DESCRICAO + " TEXT, " +
                COLUMN_FINALIZADA + " INTEGER DEFAULT 0, " + // Adicione a coluna COLUMN_FINALIZADA
                COLUMN_USER_ID + " INTEGER)";
        db.execSQL(query);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long inserirTarefa(Tarefa tarefa, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITULO, tarefa.getTitulo());
        values.put(COLUMN_SUBTITULO, tarefa.getSubtitulo());
        values.put(COLUMN_DATA, tarefa.getData());
        values.put(COLUMN_HORA, tarefa.getHora());
        values.put(COLUMN_DESCRICAO, tarefa.getDescricao());
        values.put(COLUMN_USER_ID, userId); // Adicione o ID do usuário ao ContentValues
        long resultado = db.insert(TABLE_NAME, null, values);
        db.close();
        return resultado;
    }


    public List<Tarefa> obterTarefasPorUsuario(int userId) {
        List<Tarefa> listaTarefas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                Tarefa tarefa = new Tarefa();
                tarefa.setId(cursor.getInt(0));
                tarefa.setTitulo(cursor.getString(1));
                tarefa.setSubtitulo(cursor.getString(2));
                tarefa.setData(cursor.getString(3));
                tarefa.setHora(cursor.getString(4));
                tarefa.setDescricao(cursor.getString(5));
                listaTarefas.add(tarefa);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listaTarefas;
    }

    public int atualizarTarefa(Tarefa tarefa) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITULO, tarefa.getTitulo());
        values.put(COLUMN_SUBTITULO, tarefa.getSubtitulo());
        values.put(COLUMN_DATA, tarefa.getData());
        values.put(COLUMN_HORA, tarefa.getHora());
        values.put(COLUMN_DESCRICAO, tarefa.getDescricao());
        return db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(tarefa.getId())});
    }
    public int deletarTarefa(int tarefaId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("tarefas", "id = ?", new String[]{String.valueOf(tarefaId)});
    }
    // Método para obter todas as tarefas finalizadas
    public List<Tarefa> obterTarefasFinalizadasPorUsuario(int userId) {
        List<Tarefa> listaTarefas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_USER_ID + " = ? AND " + COLUMN_FINALIZADA + " = 1",
                new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                Tarefa tarefa = new Tarefa();
                tarefa.setId(cursor.getInt(0));
                tarefa.setTitulo(cursor.getString(1));
                tarefa.setSubtitulo(cursor.getString(2));
                tarefa.setData(cursor.getString(3));
                tarefa.setHora(cursor.getString(4));
                tarefa.setDescricao(cursor.getString(5));
                listaTarefas.add(tarefa);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listaTarefas;
    }


    public int marcarComoFinalizada(Tarefa tarefa) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FINALIZADA, 1); // Define o valor de COLUMN_FINALIZADA como 1 para indicar que a tarefa foi finalizada
        return db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(tarefa.getId())});
    }
    public List<Tarefa> obterTarefasNaoFinalizadasPorUsuario(int userId) {
        List<Tarefa> listaTarefas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_USER_ID + " = ? AND " + COLUMN_FINALIZADA + " = 0",
                new String[]{String.valueOf(userId)}); // Alterado para obter apenas tarefas não finalizadas
        if (cursor.moveToFirst()) {
            do {
                Tarefa tarefa = new Tarefa();
                tarefa.setId(cursor.getInt(0));
                tarefa.setTitulo(cursor.getString(1));
                tarefa.setSubtitulo(cursor.getString(2));
                tarefa.setData(cursor.getString(3));
                tarefa.setHora(cursor.getString(4));
                tarefa.setDescricao(cursor.getString(5));
                listaTarefas.add(tarefa);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return listaTarefas;
    }
}




