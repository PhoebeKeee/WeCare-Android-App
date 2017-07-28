package com.example.victor.mertial_test.activities.setting;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.victor.mertial_test.R;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by Phoebe on 15/10/11.
 */
public class ShareSwitchAdapter extends RecyclerView.Adapter<ShareSwitchAdapter.ShareSwitchViewHolder> {

    public static class ShareSwitchViewHolder extends RecyclerView.ViewHolder {

        CardView shareswitch_cv; //recycle list
        TextView function_name; //心跳
        ImageView function_icon; //心跳_icon
        Switch switch_btn;//switch_on,switch_off

        ShareSwitchViewHolder(View itemView) {
            super(itemView);
            shareswitch_cv = (CardView)itemView.findViewById(R.id.shareswitch_cv);
            function_name = (TextView)itemView.findViewById(R.id.function_name);
            function_icon = (ImageView)itemView.findViewById(R.id.function_icon);
            switch_btn = (Switch)itemView.findViewById(R.id.switch_btn);
        }
    }

    List<ShareSwitch> persons;

    ShareSwitchAdapter(List<ShareSwitch> persons){
        this.persons = persons;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ShareSwitchViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shareswitch_item, viewGroup, false);
        ShareSwitchViewHolder shsvh = new ShareSwitchViewHolder(v);
        return shsvh;
    }

    @Override
    public void onBindViewHolder( ShareSwitchViewHolder shareswitchViewHolder, int i) {
        shareswitchViewHolder.function_name.setText(persons.get(i).sfunction_name);
        shareswitchViewHolder.function_icon.setImageResource(persons.get(i).sfunction_icon);
        final String account=persons.get(i).account;
        final String equi_name=persons.get(i).equi_name;
        final String func_name=persons.get(i).sfunction_name;
        if(persons.get(i).sswitch_btn.equals("Y")){
            shareswitchViewHolder.switch_btn.setChecked(true);
        }else{
            shareswitchViewHolder.switch_btn.setChecked(false);
        }
        shareswitchViewHolder.switch_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                final ParseQuery<ParseObject> query = ParseQuery.getQuery("Function");
                String objectID = null;
                try {
                    List<ParseObject> pList = query.find();
                    for (int i = 0; i < pList.size(); i++) {
                        String DBaccount = pList.get(i).getString("account");
                        String DBequi_name = pList.get(i).getString("equi_name");
                        String DBfunc_name = pList.get(i).getString("func_name");
                        if (DBaccount.equals(account) && DBequi_name.equals(equi_name) && DBfunc_name.equals(func_name)) {
                            objectID = pList.get(i).getObjectId();
                        }
                    }
                } catch (com.parse.ParseException e1) {
                    e1.printStackTrace();
                }
                if (isChecked) {
                    query.getInBackground(objectID, new GetCallback<ParseObject>() {
                        public void done(ParseObject Member, com.parse.ParseException e) {
                            if (e == null) {
                                Member.put("share", "Y");
                                Member.saveInBackground();
                            }
                        }
                    });
                } else {
                    query.getInBackground(objectID, new GetCallback<ParseObject>() {
                        public void done(ParseObject Member, com.parse.ParseException e) {
                            if (e == null) {
                                Member.put("share", "N");
                                Member.saveInBackground();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }
}