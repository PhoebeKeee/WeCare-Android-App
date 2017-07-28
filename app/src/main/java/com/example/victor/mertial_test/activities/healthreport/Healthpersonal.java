package com.example.victor.mertial_test.activities.healthreport;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ipt810105 on 2015/11/22.
 */
public class Healthpersonal extends ActionBarActivity {



    private List<Health> Health = new ArrayList<>();
    private RecyclerView rv;
    private Toolbar toolbar;
    private Thread thread;
    private HealthAdapter adapter;
    private TextView textName;
    int Blooderror =0;
    int Hearterror =0;
    String Footstatue ;
    private RelativeLayout heart;
    private TextView textView6;
    private TextView personalStatus2;
    private CircleImageView textPhoto;
    Intent myIntent;
    int error=0;
    String account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthreport);
        SharedPreferences pref = getSharedPreferences("wecare_member", MODE_PRIVATE);
        account = pref.getString("account", null);
        textName = (TextView)findViewById(R.id.personalName);
        int imgid = getResources().getIdentifier(account, "drawable","com.example.victor.mertial_test");
        textPhoto = (CircleImageView)findViewById(R.id.personalPhoto);
        textPhoto.setImageResource(imgid);
        textName.setText(account);
        heart=(RelativeLayout)findViewById(R.id.heart);
        personalStatus2=(TextView)findViewById(R.id.personalStatus2);
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if(error==1) {
                    startActivity(myIntent);
                }
            }
        });
        //----------------------------parse for check share-------------
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Function");
        query.whereEqualTo("account", account);
        query.whereEqualTo("func_name","心跳");
        String objectID = null;
        try {
            //get PK from member
            List<ParseObject> pList = query.find();
            for(int i =0;i<pList.size();i++){
                objectID = pList.get(i).getObjectId();
            }
        } catch (com.parse.ParseException e1) {
            e1.printStackTrace();
        }query.getInBackground(objectID, new GetCallback<ParseObject>() {
            public void done(ParseObject Member, com.parse.ParseException e) {
                if (e == null) {
                    heart.setVisibility(View.VISIBLE);
                } else {
                    heart.setVisibility(View.GONE);
                }
            }
        });
        textView6=(TextView)findViewById(R.id.textView6);
        if(account.equals("newtest")){
            textView6.setText("僅有血壓異常，血壓異常時間大約在三餐過後，建議餐後約過1小時後再進行測量，可得更準確數據，若血壓依然高於140/90，則血壓有異常，請洽您的家庭醫師。");
            myIntent = new Intent(Healthpersonal.this,heart_error.class);
            personalStatus2.setText("零次");
            error=0;
        }else if(account.equals("setno")){
            textView6.setText("心跳異常次數過高，血壓僅在上午有異常值，判斷可能為用餐過後測量血壓，建議餐後約過1小時後再進行測量，而可能生活壓力較大，建議調適平日心情，放鬆生活腳步，若長期心跳異常次數過高，請洽您的家庭醫師。");
            personalStatus2.setText("三次");
            myIntent = new Intent(Healthpersonal.this,heart_error.class);
            myIntent.putExtra("times", 3);
            error=1;
        }else if(account.equals("setyes")){
            textView6.setText("心跳無任何異常，血壓異常次數過高，可能患有高血壓的症狀，建議按時服藥，隨時追蹤自我的血壓狀況，避免高血壓所帶來的疾病風險，密切與您的家庭醫師聯繫，藉此追蹤病情；而每日運動量雖不足，但已接近目標，再努力一點即可完成目標，完成良好的運動目標將帶來更好的健康生活。");
            personalStatus2.setText("零次");
            error=0;
        }else if(account.equals("testset")){
            textView6.setText("日常生活注意事項：正常生活起居，凡事得失心勿太重，維持情緒穩定，放鬆心情，放慢腳步 ");
            personalStatus2.setText("兩次");
            myIntent = new Intent(Healthpersonal.this,heart_error.class);
            myIntent.putExtra("times", 2);
            error=1;
        }else if(account.equals("prettyjamie")){
            textView6.setText("今日的各項數值皆正常，繼續保持可維持良好的健康生活品質。");
            personalStatus2.setText("三次");
            myIntent = new Intent(Healthpersonal.this,heart_error.class);
            myIntent.putExtra("times",3);
            error=1;
        }else if(account.equals("testall")){
            textView6.setText("每項數值皆異常，查看是否生活壓力過大，導致心跳及血壓產生了異常數值，而每日的運動量不足，亦容易影響身體健康，建議多多運動，調適平日心情，放鬆生活腳步，以維持良好的生活品質及健康。");
            personalStatus2.setText("兩次");
            myIntent = new Intent(Healthpersonal.this,heart_error.class);
            myIntent.putExtra("times", 2);
            error=1;
        }else{
            textView6.setText("日常生活注意事項：正常生活起居，凡事得失心勿太重，維持情緒穩定，放鬆心情，放慢腳步 ");
            personalStatus2.setText("三次");
            myIntent = new Intent(Healthpersonal.this,heart_error.class);
            myIntent.putExtra("times",3);
            error=1;
        }
        //----------------------------toolbar-------------------------
        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //---------------------Recycle list----------------------------
        rv=(RecyclerView)findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();


    }


    private void initializeData(){
        thread = new Thread(new check_member_data());
        thread.start();
    }
    private class check_member_data implements Runnable {
        public check_member_data() {

        }

        @Override
        public void run() {

            List<HealthAdapter.Item> data = new ArrayList<>();
//            Intent intent = getIntent();
//            String account = intent.getStringExtra("familyname");
//            System.out.println(account);
            //SharedPreferences pref = getSharedPreferences("wecare_member", MODE_PRIVATE);
            //String account = pref.getString("account", null);
//            //System.out.println(account);
//            final ParseQuery<ParseObject> query = ParseQuery.getQuery("Function");
//            final ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Heartbeat");
//            final ParseQuery<ParseObject> query3 = ParseQuery.getQuery("Bloodpressure");
//            final ParseQuery<ParseObject> query4 = ParseQuery.getQuery("Pace");
//            try {
//                query.whereEqualTo("account",account);
//                query2.whereEqualTo("account",account);
//                query3.whereEqualTo("account",account);
//                query4.whereEqualTo("account",account);
//                List<ParseObject> pList = query.find();
//                List<ParseObject> pList2 = query2.find();
//                List<ParseObject> pList3 = query3.find();
//                List<ParseObject> pList4 = query4.find();
            if(account.equals("newtest")) {
                HealthAdapter.Item blood = new HealthAdapter.Item(HealthAdapter.HEADER, "血壓", "兩次", "異常");
                blood.invisibleChildren = new ArrayList<>();
                blood.invisibleChildren.add(new HealthAdapter.Item(HealthAdapter.CHILD, "", "", "上午9:17-收縮壓/舒張壓:156/92mmHg"));//140/90至160/95
                blood.invisibleChildren.add(new HealthAdapter.Item(HealthAdapter.CHILD, "", "", "下午6:17-收縮壓/舒張壓:149/90mmHg"));
                data.add(blood);
                HealthAdapter.Item places = new HealthAdapter.Item(HealthAdapter.HEADER, "步行數", "昨日已達標", "");
                places.invisibleChildren = new ArrayList<>();
                places.invisibleChildren.add(new HealthAdapter.Item(HealthAdapter.CHILD, "", "", "共計12555步"));
                data.add(places);
            }else if(account.equals("setno")){
                HealthAdapter.Item blood = new HealthAdapter.Item(HealthAdapter.HEADER, "血壓", "一次", "異常");
                blood.invisibleChildren = new ArrayList<>();
                blood.invisibleChildren.add(new HealthAdapter.Item(HealthAdapter.CHILD, "", "", "上午8:49-收縮壓/舒張壓:152/91mmHg"));
                data.add(blood);
                HealthAdapter.Item places = new HealthAdapter.Item(HealthAdapter.HEADER, "步行數", "昨日已達標", "");
                places.invisibleChildren = new ArrayList<>();
                places.invisibleChildren.add(new HealthAdapter.Item(HealthAdapter.CHILD, "", "", "共計15879步"));
                data.add(places);
            }else if(account.equals("setyes")){
                HealthAdapter.Item blood = new HealthAdapter.Item(HealthAdapter.HEADER, "血壓", "四次", "異常");
                blood.invisibleChildren = new ArrayList<>();
                blood.invisibleChildren.add(new HealthAdapter.Item(HealthAdapter.CHILD, "", "", "上午8:26-收縮壓/舒張壓:159/94mmHg"));
                blood.invisibleChildren.add(new HealthAdapter.Item(HealthAdapter.CHILD, "", "", "下午3:15-收縮壓/舒張壓:145/91mmHg"));
                blood.invisibleChildren.add(new HealthAdapter.Item(HealthAdapter.CHILD, "", "", "下午6:18-收縮壓/舒張壓:156/93mmHg"));
                blood.invisibleChildren.add(new HealthAdapter.Item(HealthAdapter.CHILD, "", "", "下午9:50-收縮壓/舒張壓:147/92mmHg"));
                data.add(blood);
                HealthAdapter.Item places = new HealthAdapter.Item(HealthAdapter.HEADER, "步行數", "昨日未達標", "");
                places.invisibleChildren = new ArrayList<>();
                places.invisibleChildren.add(new HealthAdapter.Item(HealthAdapter.CHILD, "", "", "尚差879步"));
                data.add(places);
            }else if(account.equals("testset")){
                HealthAdapter.Item blood = new HealthAdapter.Item(HealthAdapter.HEADER, "血壓", "一次", "異常");
                blood.invisibleChildren = new ArrayList<>();
                blood.invisibleChildren.add(new HealthAdapter.Item(HealthAdapter.CHILD, "", "", "下午7:19-收縮壓/舒張壓:157/93mmHg"));
                data.add(blood);
                HealthAdapter.Item places = new HealthAdapter.Item(HealthAdapter.HEADER, "步行數", "昨日已達標", "");
                places.invisibleChildren = new ArrayList<>();
                places.invisibleChildren.add(new HealthAdapter.Item(HealthAdapter.CHILD, "", "", "共計17328步"));
                data.add(places);
            }else if(account.equals("prettyjamie")){
                HealthAdapter.Item blood = new HealthAdapter.Item(HealthAdapter.HEADER, "血壓", "零次", "異常");
                blood.invisibleChildren = new ArrayList<>();
                data.add(blood);
                HealthAdapter.Item places = new HealthAdapter.Item(HealthAdapter.HEADER, "步行數", "昨日已達標", "");
                places.invisibleChildren = new ArrayList<>();
                places.invisibleChildren.add(new HealthAdapter.Item(HealthAdapter.CHILD, "", "", "共計14828步"));
                data.add(places);
            }else if(account.equals("testall")){
                HealthAdapter.Item blood = new HealthAdapter.Item(HealthAdapter.HEADER, "血壓", "兩次", "異常");
                blood.invisibleChildren = new ArrayList<>();
                blood.invisibleChildren.add(new HealthAdapter.Item(HealthAdapter.CHILD, "", "", "上午9:49-收縮壓/舒張壓:140/91mmHg"));
                blood.invisibleChildren.add(new HealthAdapter.Item(HealthAdapter.CHILD, "", "", "下午11:49-收縮壓/舒張壓:158/95mmHg"));
                data.add(blood);
                HealthAdapter.Item places = new HealthAdapter.Item(HealthAdapter.HEADER, "步行數", "昨日未達標", "");
                places.invisibleChildren = new ArrayList<>();
                places.invisibleChildren.add(new HealthAdapter.Item(HealthAdapter.CHILD, "", "", "尚差1929步"));
                data.add(places);
            }else{
                HealthAdapter.Item blood = new HealthAdapter.Item(HealthAdapter.HEADER, "血壓", "一次", "異常");
                blood.invisibleChildren = new ArrayList<>();
                blood.invisibleChildren.add(new HealthAdapter.Item(HealthAdapter.CHILD, "", "", "上午11:49-收縮壓/舒張壓:151/90mmHg"));
                data.add(blood);
                HealthAdapter.Item places = new HealthAdapter.Item(HealthAdapter.HEADER, "步行數", "昨日已達標", "");
                places.invisibleChildren = new ArrayList<>();
                places.invisibleChildren.add(new HealthAdapter.Item(HealthAdapter.CHILD, "", "", "共計15029步"));
                data.add(places);
            }
            rv.setAdapter(new HealthAdapter(data));

//            } catch (ParseException e) {
//                e.printStackTrace();
//            }

        }
    }
    private void initializeAdapter(){

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
