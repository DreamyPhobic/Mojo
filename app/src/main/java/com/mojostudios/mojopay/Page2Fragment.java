package com.mojostudios.mojopay;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class Page2Fragment extends Fragment {
    private RecyclerView mRecyclerView;
    private PersonsListDBHelper dbHelper;
    private PersonAdapter adapter;
    private String filter = "";
    ItemTouchHelper helper;
    List<Person> Dataset;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page2, container, false);
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        dbHelper = new PersonsListDBHelper(getActivity());
        Dataset=dbHelper.personList(filter);
        if(Dataset.isEmpty()){
            dbHelper.update();
        }
        setAdapter();
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        return rootView;
    }
    private void setAdapter() {
        adapter = new PersonAdapter(Dataset,getActivity(), mRecyclerView);
        mRecyclerView.setAdapter(adapter);
        Log.d("totalEntries",String.valueOf(dbHelper.getallCount()));
    }

}
