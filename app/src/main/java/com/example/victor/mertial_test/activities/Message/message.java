package com.example.victor.mertial_test.activities.Message;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.victor.mertial_test.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by ipt810105 on 2015/12/3.
 */
public class message extends ActionBarActivity {

    private Toolbar toolbar;
    private Thread thread;
    ListView ls;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ls = (ListView) findViewById(R.id.listView);
        //initialize();

        Intent intent = getIntent();
        String account = intent.getStringExtra("familyname");
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
        try {
            query.whereEqualTo("account", account);
            List<ParseObject> pList = query.find();
            int amount = query.count();
            String[] key = new String[amount];
            String[] time = new String[amount];
            for (int i = 0; i < amount; i++) {

                key[i] = pList.get(i).getString("message");
                time[i] = String.valueOf(pList.get(i).getCreatedAt()).substring(0, 19);

            }

            ls.setAdapter(new messageadapter(message.this, key,time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }



    private void initialize(){
        thread = new Thread(new check_member_data());
        thread.start();

    }
    private class check_member_data implements Runnable {
        public check_member_data() {

        }

        @Override
        public void run() {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
