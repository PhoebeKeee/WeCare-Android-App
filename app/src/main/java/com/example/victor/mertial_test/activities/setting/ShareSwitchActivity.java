package com.example.victor.mertial_test.activities.setting;

import android.content.Intent;
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
public class ShareSwitchActivity extends ActionBarActivity {

    private List<ShareSwitch> persons;
    private RecyclerView shareswitch_rv;

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shareswitch);


        //----------------------------toolbar-------------------------
        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //---------------------Recycle list----------------------------
        shareswitch_rv=(RecyclerView)findViewById(R.id.shareswitch_rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        shareswitch_rv.setLayoutManager(llm);
        shareswitch_rv.setHasFixedSize(true);

        initializeData();
        initializeAdapter();
    }

    private void initializeData(){
        persons = new ArrayList<>();

        Intent intent = getIntent();
        String equipment = intent.getStringExtra("equipment");
        SharedPreferences pref2 = getSharedPreferences("wecare_member", MODE_PRIVATE);
        final String wecare_account=pref2.getString("account", null);
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Function");
        String drawable="R.drawable.heart_clip";
        try {
            query.whereEqualTo("account", wecare_account);
            query.whereEqualTo("equi_name", equipment);
            List<ParseObject> pList = query.find();
            int count = query.count();
            for(int i=0;i<count;i++){
                if(pList.get(i).getString("func_name").equals("血壓")){
                    persons.add(new ShareSwitch(String.valueOf(pList.get(i).getString("func_name")), R.drawable.heart_clip,String.valueOf(pList.get(i).getString("share")),wecare_account,equipment));
                }else if(pList.get(i).getString("func_name").equals("步行數")){
                    persons.add(new ShareSwitch(String.valueOf(pList.get(i).getString("func_name")), R.drawable.walk,String.valueOf(pList.get(i).getString("share")),wecare_account,equipment));
                }else if(pList.get(i).getString("func_name").equals("心跳")){
                    persons.add(new ShareSwitch(String.valueOf(pList.get(i).getString("func_name")), R.drawable.heart_clip,String.valueOf(pList.get(i).getString("share")),wecare_account,equipment));
    }
}
}catch (ParseException e) {
        e.printStackTrace();
        }
    }

    private void initializeAdapter(){
        ShareSwitchAdapter adapter = new ShareSwitchAdapter(persons);
        shareswitch_rv.setAdapter(adapter);
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