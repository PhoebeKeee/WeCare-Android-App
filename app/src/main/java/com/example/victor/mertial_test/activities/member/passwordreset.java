package com.example.victor.mertial_test.activities.member;

import android.app.Activity;
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
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;



public class passwordreset extends Activity {
    EditText newpassword,newpassword2;
    Button send;
    private Thread thread;
    String objectID = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwordreset);
        //recode parse is already connect
        SharedPreferences pref=getSharedPreferences("parse_start",MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        editor.putBoolean("isRun", true);
        editor.commit();
        //conect latout
        newpassword=(EditText)findViewById(R.id.newpassword);
        newpassword2=(EditText)findViewById(R.id.newpassword2);
        send=(Button)findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Newpassword = String.valueOf(newpassword.getText());
                String Newpassword2 = String.valueOf(newpassword2.getText());
                if (newpassword.getText().toString().trim().equals("") == true) {
                    Toast.makeText(passwordreset.this, "提示：請輸入一組新密碼", Toast.LENGTH_SHORT).show();
                } else if (newpassword2.getText().toString().trim().equals("") == true) {
                    Toast.makeText(passwordreset.this, "提示：請再次輸入新密碼", Toast.LENGTH_SHORT).show();
                } else if (Newpassword.equals(Newpassword2) != true) {
                    Toast.makeText(passwordreset.this, "提示：新密碼與確認新密碼不符", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences pref2 = getSharedPreferences("resetpassword", MODE_PRIVATE);
                    String email = pref2.getString("email", null);
                    String account = pref2.getString("account", null);
                    thread = new Thread(new password_reset(account,email,Newpassword,Newpassword2));
                    thread.start();
//                    final ParseQuery<ParseObject> query = ParseQuery.getQuery("Member");
//                    try {
//                        List<ParseObject> pList = query.find();
//                        for (int i = 0; i < pList.size(); i++) {
//                            String DBemail = pList.get(i).getString("email");
//                            String DBaccount = pList.get(i).getString("account");
//                            if (DBemail.equals(email) && DBaccount.equals(account)) {
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
//                                SharedPreferences pref3 = getSharedPreferences("backbutton", MODE_PRIVATE);
//                                SharedPreferences.Editor editor = pref3.edit();
//                                editor.putBoolean("inhibit_return", true);
//                                editor.commit();
//                                //reset password
//                                Member.put("password", String.valueOf(newpassword.getText()));
//                                Member.saveInBackground();
//                                //success massege
//                                Toast.makeText(passwordreset.this, "密碼重設成功 !", Toast.LENGTH_SHORT).show();
//                                //change page
//                                Intent intent2 = new Intent();
//                                intent2.setClass(passwordreset.this, login.class);
//                                startActivity(intent2);
//                            } else {
//                                Toast.makeText(passwordreset.this, "密碼重設錯誤 !", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
                }
            }
        });
    }

    private class password_reset implements Runnable {
        String account,mail,Newpassword,Newpassword2;
        public password_reset(String account,String mail,String Newpassword,String Newpassword2) {
            this.account=account;
            this.mail = mail;
            this.Newpassword=Newpassword;
            this.Newpassword2=Newpassword2;
        }
        @Override
        public void run() {
            final ParseQuery<ParseObject> query = ParseQuery.getQuery("Member");
            try {
                List<ParseObject> pList = query.find();
                for (int i = 0; i < pList.size(); i++) {
                    String DBemail = pList.get(i).getString("email");
                    String DBaccount = pList.get(i).getString("account");
                    if (DBemail.equals(mail) && DBaccount.equals(account)) {
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
                        SharedPreferences pref3 = getSharedPreferences("backbutton", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref3.edit();
                        editor.putBoolean("inhibit_return", true);
                        editor.commit();
                        //reset password
                        Member.put("password", String.valueOf(newpassword.getText()));
                        Member.saveInBackground();
                        //success massege
                        Toast.makeText(passwordreset.this, "密碼重設成功 !", Toast.LENGTH_SHORT).show();
                        //change page
                        Intent intent2 = new Intent();
                        intent2.setClass(passwordreset.this, login.class);
                        startActivity(intent2);
                    } else {
                        Toast.makeText(passwordreset.this, "密碼重設錯誤 !", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_passwordreset, menu);
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
}
