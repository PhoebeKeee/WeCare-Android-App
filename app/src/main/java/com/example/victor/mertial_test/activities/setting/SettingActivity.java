package com.example.victor.mertial_test.activities.setting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.victor.mertial_test.Extras.Pace.PaceService;
import com.example.victor.mertial_test.R;
import com.example.victor.mertial_test.activities.member.login;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.victor.mertial_test.Extras.L.l;

public class SettingActivity extends ActionBarActivity {
    private static final String SERVER_ADDRESS="http://wecarememberpic.comze.com/";
    private Toolbar toolbar;
    ImageButton reset_higwei;
    ImageButton reset_share;
    ImageButton reset_permissionshare;
    CircleImageView reset_personal;
    Button logout;
    TextView personalname1;
    TextView personalpsw;
    TextView personalheight;
    TextView personalweight;
    TextView sharepsw;
    TextView gender;
    TextView birthday;
    Switch messageview;
    String getmessage;
    private Thread thread;

    public interface ICallback{
        public void stop();
        public void stoph();
    }
    private ICallback rCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //toolbar
        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //start
        reset_higwei=(ImageButton)findViewById(R.id.reset_higwei);
        reset_personal=(CircleImageView)findViewById(R.id.reset_personal);
        reset_share=(ImageButton)findViewById(R.id.reset_permissionpsw);
        reset_permissionshare=(ImageButton)findViewById(R.id.reset_permissionshare);
        personalname1=(TextView)findViewById(R.id.account);
        personalpsw=(TextView)findViewById(R.id.emailadd);
        personalheight=(TextView)findViewById(R.id.personalheight);
        personalweight=(TextView)findViewById(R.id.personalweight);
        sharepsw=(TextView)findViewById(R.id.permission_psw);
        gender=(TextView)findViewById(R.id.gender);
        birthday=(TextView)findViewById(R.id.birthday);
        messageview=(Switch)findViewById(R.id.message);
        logout=(Button)findViewById(R.id.logout);
        //SharedPreferences
        SharedPreferences pref2 = getSharedPreferences("wecare_member", MODE_PRIVATE);
        final String wecare_account=pref2.getString("account", null);
        final String wecare_email=pref2.getString("email", null);
        //parse
//        final String[] height = {null};
//        final String[] weight = new String[1];
//        final String[] sharepassword = new String[1];
        thread = new Thread(new setdata(wecare_account,wecare_email));
        thread.start();
//        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Member");
//        String objectID = null;
//        try {
//            List<ParseObject> pList = query.find();
//            for(int i =0;i<pList.size();i++){
//                String DBemail=pList.get(i).getString("email");
//                String DBaccount=pList.get(i).getString("account");
//                if(DBemail.equals(wecare_email)&&DBaccount.equals(wecare_account)) {
//                    objectID = pList.get(i).getObjectId();
//                }
//            }
//        } catch (com.parse.ParseException e1) {
//            e1.printStackTrace();
//        }query.getInBackground(objectID, new GetCallback<ParseObject>() {
//            public void done(ParseObject Member, com.parse.ParseException e) {
//                if (e == null) {
//                    height[0] = Member.getString("height");
//                    weight[0] = Member.getString("weight");
//                    sharepassword[0] = Member.getString("share_password");
//                    //viewset
//                    personalname1.setText("＃帳號 "+wecare_account);
//                    personalpsw.setText("＃信箱 "+wecare_email);
//                    personalheight.setText("＃身高 "+height[0]+"cm");
//                    personalweight.setText("＃體重 "+weight[0]+"kg");
//                    sharepsw.setText("＃權限密碼 "+sharepassword[0]);
//                } else {
//                    Toast.makeText(SettingActivity.this, "Parse DB Error !", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        reset_permissionshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SettingActivity.this, com.example.victor.mertial_test.activities.setting.ShareActivity.class);
                startActivity(intent);
            }
        });
        //change height and weight
        reset_higwei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SettingActivity.this, com.example.victor.mertial_test.activities.setting.reset_higwei.class);
                startActivity(intent);
            }
        });
        //change share password
        reset_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SettingActivity.this, com.example.victor.mertial_test.activities.setting.reset_share.class);
                startActivity(intent);
            }
        });
        messageview.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getmessage = "Y";
                    thread = new Thread(new setmessage(getmessage,wecare_account));
                    thread.start();
                } else {
                    getmessage = "N";
                    thread = new Thread(new setmessage(getmessage,wecare_account));
                    thread.start();
                }
            }
        });
        //logout and clean sharedpreferences
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SettingActivity.this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("登出")
                        .setMessage("您確定要登出嗎?")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                SharedPreferences pref = getSharedPreferences("wecare_member", MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.clear();
                                editor.commit();
                                editor.putBoolean("isLogged",false).commit();
                                boolean log_check =pref.getBoolean("isLogged", false);
                                l("log_check : "+log_check);
                                SharedPreferences pref2 = getSharedPreferences("resetpassword", MODE_PRIVATE);
                                SharedPreferences.Editor editor2 = pref2.edit();
                                editor2.clear();
                                editor2.commit();
                                SharedPreferences pref3 = getSharedPreferences("backbutton", MODE_PRIVATE);
                                SharedPreferences.Editor editor3 = pref3.edit();
                                editor3.putString("inhibit_return", "2");
                                editor3.commit();
                                SharedPreferences pref4 = getSharedPreferences("parse_start", MODE_PRIVATE);
                                SharedPreferences.Editor editor4 = pref4.edit();
                                editor4.putBoolean("isRun", false);
                                editor4.commit();
                                SharedPreferences pref5 = getSharedPreferences("monitor_id", MODE_PRIVATE);
                                SharedPreferences.Editor editor5 = pref5.edit();
                                editor5.remove("monitor_id");
                                editor5.putString("monitor_id","non_monitor");
                                editor5.commit();
                                if (rCallback != null) {
                                    rCallback.stop();
                                }
                                Intent stopIntent1 = new Intent(SettingActivity.this, PaceService.class);
                                stopService(stopIntent1);
                                Intent intent3 = new Intent();
                                intent3.setClass(SettingActivity.this, login.class);
                                startActivity(intent3);
                            }
                        }).setNegativeButton("否", null).show();
            }
        });
        reset_personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SettingActivity.this, com.example.victor.mertial_test.activities.picture.showpicture.class);
                startActivity(intent);
            }
        });
    }

    private class setmessage implements Runnable {
        String getmessage,account;
        public setmessage(String getmessage,String account) {
            this.account=account;
            this.getmessage=getmessage;
        }
        @Override
        public void run() {
            final ParseQuery<ParseObject> query = ParseQuery.getQuery("Member");
            String objectID = null;
            try {
                List<ParseObject> pList = query.find();
                for(int i =0;i<pList.size();i++){String DBaccount=pList.get(i).getString("account");
                    if(DBaccount.equals(account)) {
                        objectID = pList.get(i).getObjectId();
                    }
                }
            } catch (com.parse.ParseException e1) {
                e1.printStackTrace();
            }
            query.getInBackground(objectID, new GetCallback<ParseObject>() {
                public void done(ParseObject Member, com.parse.ParseException e) {
                    if (e == null) {
                        Member.put("pushmessage", String.valueOf(getmessage));
                        Member.saveInBackground();
                    }
                }
            });
        };
    }


    private class setdata implements Runnable {
        String account,mail;
        public setdata(String account,String mail) {
            this.account=account;
            this.mail = mail;
        }
        @Override
        public void run() {
            new DownloadImage(account).execute();
            final String[] height = {null};
            final String[] weight = new String[1];
            final String[] birth = new String[1];
            final String[] sex = new String[1];
            final String[] sharepassword = new String[1];
            final String[] message=new String[1];
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
            }query.getInBackground(objectID, new GetCallback<ParseObject>() {
                public void done(ParseObject Member, com.parse.ParseException e) {
                    if (e == null) {
                        sex[0]=Member.getString("sex");
                        birth[0]=Member.getString("birthday");
                        height[0] = Member.getString("height");
                        weight[0] = Member.getString("weight");
                        sharepassword[0] = Member.getString("share_password");
                        message[0] = Member.getString("pushmessage");
                        //viewset
                        personalname1.setText("帳號 "+account);
                        gender.setText("性別 "+sex[0]);
                        birthday.setText("生日 "+birth[0]);
                        personalpsw.setText("信箱 "+mail);
                        personalheight.setText("身高 "+height[0]+"cm");
                        personalweight.setText("體重 "+weight[0]+"kg");
                        sharepsw.setText("權限密碼 "+sharepassword[0]);
                        if(message[0].equals("Y")==true){
                            messageview.setChecked(true);
                        } else if(message[0].equals("N")==true){
                            messageview.setChecked(false);
                        }
                    } else {
                        Toast.makeText(SettingActivity.this, "Parse DB Error !", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        };
        private class DownloadImage extends AsyncTask<Void,Void,Bitmap> {

            String name;

            public DownloadImage(String name){
                this.name=name;
            }

            @Override
            protected Bitmap doInBackground(Void... params) {

                String url=SERVER_ADDRESS+"picture/"+name+".jpg";
                try{
                    URLConnection connection=new URL(url).openConnection();
                    connection.setConnectTimeout(10000 * 30);
                    connection.setReadTimeout(10000 * 30);
                    return BitmapFactory.decodeStream((InputStream) connection.getContent(), null, null);

                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                if (bitmap != null){
                    reset_personal.setImageBitmap(bitmap);
                }
            }
        }

        private HttpParams getHttpRequestParams(){
            HttpParams httpRequestParams=new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, 10000 * 30);
            HttpConnectionParams.setSoTimeout(httpRequestParams, 10000 * 30);
            return httpRequestParams;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if(id==android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
