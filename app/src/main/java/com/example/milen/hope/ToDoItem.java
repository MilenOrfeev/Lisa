package com.example.milen.hope;

import com.example.milen.hope.DatedItem;

import java.util.Date;

public class ToDoItem {
    private boolean completed;
    private final String description;
    ToDoItem(String description, boolean completed) {
        this.description = description;
        this.completed = completed;
    }

    ToDoItem(String description) {
        this(description, false);
    }

    public String getDescription() {
        return description;
    }
    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override public String toString() {
        //return super.toString() +  " " + completed;
        return description + " " + completed;
    }
}
