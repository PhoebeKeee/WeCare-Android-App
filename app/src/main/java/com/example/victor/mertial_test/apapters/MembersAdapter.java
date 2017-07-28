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
 * Created by Victorlin on 2015/9/23.
 */
public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.ViewHolderMember>{

    private List<String> listMembers = Collections.emptyList();
    private LayoutInflater layoutInflater;
    private Context context;

    public MembersAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    public void setMemberList(ArrayList<String> listCharacters){
        this.listMembers = listCharacters;
        notifyItemRangeChanged(0, listCharacters.size());
    }

    @Override
    public MembersAdapter.ViewHolderMember onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_member_item,parent,false);
        ViewHolderMember viewHolderMember = new ViewHolderMember(view);
        return viewHolderMember;
    }

    @Override
    public void onBindViewHolder(MembersAdapter.ViewHolderMember holder, int position) {
        String currentCharacter = listMembers.get(position);
        holder.member_image.setImageResource(R.drawable.testall);
        holder.member_text.setText(currentCharacter);
    }

    @Override
    public int getItemCount() {
        return listMembers.size();
    }
    static class ViewHolderMember extends RecyclerView.ViewHolder{

        private ImageView member_image;
        private TextView member_text;

        public ViewHolderMember(View itemView) {
            super(itemView);
            member_image = (ImageView)itemView.findViewById(R.id.member_image);
            member_text = (TextView)itemView.findViewById(R.id.member_name);
        }
    }
}
