package com.example.victor.mertial_test.apapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.victor.mertial_test.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Victor on 2015/10/22.
 */
public class BindAdapter extends RecyclerView.Adapter<BindAdapter.ViewHolderBind>{

    private List<String> listBinds = Collections.emptyList();
    private LayoutInflater layoutInflater;
    private Context context;

    public BindAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    public void setBindList(ArrayList<String> listBinds){
        this.listBinds = listBinds;
        notifyItemRangeChanged(0, listBinds.size());
    }


    @Override
    public ViewHolderBind onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_device_item, parent, false);
        ViewHolderBind viewHolderBind = new ViewHolderBind(view);
        return viewHolderBind;
    }

    @Override
    public void onBindViewHolder(ViewHolderBind holder, int position) {
        String currentBind = listBinds.get(position);
        holder.bind_image.setImageResource(R.drawable.bindingwd_icon);
        holder.bind_text.setText(currentBind);
    }

    @Override
    public int getItemCount() {
        return listBinds.size();
    }


    public void delete(int position){
        listBinds.remove(position);
        notifyItemRemoved(position);
    }

    public void removeall(){
        listBinds.clear();
    }

    class ViewHolderBind extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView bind_image;
        private TextView bind_text;

        public ViewHolderBind(View itemView) {
            super(itemView);
            bind_image = (ImageView)itemView.findViewById(R.id.deivice_image);
            bind_text = (TextView)itemView.findViewById(R.id.device_name);
            bind_text.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            delete(getPosition());
        }
    }
}
