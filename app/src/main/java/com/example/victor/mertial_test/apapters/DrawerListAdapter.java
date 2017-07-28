package com.example.victor.mertial_test.apapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.victor.mertial_test.pojo.Information;
import com.example.victor.mertial_test.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by Victor on 2015/9/10.
 */
public class DrawerListAdapter extends RecyclerView.Adapter<DrawerListAdapter.MyViewHolder>{


    private LayoutInflater inflater;
    List<Information> data = Collections.emptyList();
    private Context context;


    public DrawerListAdapter(Context context,List<Information> data){
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }
    @Override
    public DrawerListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = inflater.inflate(R.layout.drawer_list_row,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        Log.i("TAG", "onCreateHolder called");
        return holder;
    }

    public void delete(int position){
        data.remove(position);
        notifyItemRemoved(position);
    }



    @Override
    public void onBindViewHolder(DrawerListAdapter.MyViewHolder viewHolder, final int position) {
        Information current = data.get(position);
        Log.i("TAG", "onBindViewHolder called " + position);
        viewHolder.title.setText(current.title);
        viewHolder.icon.setImageResource(current.iconId);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        ImageView icon;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.drawerlistTextId);
            icon = (ImageView)itemView.findViewById(R.id.drawerlistIconId);
            icon.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }


}
