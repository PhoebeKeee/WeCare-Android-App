package com.example.victor.mertial_test.activities.myMessage;

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
 * Created by Phoebe on 15/9/18.
 */
public class MessageAdapter  extends RecyclerView.Adapter<MessageAdapter.MsgViewHolder> {


    public static class MsgViewHolder extends RecyclerView.ViewHolder {

        CardView ccvv; //cardview
        TextView messName; //K.Phoebe
        TextView messInfo; //血壓過高
        TextView messDetail; //170mmHg
        TextView messTime; //上午9:07
        ImageView messPhoto; //Info相關圖片

        MsgViewHolder(View itemView) {
            super(itemView);
            ccvv = (CardView)itemView.findViewById(R.id.ccvv);

            messName = (TextView)itemView.findViewById(R.id.messName);
            messInfo = (TextView)itemView.findViewById(R.id.messInfo);
            messDetail =(TextView)itemView.findViewById(R.id.messDetail);
            messTime =(TextView)itemView.findViewById(R.id.messTime);
            messPhoto = (ImageView)itemView.findViewById(R.id.messPhoto);
        }
    }

    List<Msg> persons;

    MessageAdapter(List<Msg> persons){
        this.persons = persons;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public MsgViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_item, viewGroup, false);
        MsgViewHolder mvh = new MsgViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MsgViewHolder msgViewHolder, int i) {
        msgViewHolder.messName.setText(persons.get(i).name);
        msgViewHolder.messInfo.setText(persons.get(i).info);
        msgViewHolder.messDetail.setText(persons.get(i).detail);
        msgViewHolder.messTime.setText(persons.get(i).time);
        msgViewHolder.messPhoto.setImageResource(persons.get(i).photoId);
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

}
