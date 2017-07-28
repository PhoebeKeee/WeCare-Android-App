package com.example.victor.mertial_test.activities.familymember;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ipt810105 on 2015/10/22.
 */
public class FamilyAdapter extends RecyclerView.Adapter<FamilyAdapter.PersonViewHolder>{

    List<member> member = Collections.emptyList();

    public static class PersonViewHolder extends RecyclerView.ViewHolder{

        CardView cv; //recycle list
        TextView textName; //K.Phoebe
        CircleImageView textPhoto; //user photo
        Switch gogo;
        TextView familyname;
        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            textName = (TextView)itemView.findViewById(R.id.personalName);
            textPhoto = (CircleImageView)itemView.findViewById(R.id.personalPhoto);
            gogo=(Switch)itemView.findViewById(R.id.gogo);
            familyname=(TextView)itemView.findViewById(R.id.familyname);

//            itemView.setOnClickListener(new View.OnClickListener(){
//           @Override
//           public void onClick(final View view) {
//              Intent myIntent = new Intent(view.getContext(),Healthreport.class);
//              myIntent.putExtra("familyname",familyname.getText().toString());
//              view.getContext().startActivity(myIntent);
//            }});

        }

    }




    FamilyAdapter(List<member> member){
        this.member = member;
    }

    public void sethomeList(ArrayList<member> listmember){
        this.member = listmember;
        notifyItemRangeChanged(0, member.size());
    }


    public void setName(String name,int position){
        member.get(position).setNickname(name);
        notifyItemRangeChanged(0, member.size());
    };


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item2, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, final int i) {
        personViewHolder.textName.setText(member.get(i).getNickname());
        personViewHolder.textPhoto.setImageResource(member.get(i).photoId);
        final String family=member.get(i).name.toString();
        final String account=member.get(i).account.toString();
        personViewHolder.familyname.setText(member.get(i).name);
        if(member.get(i).pin.equals("Y")){
            personViewHolder.gogo.setChecked(true);
        }else{
            personViewHolder.gogo.setChecked(false);
        }
        personViewHolder.gogo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                final ParseQuery<ParseObject> query = ParseQuery.getQuery("Relation");
                String objectID = null;
                try {
                    List<ParseObject> pList = query.find();
                    for (int i = 0; i < pList.size(); i++) {
                        String DBaccount = pList.get(i).getString("account");
                        String DBfamilyname = pList.get(i).getString("familymember");
                        if (DBaccount.equals(account) && DBfamilyname.equals(family)) {
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
                                Member.put("pin", "Y");
                                Member.saveInBackground();
                            }
                        }
                    });
                } else {
                    query.getInBackground(objectID, new GetCallback<ParseObject>() {
                        public void done(ParseObject Member, com.parse.ParseException e) {
                            if (e == null) {
                                Member.put("pin", "N");
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
        return member.size();
    }




}
