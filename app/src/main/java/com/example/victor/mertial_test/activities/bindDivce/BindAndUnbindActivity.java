package com.example.victor.mertial_test.activities.bindDivce;

import android.content.Context;
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
import android.widget.Toast;

import com.example.victor.mertial_test.Extras.HeartBeat.HeartBeatService;
import com.example.victor.mertial_test.R;
import com.example.victor.mertial_test.activities.Guidingpage_Monitor_Binding.BindingMtActivity_2;
import com.example.victor.mertial_test.activities.Guidingpage_WearableDevice_Binding.BindingWdActivity_2;
import com.example.victor.mertial_test.apapters.BindAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.example.victor.mertial_test.Extras.L.l;

public class BindAndUnbindActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private RecyclerView bind_rv;
    private List<Integer> type;
    private List<String> binds;
    private BindAdapter bindAdapter;


    public interface ICallback{
        public void stop();
        public void stoph();
    }
    private ICallback rCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bindandunbind);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binds = new ArrayList<>();
        type = new ArrayList<>();
        bind_rv = (RecyclerView)findViewById(R.id.bindandunbind_rv);
        bind_rv.setLayoutManager(new LinearLayoutManager(this));
        bindAdapter = new BindAdapter(this);
        bind_rv.setAdapter(bindAdapter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        binds.clear();
        type.clear();
        bindAdapter.removeall();
    }

    @Override
    protected void onResume() {
        super.onResume();

        initializeData();
        l(binds.size() + "");
        l(bindAdapter.getItemCount() + " ");
        l(binds.size()+"");
        bind_rv.addOnItemTouchListener(new RecyclerTouchListener(this, bind_rv, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (type.get(position) == 1) {
                    if (rCallback != null) {
                        rCallback.stoph();
                    }
                    Intent stopIntent1 = new Intent(BindAndUnbindActivity.this, HeartBeatService.class);
                    stopService(stopIntent1);
                } else {
                    SharedPreferences pref1 = getSharedPreferences("monitor_id", MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = pref1.edit();
                    editor1.remove("monitor_id");
                    editor1.putString("monitor_id", "non_monitor");
                    editor1.commit();
                }
                Toast.makeText(BindAndUnbindActivity.this, "解除成功", Toast.LENGTH_SHORT).show();
//                    }
//                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//                alert.show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bind_and_unbind, menu);
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


    public void wearable_device(View view){
        startActivity(new Intent(this, BindingWdActivity_2.class));
    }

    public void healthy_monitor(View view){
        startActivity(new Intent(this, BindingMtActivity_2.class));
    }
    private void initializeData(){
        SharedPreferences isServieAlive = getSharedPreferences("Servicelife", MODE_PRIVATE);
        SharedPreferences pref = getSharedPreferences("monitor_id", MODE_PRIVATE);
        String monitor = pref.getString("monitor_id","non_monitor");
        if(isServieAlive.getBoolean("alive",false)){
            SharedPreferences device_name = getSharedPreferences("Device_name",MODE_PRIVATE);
            String name = device_name.getString("device_name", "穿戴裝置");
            binds.add(name);
            type.add(1);
        }
        if(!monitor.equals("non_monitor")){
            binds.add(monitor);
            type.add(2);
        }
        bindAdapter.setBindList((ArrayList<String>) binds);
        bindAdapter.notifyDataSetChanged();
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
}
