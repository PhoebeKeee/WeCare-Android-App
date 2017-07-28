package com.example.victor.mertial_test.activities.Guidingpage_Monitor_Binding;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.example.victor.mertial_test.R;
import com.example.victor.mertial_test.activities.myCare.HomeActivity;
import com.example.victor.mertial_test.apapters.MonitorAdapter;
import com.example.victor.mertial_test.pojo.Monitor;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class BindingMtActivity_2 extends ActionBarActivity {

    private RecyclerView monitor_rv;
    private List<Monitor> monitors;
    private Thread thread;
    private Handler handler;
    private Boolean islog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bindingmonitor_choose);
        SharedPreferences pref = getSharedPreferences("wecare_member",MODE_PRIVATE);
        islog=pref.getBoolean("isLogged",false);
        monitor_rv = (RecyclerView)findViewById(R.id.monitorrv);
        monitor_rv.setLayoutManager(new LinearLayoutManager(this));
        monitor_rv.setHasFixedSize(true);
        initializeData();
        initializeAdapter();
        handler = new Handler();
        monitor_rv.addOnItemTouchListener(new RecyclerTouchListener(this, monitor_rv,new ClickListener(){

            @Override
            public void onClick(View view, int position) {
                AlertDialog.Builder alert = new AlertDialog.Builder(BindingMtActivity_2.this);
                alert.setTitle("輸入產品代碼");
                final EditText input = new EditText(BindingMtActivity_2.this);
                alert.setView(input);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //find product
                        thread = new Thread(new find_product(input.getText().toString()));
                        thread.start();
                        handler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                if (msg.arg1 == 1) {
                                    AlertDialog.Builder alert1 = new AlertDialog.Builder(BindingMtActivity_2.this);
                                    alert1.setTitle("是否確定綁定");
                                    alert1.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            SharedPreferences pref = getSharedPreferences("monitor_id", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = pref.edit();
                                            editor.putString("monitor_id",input.getText().toString());
                                            editor.commit();
                                            AlertDialog.Builder alert2 = new AlertDialog.Builder(BindingMtActivity_2.this);
                                            alert2.setTitle("成功囉!");
                                            alert2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    if(islog){
                                                        finish();
                                                    }else{
                                                        startActivity(new Intent(BindingMtActivity_2.this, HomeActivity.class));
                                                    }
                                                }
                                            });
                                            alert2.show();
                                        }
                                    });
                                    alert1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    alert1.show();
                                } else {
                                    AlertDialog.Builder alert1 = new AlertDialog.Builder(BindingMtActivity_2.this);
                                    alert1.setTitle("代碼錯誤!");
                                    alert1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    alert1.show();
                                }
                            }
                        };

                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alert.show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void initializeData(){
        monitors = new ArrayList<>();
        monitors.add(new Monitor("血壓計"));
    }

    private void initializeAdapter(){
        MonitorAdapter adapter = new MonitorAdapter(monitors);
        monitor_rv.setAdapter(adapter);
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private GestureDetector gestureDetector;
        private ClickListener clickListener;
        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener){
            Log.i("TAG", "constructor invoked ");
            this.clickListener=clickListener;
            gestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    Log.i("TAG","onSingleTapUp "+e);
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(),e.getY());
                    if(child!=null && clickListener!=null){
                        Log.i("TAG","onLongPress "+e);
                        clickListener.onLongClick(child,recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clickListener!=null && gestureDetector.onTouchEvent(e)){
                clickListener.onClick(child,rv.getChildPosition(child));
            }
            //  Log.i("TAG","onInterceptTouchEvent "+e);
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            Log.i("TAG","onTouchEvent "+gestureDetector.onTouchEvent(e)+" "+e);
        }
    }

    public static interface ClickListener{
        public void onClick(View view, int position);
        public void onLongClick(View view, int position);
    }

    private class find_product implements Runnable {
        String input;
        public find_product(String input) {
            this.input=input;
        }
        @Override
        public void run() {
            final ParseQuery<ParseObject> query = ParseQuery.getQuery("Product");
            int mark = 0;
            try {
                List<ParseObject> pList = query.find();
                for (int i = 0; i < pList.size(); i++) {
                    String monitor_id = pList.get(i).getString("monitor_id");
                    if (input.equals(monitor_id)) {
                        mark = 1;
                    }
                }
                Message msg = Message.obtain();
                msg.arg1 = mark;
                handler.sendMessage(msg);
            }catch (com.parse.ParseException e1) {
                e1.printStackTrace();
            }
        };}
    }




