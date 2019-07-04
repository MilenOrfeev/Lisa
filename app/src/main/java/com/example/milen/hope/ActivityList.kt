package com.example.milen.hope

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class ActivityList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
    }

    fun onCreateButtonClicked(view: View) {
        val intent = Intent(this, CreateList::class.java)
        startActivity(intent)
    }

    fun onSeeListsButtonClicked(view: View) {
        val intent = Intent(this, SeeLists::class.java)
        startActivity(intent)
    }
}
