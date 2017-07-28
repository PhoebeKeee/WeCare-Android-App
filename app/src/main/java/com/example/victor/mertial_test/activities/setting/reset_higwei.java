package com.example.victor.mertial_test.activities.setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.victor.mertial_test.R;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class reset_higwei extends ActionBarActivity {

    private Toolbar toolbar;
    EditText reset_height;
    EditText reset_weight;
    Button reset;
    private Thread thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_higwei);
        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        reset_height=(EditText)findViewById(R.id.reset_height);
        reset_weight=(EditText)findViewById(R.id.reset_weight);
        reset=(Button)findViewById(R.id.reset);
        SharedPreferences pref = getSharedPreferences("wecare_member", MODE_PRIVATE);
        final String account=pref.getString("account", null);
        final String email=pref.getString("email",null);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((reset_height.getText().toString().trim().equals("") == true)&&(reset_weight.getText().toString().trim().equals("") == true)) {
                    Toast.makeText(reset_higwei.this, "提示：請至少輸入一個欄位", Toast.LENGTH_SHORT).show();
                } else if (reset_height.getText().toString().trim().equals("") == true) {
                    thread = new Thread(new setweight(account,email));
                    thread.start();
//                    final ParseQuery<ParseObject> query = ParseQuery.getQuery("Member");
//                    String objectID = null;
//                    try {
//                        List<ParseObject> pList = query.find();
//                        for(int i =0;i<pList.size();i++){
//                            String DBemail=pList.get(i).getString("email");
//                            String DBaccount=pList.get(i).getString("account");
//                            if(DBemail.equals(email)&&DBaccount.equals(account)) {
//                                objectID = pList.get(i).getObjectId();
//                            }
//                        }
//                    } catch (com.parse.ParseException e1) {
//                        e1.printStackTrace();
//                    }
//                    query.getInBackground(objectID, new GetCallback<ParseObject>() {
//                        public void done(ParseObject Member, com.parse.ParseException e) {
//                            if (e == null) {
//                                //reset weight
//                                Member.put("weight", String.valueOf(reset_weight.getText()));
//                                Member.saveInBackground();
//                                //success massege
//                                Toast.makeText(reset_higwei.this, "體重重設成功 !", Toast.LENGTH_SHORT).show();
//                                //change page
//                                Intent intent2 = new Intent();
//                                intent2.setClass(reset_higwei.this, SettingActivity.class);
//                                startActivity(intent2);
//                            } else {
//                                Toast.makeText(reset_higwei.this, "體重重設失敗 !", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
                }else if (reset_weight.getText().toString().trim().equals("") == true) {
                    thread = new Thread(new setheight(account,email));
                    thread.start();
//                    final ParseQuery<ParseObject> query = ParseQuery.getQuery("Member");
//                    String objectID = null;
//                    try {
//                        List<ParseObject> pList = query.find();
//                        for(int i =0;i<pList.size();i++){
//                            String DBemail=pList.get(i).getString("email");
//                            String DBaccount=pList.get(i).getString("account");
//                            if(DBemail.equals(email)&&DBaccount.equals(account)) {
//                                objectID = pList.get(i).getObjectId();
//                            }
//                        }
//                    } catch (com.parse.ParseException e1) {
//                        e1.printStackTrace();
//                    }
//                    query.getInBackground(objectID, new GetCallback<ParseObject>() {
//                        public void done(ParseObject Member, com.parse.ParseException e) {
//                            if (e == null) {
//                                //reset height
//                                Member.put("height", String.valueOf(reset_height.getText()));
//                                Member.saveInBackground();
//                                //success massege
//                                Toast.makeText(reset_higwei.this, "身高重設成功 !", Toast.LENGTH_SHORT).show();
//                                //change page
//                                Intent intent2 = new Intent();
//                                intent2.setClass(reset_higwei.this, SettingActivity.class);
//                                startActivity(intent2);
//                            } else {
//                                Toast.makeText(reset_higwei.this, "身高重設失敗 !", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
                }else{
                    thread = new Thread(new setboth(account,email));
                    thread.start();
//                    final ParseQuery<ParseObject> query = ParseQuery.getQuery("Member");
//                    String objectID = null;
//                    try {
//                        List<ParseObject> pList = query.find();
//                        for(int i =0;i<pList.size();i++){
//                            String DBemail=pList.get(i).getString("email");
//                            String DBaccount=pList.get(i).getString("account");
//                            if(DBemail.equals(email)&&DBaccount.equals(account)) {
//                                objectID = pList.get(i).getObjectId();
//                            }
//                        }
//                    } catch (com.parse.ParseException e1) {
//                        e1.printStackTrace();
//                    }
//                    query.getInBackground(objectID, new GetCallback<ParseObject>() {
//                        public void done(ParseObject Member, com.parse.ParseException e) {
//                            if (e == null) {
//                                //reset height&weight
//                                Member.put("height", String.valueOf(reset_height.getText()));
//                                Member.put("weight", String.valueOf(reset_weight.getText()));
//                                Member.saveInBackground();
//                                //success massege
//                                Toast.makeText(reset_higwei.this, "身高和體重重設成功 !", Toast.LENGTH_SHORT).show();
//                                //change page
//                                Intent intent2 = new Intent();
//                                intent2.setClass(reset_higwei.this, SettingActivity.class);
//                                startActivity(intent2);
//                            } else {
//                                Toast.makeText(reset_higwei.this, "身高和體重重設失敗 !", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
                }
            }
        });
    }

    private class setweight implements Runnable {
        String account,mail;
        public setweight(String account,String mail) {
            this.account=account;
            this.mail = mail;
        }
        @Override
        public void run() {
            final ParseQuery<ParseObject> query = ParseQuery.getQuery("Member");
            String objectID = null;
            try {
                List<ParseObject> pList = query.find();
                for(int i =0;i<pList.size();i++){
                    String DBemail=pList.get(i).getString("email");
                    String DBaccount=pList.get(i).getString("account");
                    if(DBemail.equals(mail)&&DBaccount.equals(account)) {
                        objectID = pList.get(i).getObjectId();
                    }
                }
            } catch (com.parse.ParseException e1) {
                e1.printStackTrace();
            }
            query.getInBackground(objectID, new GetCallback<ParseObject>() {
                public void done(ParseObject Member, com.parse.ParseException e) {
                    if (e == null) {
                        //reset weight
                        Member.put("weight", String.valueOf(reset_weight.getText()));
                        Member.saveInBackground();
                        //success massege
                        Toast.makeText(reset_higwei.this, "體重重設成功 !", Toast.LENGTH_SHORT).show();
                        //change page
                        Intent intent2 = new Intent();
                        intent2.setClass(reset_higwei.this, SettingActivity.class);
                        startActivity(intent2);
                    } else {
                        Toast.makeText(reset_higwei.this, "體重重設失敗 !", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        };
    }

    private class setheight implements Runnable {
        String account,mail;
        public setheight(String account,String mail) {
            this.account=account;
            this.mail = mail;
        }
        @Override
        public void run() {
            final ParseQuery<ParseObject> query = ParseQuery.getQuery("Member");
            String objectID = null;
            try {
                List<ParseObject> pList = query.find();
                for(int i =0;i<pList.size();i++){
                    String DBemail=pList.get(i).getString("email");
                    String DBaccount=pList.get(i).getString("account");
                    if(DBemail.equals(mail)&&DBaccount.equals(account)) {
                        objectID = pList.get(i).getObjectId();
                    }
                }
            } catch (com.parse.ParseException e1) {
                e1.printStackTrace();
            }
            query.getInBackground(objectID, new GetCallback<ParseObject>() {
                public void done(ParseObject Member, com.parse.ParseException e) {
                    if (e == null) {
                        //reset height
                        Member.put("height", String.valueOf(reset_height.getText()));
                        Member.saveInBackground();
                        //success massege
                        Toast.makeText(reset_higwei.this, "身高重設成功 !", Toast.LENGTH_SHORT).show();
                        //change page
                        Intent intent2 = new Intent();
                        intent2.setClass(reset_higwei.this, SettingActivity.class);
                        startActivity(intent2);
                    } else {
                        Toast.makeText(reset_higwei.this, "身高重設失敗 !", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        };
    }
    private class setboth implements Runnable {
        String account,mail;
        public setboth(String account,String mail) {
            this.account=account;
            this.mail = mail;
        }
        @Override
        public void run() {
            final ParseQuery<ParseObject> query = ParseQuery.getQuery("Member");
            String objectID = null;
            try {
                List<ParseObject> pList = query.find();
                for(int i =0;i<pList.size();i++){
                    String DBemail=pList.get(i).getString("email");
                    String DBaccount=pList.get(i).getString("account");
                    if(DBemail.equals(mail)&&DBaccount.equals(account)) {
                        objectID = pList.get(i).getObjectId();
                    }
                }
            } catch (com.parse.ParseException e1) {
                e1.printStackTrace();
            }
            query.getInBackground(objectID, new GetCallback<ParseObject>() {
                public void done(ParseObject Member, com.parse.ParseException e) {
                    if (e == null) {
                        //reset height&weight
                        Member.put("height", String.valueOf(reset_height.getText()));
                        Member.put("weight", String.valueOf(reset_weight.getText()));
                        Member.saveInBackground();
                        //success massege
                        Toast.makeText(reset_higwei.this, "身高和體重重設成功 !", Toast.LENGTH_SHORT).show();
                        //change page
                        Intent intent2 = new Intent();
                        intent2.setClass(reset_higwei.this, SettingActivity.class);
                        startActivity(intent2);
                    } else {
                        Toast.makeText(reset_higwei.this, "身高和體重重設失敗 !", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reset_higwei, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
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
