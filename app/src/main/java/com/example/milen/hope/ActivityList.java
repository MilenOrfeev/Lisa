package com.example.milen.hope;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ActivityList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
    }

    public void onCreateButtonClicked(View view)
    {
        Intent intent = new Intent(this, CreateList.class);
        startActivity(intent);
    }

    public void onSeeListsButtonClicked(View view)
    {
        Intent intent = new Intent(this, SeeLists.class);
        startActivity(intent);
    }
}
