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
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "tarefas";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITULO = "titulo";
    private static final String COLUMN_SUBTITULO = "subtitulo";
    private static final String COLUMN_DATA = "data";
    private static final String COLUMN_HORA = "hora";
    private static final String COLUMN_DESCRICAO = "descricao";

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
                COLUMN_DESCRICAO + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long inserirTarefa(Tarefa tarefa) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITULO, tarefa.getTitulo());
        values.put(COLUMN_SUBTITULO, tarefa.getSubtitulo());
        values.put(COLUMN_DATA, tarefa.getData());
        values.put(COLUMN_HORA, tarefa.getHora());
        values.put(COLUMN_DESCRICAO, tarefa.getDescricao());
        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result;
    }

    public List<Tarefa> obterTodasTarefas() {
        List<Tarefa> listaTarefas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

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
}
