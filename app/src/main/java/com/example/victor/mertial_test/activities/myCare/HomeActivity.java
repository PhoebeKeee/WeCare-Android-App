package com.example.victor.mertial_test.activities.myCare;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.victor.mertial_test.R;
import com.example.victor.mertial_test.activities.Message.message;
import com.example.victor.mertial_test.fragments.NavigationDrawerFragment;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends ActionBarActivity {
    private static final String SERVER_ADDRESS="http://wecarememberpic.comze.com/";
    ProgressDialog dialog;

    private List<Person> persons = new ArrayList<>();
    private RecyclerView rv;
    private Toolbar toolbar;
    private Thread thread;
    private RVAdapter adapter;
    private SharedPreferences home;
    private RelativeLayout Click;
    private SharedPreferences.Editor home_edit;
    private TextView others;
    CircleImageView photo;
    TextView name;
    int imgid;
    TextView message2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SharedPreferences pref = getSharedPreferences("wecare_member", MODE_PRIVATE);
        String account = pref.getString("account", null);
        final  ParseQuery<ParseObject> query3 = ParseQuery.getQuery("Newmessage");
        query3.whereEqualTo("account", account);
        new DownloadImage(account).execute();


        photo =(CircleImageView)findViewById(R.id.personalPhoto);
        name = (TextView)findViewById(R.id.personalName);
        name.setText(account);
        others=(TextView)findViewById(R.id.others);
        message2 = (TextView)findViewById(R.id.accountmessage);


        List<ParseObject> pList3 = null;
        try {
            pList3 = query3.find();
            System.out.println(query3.count());
            System.out.println(pList3.get(0).getString("newmessage"));
            message2.setText(pList3.get(0).getString("newmessage"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        home = getSharedPreferences("home",MODE_PRIVATE);
        home_edit = home.edit();
        home_edit.putBoolean("lock",true).commit();
        //----------------------------toolbar-------------------------
        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);



        //---------------------Recycle list----------------------------
        Click=(RelativeLayout)findViewById(R.id.intent);
        rv=(RecyclerView)findViewById(R.id.rv);
        Click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences("wecare_member", MODE_PRIVATE);
                String account = pref.getString("account", null);
                Intent myIntent = new Intent(v.getContext(), message.class);
                myIntent.putExtra("familyname", account);
                v.getContext().startActivity(myIntent);
            }
        });
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        adapter = new RVAdapter(persons);
        rv.setAdapter(adapter);
        initializeData();

    }


    private void initializeData(){
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
            thread = new Thread(new check_member_data());
            thread.start();
        }
        },0, 1000);
        //thread = new Thread(new check_member_data());
        //thread.start();
    }
    private class check_member_data implements Runnable {
        public check_member_data() {

        }
        @Override
        public void run() {

            if (home.getBoolean("lock", true)) {
                SharedPreferences pref = getSharedPreferences("wecare_member", MODE_PRIVATE);
                String account = pref.getString("account", null);
                System.out.println(account);
                final ParseQuery<ParseObject> query = ParseQuery.getQuery("Relation");
                final  ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Newmessage");
                try {

                    query.whereEqualTo("account", account);
                    query.whereEqualTo("pin", "Y");


                    final int k = query.count();
                    List<ParseObject> pList = query.find();
                    List<ParseObject> pList2 = query2.find();
                    persons.clear();
                    for (int i = 0; i < k; i++) {
                        imgid = getResources().getIdentifier(pList.get(i).getString("familymember"), "drawable","com.example.victor.mertial_test");
                        persons.add(new Person(pList.get(i).getString("nickname"), String.valueOf(pList.get(i).getCreatedAt()).substring(0, 19), imgid, pList.get(i).getString("familymember"),pList2.get(i).getString("newmessage")));
                    }
                    adapter.sethomeList((ArrayList<Person>) persons);
                    adapter.notifyDataSetChanged();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            others.setText("我的最愛( "+k+" )");
                        }
                    });
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                home_edit.putBoolean("lock", false).commit();
            }
        }
    }
    private void initializeAdapter(){

    }
//    public static Bitmap readBitMap(Context context, int imgid){
//            BitmapFactory.Options opt = new BitmapFactory.Options();
//            opt.inPreferredConfig = Bitmap.Config.RGB_565;
//             opt.inPurgeable = true;
//            opt.inInputShareable = true;
//               //獲取資源圖片
//            InputStream is = context.getResources().openRawResource(imgid);
//               return BitmapFactory.decodeStream(is,null,opt);
//       }
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
            photo.setImageBitmap(bitmap);
        }
    }
}

    private HttpParams getHttpRequestParams(){
        HttpParams httpRequestParams=new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpRequestParams, 10000 * 30);
        HttpConnectionParams.setSoTimeout(httpRequestParams, 10000 * 30);
        return httpRequestParams;
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
        return super.onOptionsItemSelected(item);
    }
}
