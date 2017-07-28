package com.example.victor.mertial_test.activities.setting;

import android.content.SharedPreferences;
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
 * Created by Phoebe on 15/10/11.
 */
public class ShareActivity extends ActionBarActivity {

    private List<Share> persons;
    private RecyclerView share_rv;
    private Toolbar toolbar;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        //----------------------------toolbar-------------------------
        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //---------------------Recycle list----------------------------
        share_rv=(RecyclerView)findViewById(R.id.share_rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        share_rv.setLayoutManager(llm);
        share_rv.setHasFixedSize(true);
        initializeData();
        initializeAdapter();
    }

    private void initializeData(){
//        persons = new ArrayList<>();
//        format : String bmonitor_name,int photoId
//        persons.add(new Share("H-C-2010-06-01", R.drawable.bindingwd_icon));
//        persons.add(new Share("jdsC-血壓計", R.drawable.bindingwd_icon));
//        persons.add(new Share("Phoebe's 計步器", R.drawable.bindingwd_icon));
        persons = new ArrayList<>();
        SharedPreferences pref2 = getSharedPreferences("wecare_member", MODE_PRIVATE);
        final String wecare_account=pref2.getString("account", null);
//        thread = new Thread(new setequip(wecare_account));
//        thread.start();
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Equipment");
        try {
            query.whereEqualTo("account", wecare_account);
            List<ParseObject> pList = query.find();
            int count = query.count();
            for(int i=0;i<count;i++){
                persons.add(new Share(String.valueOf(pList.get(i).getString("equi_name")), R.drawable.bindingwd_icon));
            }
        }catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void initializeAdapter(){
        ShareAdapter adapter = new ShareAdapter(persons);
        share_rv.setAdapter(adapter);
    }

//    private class setequip implements Runnable {
//        String account;
//        public setequip(String account) {
//            this.account=account;
//        }
//        @Override
//        public void run() {
//            final ParseQuery<ParseObject> query = ParseQuery.getQuery("Equipment");
//            try {
//                query.whereEqualTo("account", account);
//                List<ParseObject> pList = query.find();
//                int count = query.count();
//                for(int i=0;i<count;i++){
//                    persons.add(new Share(String.valueOf(pList.get(i).getString("equi_name")), R.drawable.bindingwd_icon));
//                }
//            }catch (ParseException e) {
//                e.printStackTrace();
//            }
//        };
//    }

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
