package com.example.victor.mertial_test.activities.myMessage;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.victor.mertial_test.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phoebe on 15/9/18.
 */
public class MessageActivity extends ActionBarActivity {
    private List<Msg> persons;
    private RecyclerView msgRecycler;

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        //----------------------------toolbar-------------------------
        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //---------------------Recycle list----------------------------
        msgRecycler=(RecyclerView)findViewById(R.id.msgRecycler);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        msgRecycler.setLayoutManager(llm);
        msgRecycler.setHasFixedSize(true);

        initializeData();
        initializeAdapter();
    }

    private void initializeData(){
        persons = new ArrayList<>();
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Member");
        final ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Digital");
        final ParseQuery<ParseObject> query3 = ParseQuery.getQuery("Digital");

        try {
            List<ParseObject> pList = query.find();
            query2.whereEqualTo("func_name", "a");
            query2.whereLessThan("data", "50");
            query3.whereEqualTo("func_name", "b");
            query3.whereLessThan("data", "4001");
            List<ParseObject> pList2 = query2.find();
            List<ParseObject> pList3 = query3.find();

            int k = query2.count();
            int y = query3.count();
            ArrayList a = new ArrayList();
            ArrayList b = new ArrayList();
            ArrayList e = new ArrayList();

            for (int s = 0; s < k; s++) {
                String c = pList2.get(s).getString("account");
                String data = pList2.get(s).getString("data") + "BPM";
                a.add(c);
                b.add(data);
                e.add("血壓不足");
            }
            for(int s =0;s<y;s++) {
                String d = pList3.get(s).getString("account");
                String data2 = pList3.get(s).getString("data") + "步";
                a.add(d);
                b.add(data2);
                e.add("運動量不足");
            }


            for(int i=0;i<k+y;i++){
                persons.add(new Msg((String) a.get(i), (String) e.get(i),(String) b.get(i),String.valueOf(pList.get(i).getCreatedAt()).substring(0, 19), R.drawable.ic_tab_2));

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        //format : String name, String info,String detail, String time, int photoId


    }

    private void initializeAdapter(){
        MessageAdapter adapter = new MessageAdapter(persons);
        msgRecycler.setAdapter(adapter);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on t`    1
        // \he Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        if(id == android.R.id.home){
            finish();
            return true;
        }



        return super.onOptionsItemSelected(item);
    }
}
