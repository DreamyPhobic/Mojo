package com.example.harshgupta.balanceit;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import java.util.List;

import javax.security.auth.Subject;

public class PersonsList extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private PersonsListDBHelper dbHelper;
    private PersonAdapter adapter;
    private String filter = "";
    ItemTouchHelper helper;
    List<Person> Dataset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        dbHelper = new PersonsListDBHelper(this);
        Dataset=dbHelper.personList(filter);
        if(Dataset.isEmpty()){
            dbHelper.update();
        }
        setAdapter();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setAdapter() {
        adapter = new PersonAdapter(Dataset,this, mRecyclerView);
        mRecyclerView.setAdapter(adapter);
        Log.d("totalEntries",String.valueOf(dbHelper.getallCount()));
    }

}
