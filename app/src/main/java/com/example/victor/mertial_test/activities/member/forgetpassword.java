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




public class forgetpassword extends Activity {
    EditText email;
    EditText account;
    Button send;
    private Thread thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //recode parse is already connect
        setContentView(R.layout.activity_forgetpassword);
        SharedPreferences pref=getSharedPreferences("parse_start",MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        editor.putBoolean("isRun", true);
        editor.commit();
        //check identity
        email=(EditText)findViewById(R.id.email);
        account=(EditText)findViewById(R.id.account);
        send=(Button)findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (account.getText().toString().trim().equals("") == true) {
                    Toast.makeText(forgetpassword.this, "提示：請輸入帳號", Toast.LENGTH_SHORT).show();
                } else if (email.getText().toString().trim().equals("") == true) {
                    Toast.makeText(forgetpassword.this, "提示：請輸入信箱", Toast.LENGTH_SHORT).show();
                } else if (email.getText().toString().trim().indexOf("@") == -1) {
                    Toast.makeText(forgetpassword.this, "提示：信箱格式錯誤", Toast.LENGTH_SHORT).show();
                } else {
                    thread = new Thread(new forget_password(account.getText().toString(),email.getText().toString()));
                    thread.start();
//                    final ParseQuery<ParseObject> query = ParseQuery.getQuery("Member");
//                    String objectID = null;
//                    String Email = String.valueOf(email.getText());
//                    String Account = String.valueOf(account.getText());
//                    try {
//                        List<ParseObject> pList = query.find();
//                        for (int i = 0; i < pList.size(); i++) {
//                            String DBemail = pList.get(i).getString("email");
//                            String DBaccount = pList.get(i).getString("account");
//                            if (DBemail.equals(Email) && DBaccount.equals(Account)) {
//                                SharedPreferences pref2 = getSharedPreferences("resetpassword", MODE_PRIVATE);
//                                SharedPreferences.Editor editor = pref2.edit();
//                                editor.putString("email", Email);
//                                editor.putString("account", Account);
//                                editor.commit();
//                                objectID = pList.get(i).getObjectId();
//                            }
//                        }
//                    } catch (com.parse.ParseException e1) {
//                        e1.printStackTrace();
//                    }
//                    query.getInBackground(objectID, new GetCallback<ParseObject>() {
//                        public void done(ParseObject Member, com.parse.ParseException e) {
//                            if (e == null) {
//                                //correct message
//                                Toast.makeText(forgetpassword.this, "提示：身分確認成功", Toast.LENGTH_LONG).show();
//                                //changepage
//                                Intent intent = new Intent();
//                                intent.setClass(forgetpassword.this, passwordreset.class);
//                                startActivity(intent);
//                            } else {
//                                Toast.makeText(forgetpassword.this, "提示：帳號和信箱不符", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
                }
            }
        });
    }

    private class forget_password implements Runnable {
        String account,mail;
        public forget_password(String account,String mail) {
            this.account=account;
            this.mail = mail;

        }
        @Override
        public void run() {
            final ParseQuery<ParseObject> query = ParseQuery.getQuery("Member");
            String objectID = null;
            String Email = String.valueOf(mail);
            String Account = String.valueOf(account);
            try {
                List<ParseObject> pList = query.find();
                for (int i = 0; i < pList.size(); i++) {
                    String DBemail = pList.get(i).getString("email");
                    String DBaccount = pList.get(i).getString("account");
                    if (DBemail.equals(Email) && DBaccount.equals(Account)) {
                        SharedPreferences pref2 = getSharedPreferences("resetpassword", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref2.edit();
                        editor.putString("email", Email);
                        editor.putString("account", Account);
                        editor.commit();
                        objectID = pList.get(i).getObjectId();
                    }
                }
            } catch (com.parse.ParseException e1) {
                e1.printStackTrace();
            }
            query.getInBackground(objectID, new GetCallback<ParseObject>() {
                public void done(ParseObject Member, com.parse.ParseException e) {
                    if (e == null) {
                        //correct message
                        Toast.makeText(forgetpassword.this, "提示：身分確認成功", Toast.LENGTH_LONG).show();
                        //changepage
                        Intent intent = new Intent();
                        intent.setClass(forgetpassword.this, passwordreset.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(forgetpassword.this, "提示：帳號和信箱不符", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_forgetpassword, menu);
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
