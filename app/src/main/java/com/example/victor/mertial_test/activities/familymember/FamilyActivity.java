package com.example.victor.mertial_test.activities.familymember;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.victor.mertial_test.R;
import com.example.victor.mertial_test.activities.healthreport.Healthreport;
import com.example.victor.mertial_test.activities.setting.SettingActivity;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.victor.mertial_test.Extras.L.l;
import static com.example.victor.mertial_test.Extras.L.m;

/**
 * Created by ipt810105 on 2015/10/22.
 */
public class FamilyActivity extends ActionBarActivity {
    private List<member> member = new ArrayList<>();
    private RecyclerView rv;
    private Toolbar toolbar;
    private ImageButton clickMember;
    private Thread thread;
    private FamilyAdapter adapter;
    private SharedPreferences home;
    private SharedPreferences family;
    private SharedPreferences.Editor home_edit;
    private SharedPreferences.Editor family_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);

        home = getSharedPreferences("home",MODE_PRIVATE);
        home_edit = home.edit();
        family=getSharedPreferences("family123", MODE_PRIVATE);
        family_edit=family.edit();
        family_edit.putBoolean("lock2",true).commit();
        //----------------------------toolbar-------------------------
        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //---------------------Recycle list----------------------------
        clickMember = (ImageButton)findViewById(R.id.Button3);
        rv=(RecyclerView)findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        adapter = new FamilyAdapter(member);
        rv.setAdapter(adapter);
        initializeData();


        clickMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(FamilyActivity.this, SearchMemberActivity.class);
                startActivity(Intent);
            }
        });
        rv.addOnItemTouchListener(new RecyclerTouchListener(this, rv, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
//                m(FamilyActivity.this,"short");
                Intent intent = new Intent(FamilyActivity.this, Healthreport.class);
                intent.putExtra("familyname",member.get(position).name);
                intent.putExtra("nickname",member.get(position).getNickname());
                startActivity(intent);
                }
            @Override
            public void onLongClick(View view, final int position) {
//             m(FamilyActivity.this,"long");
                AlertDialog.Builder alert = new AlertDialog.Builder(FamilyActivity.this);
                alert.setTitle("更換名稱");
                final EditText input = new EditText(FamilyActivity.this);
                alert.setView(input);
                alert.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.setName(input.getText().toString(), position);
                        SharedPreferences pref = getSharedPreferences("wecare_member", MODE_PRIVATE);
                        String account = pref.getString("account", null);
                        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Relation");
                        String objectID = null;
                        query.whereEqualTo("account",account);
                        query.whereEqualTo("familymember",member.get(position).name);
                        try {
                            List<ParseObject> pList = query.find();
                            for(int i =0;i<pList.size();i++){
                                objectID = pList.get(i).getObjectId();
                            }
                        } catch (com.parse.ParseException e1) {
                            e1.printStackTrace();
                        }
                        query.getInBackground(objectID, new GetCallback<ParseObject>() {
                            public void done(ParseObject Relation, com.parse.ParseException e) {
                                if (e == null) {
                                    //reset weight
                                    Relation.put("nickname", input.getText().toString());
                                    Relation.saveInBackground();
                                    //success massege
                                    Toast.makeText(FamilyActivity.this, "修改成功 !", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(FamilyActivity.this, "修改失敗 !", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();
            }
        }));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        home_edit.putBoolean("lock",true).commit();
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
            }

            private class check_member_data implements Runnable {
                public check_member_data() {

                }

                @Override
                public void run() {
                    if (family.getBoolean("lock2", true)) {
                    SharedPreferences pref = getSharedPreferences("wecare_member", MODE_PRIVATE);
                    String account = pref.getString("account", null);
                    member = new ArrayList<>();
                        l("run");
                    final ParseQuery<ParseObject> query = ParseQuery.getQuery("Relation");
                    try {
                        query.whereEqualTo("account", account);
                        List<ParseObject> pList = query.find();

                        int k = query.count();
                        member.clear();
                        for (int i = 0; i < k; i++) {
                            int imgid = getResources().getIdentifier(pList.get(i).getString("familymember"), "drawable", "com.example.victor.mertial_test");
                            member.add(new member(account, pList.get(i).getString("familymember"), imgid, pList.get(i).getString("nickname"), pList.get(i).getString("pin")));

                        }
                        adapter.sethomeList((ArrayList<member>) member);
                        adapter.notifyDataSetChanged();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                        family_edit.putBoolean("lock2", false).commit();
                    }
                }
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
                if (id == android.R.id.home) {
                    finish();
                    return true;
                }


                return super.onOptionsItemSelected(item);
            }


            class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

                private GestureDetector gestureDetector;
                private ClickListener clickListener;

                public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
                    Log.i("TAG", "constructor invoked ");
                    this.clickListener = clickListener;
                    gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

                        @Override
                        public boolean onSingleTapUp(MotionEvent e) {
                            Log.i("TAG", "onSingleTapUp " + e);
                            return true;
                        }

                        @Override
                        public void onLongPress(MotionEvent e) {
                            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                            if (child != null && clickListener != null) {
                                Log.i("TAG", "onLongPress " + e);
                                clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                            }
                        }
                    });
                }

                @Override
                public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                    View child = rv.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                        clickListener.onClick(child, rv.getChildPosition(child));
                    }
                    //  Log.i("TAG","onInterceptTouchEvent "+e);
                    return false;
                }

                @Override
                public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                    Log.i("TAG", "onTouchEvent " + gestureDetector.onTouchEvent(e) + " " + e);
                }
            }

            public static interface ClickListener {
                public void onClick(View view, int position);

                public void onLongClick(View view, int position);
            }
        }
