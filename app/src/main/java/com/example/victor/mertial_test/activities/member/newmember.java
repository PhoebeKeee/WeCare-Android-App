package com.example.victor.mertial_test.activities.member;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.victor.mertial_test.R;
import com.example.victor.mertial_test.activities.myCare.HomeActivity;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Calendar;
import java.util.List;


public class newmember extends Activity {
    EditText account;
    EditText email;
    EditText password;
    Button submit;
    EditText share_password;
    EditText height;
    EditText weight;
    private Thread thread;
    RadioButton boy;
    RadioButton girl;
    RadioGroup radiosex;
    String sex;
    Switch pushmessage;
    String getmessage="Y";
    private TextView tvDate;
    private int mYear, mMonth, mDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newmember);
        //recode parse is already connect
        SharedPreferences pref=getSharedPreferences("parse_start",MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        editor.putBoolean("isRun", true);
        editor.commit();
        //connect view
        account=(EditText)findViewById(R.id.account);
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        share_password=(EditText)findViewById(R.id.share_password);
        height=(EditText)findViewById(R.id.height);
        weight=(EditText)findViewById(R.id.weight);
        boy=(RadioButton)findViewById(R.id.boy);
        girl=(RadioButton)findViewById(R.id.girl);
        radiosex=(RadioGroup)findViewById(R.id.radiosex);
        pushmessage=(Switch)findViewById(R.id.pushmessage);
        //set birthday
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvDate.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        //set newmember
        submit=(Button)findViewById(R.id.submit);
        pushmessage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getmessage="Y";
                } else {
                    getmessage="N";
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (account.getText().toString().trim().equals("") == true) {
                    Toast.makeText(newmember.this, "請輸入帳號 !", Toast.LENGTH_SHORT).show();
                } else if (email.getText().toString().trim().equals("") == true) {
                    Toast.makeText(newmember.this, "請輸入信箱 !", Toast.LENGTH_SHORT).show();
                } else if (email.getText().toString().trim().indexOf("@") == -1) {
                    Toast.makeText(newmember.this, "信箱格式錯誤 !", Toast.LENGTH_SHORT).show();
                } else if (password.getText().toString().trim().equals("") == true) {
                    Toast.makeText(newmember.this, "請輸入密碼 !", Toast.LENGTH_SHORT).show();
                } else if (share_password.getText().toString().trim().equals("") == true) {
                    Toast.makeText(newmember.this, "請輸入分享權限密碼 !", Toast.LENGTH_SHORT).show();
                }else if (height.getText().toString().trim().equals("") == true) {
                    Toast.makeText(newmember.this, "請輸入身高 !", Toast.LENGTH_SHORT).show();
                } else if (weight.getText().toString().trim().equals("") == true) {
                    Toast.makeText(newmember.this, "請輸入體重 !", Toast.LENGTH_SHORT).show();
                }else if (tvDate.getText().toString().trim().equals("") == true) {
                    Toast.makeText(newmember.this, "請輸入生日 !", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(radiosex.getCheckedRadioButtonId() == R.id.boy){
                        sex="male";
                    }else if(radiosex.getCheckedRadioButtonId() == R.id.girl){
                        sex="female";
                    }
                    thread = new Thread(new new_member_data(account.getText().toString(),email.getText().toString(),password.getText().toString(),share_password.getText().toString(),height.getText().toString(),weight.getText().toString(),sex,getmessage.toString(),tvDate.getText().toString()));
                    thread.start();
                }
            }
        });
    }
    public void showDatePickerDialog() {
        // 設定初始日期
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // 跳出日期選擇器
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // 完成選擇，顯示日期
                        tvDate.setText(year + "/" + (monthOfYear + 1) + "/"
                                + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }
    private class new_member_data implements Runnable {
        String account,mail,password,share_password,height,weight,sex,pushmessage,tvDate;
        public new_member_data(String account,String mail,String password,String share_password,String height,String weight,String sex,String pushmessage,String tvDate) {
            this.account=account;
            this.mail = mail;
            this.password=password;
            this.share_password=share_password;
            this.height=height;
            this.weight=weight;
            this.sex=sex;
            this.pushmessage=pushmessage;
            this.tvDate=tvDate;
        }
        @Override
        public void run() {
            final ParseQuery<ParseObject> query = ParseQuery.getQuery("Member");
            String mark_account = "0";
            String mark_email = "0";
            try {
                List<ParseObject> pList = query.find();
                for (int i = 0; i < pList.size(); i++) {
                    String DBemail = pList.get(i).getString("email");
                    String DBaccount = pList.get(i).getString("account");
                    if (account.equals(DBaccount)) {
                        mark_account = "1";
                    } else if (mail.equals(DBemail)) {
                        mark_email = "1";
                    }
                }
            } catch (com.parse.ParseException e1) {
                e1.printStackTrace();
            }
            //Parse
            if (mark_account.equals("1")) {
                Looper.prepare();
                Toast.makeText(newmember.this, "此帳號已存在 !", Toast.LENGTH_SHORT).show();
                Looper.loop();
            } else if (mark_email.equals("1")) {
                Looper.prepare();
                Toast.makeText(newmember.this, "此信箱已存在 !", Toast.LENGTH_SHORT).show();
                Looper.loop();
            } else {
                //new member
                ParseObject memberObject = new ParseObject("Member");
                memberObject.put("account", String.valueOf(account));
                memberObject.put("email", String.valueOf(mail));
                memberObject.put("password", String.valueOf(password));
                memberObject.put("share_password", String.valueOf(share_password));
                memberObject.put("height", String.valueOf(height));
                memberObject.put("weight", String.valueOf(weight));
                memberObject.put("sex", String.valueOf(sex));
                memberObject.put("pushmessage", String.valueOf(pushmessage));
                memberObject.put("birthday",String.valueOf(tvDate));
                memberObject.saveInBackground();
                //SharedPreferences
                SharedPreferences pref = getSharedPreferences("wecare_member", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("email", mail);
                editor.putString("account", account);
                editor.putBoolean("isLogged", true);
                editor.commit();
                //changepage
                Intent intent = new Intent();
                intent.setClass(newmember.this, HomeActivity.class);
                startActivity(intent);
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_newmember, menu);
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