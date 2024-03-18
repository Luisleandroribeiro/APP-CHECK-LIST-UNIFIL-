package com.example.checklist;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "checklist_items")
public class ChecklistItem {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private boolean completed;

    // Construtor
    public ChecklistItem(String name, boolean completed) {
        this.name = name;
        this.completed = completed;
    }

    // Getter para o campo 'id'
    public int getId() {
        return id;
    }

    // Setter para o campo 'id'
    public void setId(int id) {
        this.id = id;
    }

    // Getter para o campo 'name'
    public String getName() {
        return name;
    }

    // Setter para o campo 'name'
    public void setName(String name) {
        this.name = name;
    }

    // Getter para o campo 'completed'
    public boolean isCompleted() {
        return completed;
    }

    // Setter para o campo 'completed'
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
