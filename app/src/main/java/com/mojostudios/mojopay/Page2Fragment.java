package com.mojostudios.mojopay;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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

        helper=new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN|ItemTouchHelper.UP|ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//               int from=viewHolder.getAdapterPosition();
//               int to=target.getAdapterPosition();
                Person temp1=Dataset.get(viewHolder.getAdapterPosition());
                Person temp2= Dataset.get(target.getAdapterPosition());

                int dbto=temp2.getID();
                int dbfrom=temp1.getID();
                //temp1.setID(to);
                //temp2.setID(from);
                dbHelper.updatePersonRecord(dbto,getActivity(),temp1);
                dbHelper.updatePersonRecord(dbfrom,getActivity(),temp2);
                Dataset=dbHelper.personList(filter);
                adapter.notifyItemMoved(viewHolder.getAdapterPosition(),target.getAdapterPosition());
                //adapter.notifyDataSetChanged();
                // setAdapter();
                return true;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                final int idk=viewHolder.getAdapterPosition();
                final Person temp=Dataset.get(idk);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.delete_message)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Delete the Subject

                                Log.d("idtoremove",String.valueOf(idk));
                                Person temp=Dataset.get(idk);
                                int resId=temp.getID();

                                Person subject=dbHelper.getPerson(resId);
                                Log.d("idtoRemoveinDB",String.valueOf(resId));
                                dbHelper.deletePersonRecord(resId,getActivity());
                                Dataset.remove(idk);
                                Log.d("totalitems",String.valueOf(dbHelper.getallCount()));
                                adapter.notifyItemRemoved(idk);
                                //adapter.notifyDataSetChanged();
//                               Log.d("totalitems",String.valueOf(dbHelper.getSubjectsCount()));
//                               mAdapter.notifyItemRangeChanged(0,dbHelper.getSubjectsCount());

                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                adapter.notifyDataSetChanged();
                            }
                        });
                // Create the AlertDialog object and return it
                builder.create().show();
            }
        });
        helper.attachToRecyclerView(mRecyclerView);
        return rootView;
    }
    private void setAdapter() {
        adapter = new PersonAdapter(Dataset,getActivity(), mRecyclerView);
        mRecyclerView.setAdapter(adapter);
        Log.d("totalEntries",String.valueOf(dbHelper.getallCount()));
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }
}
