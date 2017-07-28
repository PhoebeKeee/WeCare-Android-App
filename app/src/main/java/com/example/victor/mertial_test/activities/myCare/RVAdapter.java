package com.example.victor.mertial_test.activities.myCare;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.victor.mertial_test.R;
import com.example.victor.mertial_test.activities.Message.message;

import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Phoebe on 15/9/14.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder> {
//    ImageView photo;

    //    ImageView textPhoto ;
//
    private static final String SERVER_ADDRESS="http://wecarememberpic.comze.com/";
    public class PersonViewHolder extends RecyclerView.ViewHolder {

        CardView cv; //recycle list
        TextView textName; //K.Phoebe
        TextView textTime; //上午9:07
        CircleImageView textPhoto ;
        TextView messageaccount;
        TextView message;
        //user photo


        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            messageaccount=(TextView)itemView.findViewById(R.id.messageaccount);
            textName = (TextView)itemView.findViewById(R.id.personalName);
            textTime =(TextView)itemView.findViewById(R.id.personalTime);
            textPhoto=(CircleImageView)itemView.findViewById(R.id.personalPhoto);
            message = (TextView)itemView.findViewById(R.id.newmessage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    Intent myIntent = new Intent(view.getContext(),message.class);
                    myIntent.putExtra("familyname",messageaccount.getText().toString());
                    view.getContext().startActivity(myIntent);
                }});
        }
    }



    List<Person> persons = Collections.emptyList();

    RVAdapter(List<Person> persons){
        this.persons = persons;
    }

    public void sethomeList(ArrayList<Person> listPerson){
        this.persons = listPerson;

        notifyItemRangeChanged(0, persons.size());
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }
    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.textName.setText(persons.get(i).name);
        personViewHolder.textTime.setText(persons.get(i).time);
        personViewHolder.textPhoto.setImageResource(persons.get(i).photo);
        personViewHolder.messageaccount.setText(persons.get(i).familymember);
        personViewHolder.message.setText(persons.get(i).message);
    }



    @Override
    public int getItemCount() {
        return persons.size();
    }
}