package com.example.milen.hope

import com.example.milen.hope.DatedItem

import java.util.Date

class ToDoItem @JvmOverloads internal constructor(val description: String, var isCompleted: Boolean = false) {

    override fun toString(): String {
        //return super.toString() +  " " + completed;
        return "$description $isCompleted"
    }
}
