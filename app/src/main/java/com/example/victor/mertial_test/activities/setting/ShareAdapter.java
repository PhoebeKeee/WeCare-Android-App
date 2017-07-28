package com.example.victor.mertial_test.activities.setting;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.victor.mertial_test.R;

import java.util.List;

/**
 * Created by Phoebe on 15/10/11.
 */
public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ShareViewHolder> {

    public static class ShareViewHolder extends RecyclerView.ViewHolder {

        CardView share_cv; //recycle list
        TextView share_name; //血壓計
        ImageView share_icon; //binding_icon

        ShareViewHolder(View itemView) {
            super(itemView);
            share_cv = (CardView)itemView.findViewById(R.id.share_cv);

            share_name = (TextView)itemView.findViewById(R.id.share_name);
            share_icon = (ImageView)itemView.findViewById(R.id.share_icon);
            share_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳轉
                    Intent myIntent = new Intent(v.getContext(),ShareSwitchActivity.class);
                    myIntent.putExtra("equipment",share_name.getText().toString());
                    v.getContext().startActivity(myIntent);
                    //Intent intent = getIntent();
                    //String equipment = intent.getStringExtra("equipment");
                }
            });
        }
    }

    List<Share> persons;

    ShareAdapter(List<Share> persons){
        this.persons = persons;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ShareViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.share_item, viewGroup, false);
        ShareViewHolder shvh = new ShareViewHolder(v);
        return shvh;
    }

    @Override
    public void onBindViewHolder( ShareViewHolder shareViewHolder, int i) {
        shareViewHolder.share_name.setText(persons.get(i).sshare_name);
        shareViewHolder.share_icon.setImageResource(persons.get(i).photoId);
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }
}
