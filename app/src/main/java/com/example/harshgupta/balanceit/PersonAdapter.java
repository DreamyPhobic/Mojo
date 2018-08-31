package com.example.harshgupta.balanceit;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by Harsh Gupta on 04-Mar-18.
 */


public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {
    private List<Person> mPersonList;
    private Context mContext;
    private RecyclerView mRecyclerV;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView PersonNameTxtV;
        TextView PersonTotalTxtV;
        ImageView PersonImageImgV;
        public View layout;

        ViewHolder(View v) {
            super(v);
            layout = v;
            PersonNameTxtV = (TextView) v.findViewById(R.id.tv_personName);
            PersonImageImgV = (ImageView) v.findViewById(R.id.i_personIcon);
            PersonTotalTxtV=(TextView) v.findViewById(R.id.tv_totalCount);
        }

    }

    public void add(int position, Person Person) {
        mPersonList.add(position, Person);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        mPersonList.remove(position);
        notifyItemRemoved(position);
    }



    // Provide a suitable constructor (depends on the kind of dataset)
    PersonAdapter(List<Person> myDataset, Context context, RecyclerView recyclerView) {
        mPersonList = myDataset;
        mContext = context;
        mRecyclerV = recyclerView;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PersonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.person_card, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
///data/user/0/com.example.harshgupta.dontmissaclass/cache
        final Person Person = mPersonList.get(position);
        holder.PersonNameTxtV.setText(Person.getName());
        holder.PersonTotalTxtV.setText(String.valueOf(Person.getTotal()));
        if(!Person.getImage().isEmpty()){
            File file = new File("/storage/emulated/0/DMC_Images"+Person.getName());
            if(!file.exists()){
                Picasso.with(mContext).load(Person.getImage()).placeholder(R.mipmap.ic_launcher).into(holder.PersonImageImgV);
            }
            else
                Picasso.with(mContext).load(file).placeholder(R.mipmap.ic_launcher).into(holder.PersonImageImgV);
        }
        //listen to single view layout click
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToDetail = new Intent(mContext, DetailActivity.class);
                goToDetail.putExtra("USER_ID",Person.getID());
                mContext.startActivity(goToDetail);
            }
        });


    }
    // Return the size of your dataset (invoked by the layout manCreditr)
    @Override
    public int getItemCount() {
        return mPersonList.size();
    }



}
