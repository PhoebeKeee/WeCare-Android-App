package com.example.victor.mertial_test.activities.member;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.victor.mertial_test.R;
import com.example.victor.mertial_test.activities.Guidingpage_WearableDevice_Binding.BindingWdActivity_1;
import com.example.victor.mertial_test.activities.myCare.HomeActivity;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class login extends Activity {


    EditText email;
    EditText password;
    Button login;
    Button forget;
    Button newmember;
    private Thread thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //if islog than change page
        SharedPreferences pref4 = getSharedPreferences("wecare_member",MODE_PRIVATE);
        Boolean islog=pref4.getBoolean("isLogged",false);
        if(islog==true){
            Intent intent4 =new Intent();
            intent4.setClass(login.this,HomeActivity.class);
            startActivity(intent4);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //recode Parse connect is already
        SharedPreferences pref=getSharedPreferences("parse_start",MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        editor.putBoolean("isRun", true);
        editor.commit();
        //connect view
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        login=(Button)findViewById(R.id.login);
        forget=(Button)findViewById(R.id.forget);
        //click newmember than change page
        newmember=(Button)findViewById(R.id.newmember);
        newmember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(login.this,newmember.class);
                startActivity(intent);
            }
        });
        //click login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().trim().equals("") == true) {
                    Toast.makeText(login.this, "提示：請輸入信箱", Toast.LENGTH_SHORT).show();
                } else if (email.getText().toString().trim().indexOf("@") == -1) {
                    Toast.makeText(login.this, "提示：信箱格式錯誤", Toast.LENGTH_SHORT).show();
                } else if (password.getText().toString().trim().equals("") == true) {
                    Toast.makeText(login.this, "提示：請輸入密碼", Toast.LENGTH_SHORT).show();
                }else {
                    thread = new Thread(new check_member_data(email.getText().toString(),password.getText().toString()));
                    thread.start();
//                    final ParseQuery<ParseObject> query = ParseQuery.getQuery("Member");
//                    String objectID = null;
//                    String Email=String.valueOf(email.getText());
//                    String Password=String.valueOf(password.getText());
//                    try {
//                        //get PK from member
//                        List<ParseObject> pList = query.find();
//                        for(int i =0;i<pList.size();i++){
//                            String DBemail=pList.get(i).getString("email");
//                            String DBpassword=pList.get(i).getString("password");
//                            if(DBemail.equals(Email)&&DBpassword.equals(Password)) {
//                                objectID = pList.get(i).getObjectId();
//                            }
//                        }
//                    } catch (com.parse.ParseException e1) {
//                        e1.printStackTrace();
//                    }
//                    query.getInBackground(objectID, new GetCallback<ParseObject>() {
//                        public void done(ParseObject Member, com.parse.ParseException e) {
//                            if (e == null) {
//                                //SharedPreferences
//                                SharedPreferences pref = getSharedPreferences("wecare_member", MODE_PRIVATE);
//                                SharedPreferences.Editor editor = pref.edit();
//                                String Email = String.valueOf(email.getText());
//                                String Account =Member.getString("account");
//                                editor.putString("email", Email);
//                                editor.putString("account", Account);
//                                editor.putBoolean("isLogged", true);
//                                editor.commit();
//                                //change page
//                                Intent intent = new Intent();
//                                intent.setClass(login.this, HomeActivity.class);
//                                startActivity(intent);
//                            }else{
//                                Toast.makeText(login.this, "提示：信箱和密碼不符", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
                }
            }
        });
        //change page to fotget password
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent();
                intent2.setClass(login.this, forgetpassword.class);
                startActivity(intent2);
            }
        });

    }

    private class check_member_data implements Runnable {
        String mail,password;
        public check_member_data(String mail,String password) {
            this.mail = mail;
            this.password=password;
        }
        @Override
        public void run() {
            final ParseQuery<ParseObject> query = ParseQuery.getQuery("Member");
            String objectID = null;
            try {
                //get PK from member
                List<ParseObject> pList = query.find();
                for(int i =0;i<pList.size();i++){
                    String DBemail=pList.get(i).getString("email");
                    String DBpassword=pList.get(i).getString("password");
                    if(DBemail.equals(mail)&&DBpassword.equals(password)) {
                        objectID = pList.get(i).getObjectId();
                    }
                }
            } catch (com.parse.ParseException e1) {
                e1.printStackTrace();
            }
            query.getInBackground(objectID, new GetCallback<ParseObject>() {
                public void done(ParseObject Member, com.parse.ParseException e) {
                    if (e == null) {
                        //SharedPreferences
                        SharedPreferences pref = getSharedPreferences("wecare_member", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        String Account =Member.getString("account");
                        editor.putString("email", mail);
                        editor.putString("account", Account);
                        editor.putBoolean("isLogged", true);
                        editor.commit();
                        //change page
                        Intent intent = new Intent();
                        intent.setClass(login.this, BindingWdActivity_1.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(login.this, "提示：信箱和密碼不符", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        };
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
    //reset the back button if it is necessary
    @Override
    public void onBackPressed() {
        SharedPreferences pref3 = getSharedPreferences("backbutton", MODE_PRIVATE);
        Boolean ban=pref3.getBoolean("inhibit_return", false);
        if (ban == true) {
            new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("警告 !").setMessage("抱歉，您不能回前一頁").setNegativeButton("確認", null).show();
        }else{
            super.onBackPressed();
        }
    }
}