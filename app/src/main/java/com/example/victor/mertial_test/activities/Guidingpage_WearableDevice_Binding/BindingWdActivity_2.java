package com.example.victor.mertial_test.activities.Guidingpage_WearableDevice_Binding;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.victor.mertial_test.Extras.HeartBeat.HeartBeatService;
import com.example.victor.mertial_test.R;
import com.example.victor.mertial_test.activities.Guidingpage_Monitor_Binding.BindingMtActivity_1;
import com.example.victor.mertial_test.apapters.WearableDevicesAdapter;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class BindingWdActivity_2 extends ActionBarActivity {

    private RecyclerView listbtDevices;
    private ArrayList<BluetoothDevice> remoteDeviceList = new ArrayList<>();
    private WearableDevicesAdapter devicesAdapter;

    private static int REQUEST_ENABLE_BT = 0x0;
    public static final String ACTION_DISCOVERY_FINISHED = BluetoothAdapter.ACTION_DISCOVERY_FINISHED;
    public static final String ACTION_FOUND = BluetoothDevice.ACTION_FOUND;
    private BluetoothAdapter btAdapter = null;
    private Thread thread;
    private Thread thread2;
    private Boolean next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bindingwd_choose);
        final SharedPreferences pref = getSharedPreferences("nextpage_check",MODE_PRIVATE);
        next=pref.getBoolean("next",false);
        listbtDevices = (RecyclerView)findViewById(R.id.listDevices);
        listbtDevices.setLayoutManager(new LinearLayoutManager(this));
        devicesAdapter = new WearableDevicesAdapter(this);
        listbtDevices.setAdapter(devicesAdapter);
        listbtDevices.addOnItemTouchListener(new RecyclerTouchListener(this, listbtDevices, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                AlertDialog.Builder alert = new AlertDialog.Builder(BindingWdActivity_2.this);
                alert.setTitle("是否確定綁定");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent startHearBeat = new Intent(BindingWdActivity_2.this, HeartBeatService.class);
                        startHearBeat.putExtra("bt", remoteDeviceList.get(position));
                        startService(startHearBeat);
                        //parse
                        SharedPreferences pref2 = getSharedPreferences("wecare_member", MODE_PRIVATE);
                        final String wecare_account=pref2.getString("account", null);
                        thread = new Thread(new new_equipment(wecare_account,remoteDeviceList.get(position).getName()));
                        thread.start();
                        thread2 = new Thread(new new_function(wecare_account,remoteDeviceList.get(position).getName(),"心跳","N"));
                        thread2.start();
                        AlertDialog.Builder last_check = new AlertDialog.Builder(BindingWdActivity_2.this);
                        last_check.setTitle("連結成功");
                        last_check.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(!next){
                                   finish();
                                }else{
                                    SharedPreferences.Editor check_nextpage = pref.edit();
                                    check_nextpage.putBoolean("next",false).commit();
                                    startActivity(new Intent(BindingWdActivity_2.this, BindingMtActivity_1.class));
                                }
                            }
                        });
                        last_check.show();
                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();
            }
            @Override
            public void onLongClick(View view, int position) {
//                Intent stopHearBeat = new Intent(BindingWdActivity_2.this,HeartBeatService.class);
//                stopService(stopHearBeat);
            }
        }));
        getBtAdapter();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_FOUND);
        filter.addAction(ACTION_DISCOVERY_FINISHED);
        Log.i("TAG", "register Receiver");
        registerReceiver(mReceiver, filter);


    }

    private class new_equipment implements Runnable {
        String account,equi_name;
        public new_equipment(String account,String equi_name) {
            this.account=account;
            this.equi_name=equi_name;
        }
        @Override
        public void run() {
            final ParseQuery<ParseObject> query = ParseQuery.getQuery("Equipment");
            String mark = "0";
            try {
                List<ParseObject> pList = query.find();
                for (int i = 0; i < pList.size(); i++) {
                    String DBeq = pList.get(i).getString("equi_name");
                    String DBaccount = pList.get(i).getString("account");
                    if (account.equals(DBaccount)&&equi_name.equals(DBeq)) {
                        mark = "1";
                    }
                }
            }catch (com.parse.ParseException e1) {
                e1.printStackTrace();
            }if (mark.equals("0")) {
                ParseObject memberObject = new ParseObject("Equipment");
                memberObject.put("account", String.valueOf(account));
                memberObject.put("equi_name", String.valueOf(equi_name));
                memberObject.saveInBackground();
            }
        };}

    private class new_function implements Runnable {
        String account,equi_name,func_name,share;
        public new_function(String account,String equi_name,String func_name,String share) {
            this.account=account;
            this.equi_name=equi_name;
            this.func_name=func_name;
            this.share=share;
        }
        @Override
        public void run() {
            final ParseQuery<ParseObject> query = ParseQuery.getQuery("Function");
            String mark = "0";
            try {
                List<ParseObject> pList = query.find();
                for (int i = 0; i < pList.size(); i++) {
                    String DBeq = pList.get(i).getString("equi_name");
                    String DBaccount = pList.get(i).getString("account");
                    String DBfunction = pList.get(i).getString("func_name");
                    if (account.equals(DBaccount)&&equi_name.equals(DBeq)&&func_name.equals(DBfunction)) {
                        mark = "1";
                    }
                }
            }catch (com.parse.ParseException e1) {
                e1.printStackTrace();
            }if (mark.equals("0")) {
                ParseObject memberObject = new ParseObject("Function");
                memberObject.put("account", String.valueOf(account));
                memberObject.put("equi_name", String.valueOf(equi_name));
                memberObject.put("func_name", String.valueOf(func_name));
                memberObject.put("share", String.valueOf(share));
                memberObject.saveInBackground();
            }
        };}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("TAG", " unregisterReceiver(mReceiver);");
        unregisterReceiver(mReceiver);
    }

    @Override
    public void onStart() {
        super.onStart();
        enableBluetooth();
    }

    public void SearchDevices(View view){

        if(btAdapter.isDiscovering()){
            btAdapter.cancelDiscovery();
            Log.i("TAG", "cancelDiscovery.");
        }
        btAdapter.startDiscovery();
        if(btAdapter.isDiscovering()){
            Log.i("TAG", "ISDiscovery.");
        }
        remoteDeviceList.clear();
        Log.i("TAG", "I'm clear.");
    }

    public void getBtAdapter() {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            Log.i("TAG", "Device does not support Bluetooth");
        }
    }

    private void enableBluetooth() {
        if (!btAdapter.isEnabled()) {
            Log.i("TAG", "Bluetooth isn't enable");
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_ENABLE_BT);
        } else {
            //if bluetooth is enable show bounded devices
            Log.i("TAG", "Bluetooth is enable");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            switch (resultCode) {
                case RESULT_OK:
                    Toast.makeText(this, "Bluetooth is open", Toast.LENGTH_SHORT).show();
                    Log.i("TAG", "User enable Bluetooth");
                    Log.i("TAG", "Bluetooth is enable<onActivityResult>");
                    //after bluetooth enable show bounded devices
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(this, "Please open Bluetooth", Toast.LENGTH_SHORT).show();
                    Log.i("TAG", "User doesn't enable Bluetooth");
                    enableBluetooth();
                    break;
            }
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(ACTION_FOUND.equals(action)){
                Log.i("TAG", "Find remote device");
                BluetoothDevice tmpBluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.i("TAG", tmpBluetoothDevice.getName() + ":in tmp");
                Boolean addDevice = false;
                //first remote device
                if (remoteDeviceList.size() == 0) {
                    Log.i("TAG", "There comes first remote device");
                    remoteDeviceList.add(tmpBluetoothDevice);
                    Log.i("TAG", "There are " + remoteDeviceList.get(0).getName() + "-" + remoteDeviceList.get(0).getAddress() + "is the first");

                }

                for (int i = 0; i < remoteDeviceList.size(); i++) {
                    if (tmpBluetoothDevice.equals(remoteDeviceList.get(i))) {

                        addDevice = false;//加過=flase
                        break;
                    } else addDevice = true;
                }
                Log.i("TAG", "addDevice : " + addDevice.toString());
                if (addDevice) {
                    remoteDeviceList.add(tmpBluetoothDevice);
                    for (int i = 0; i < remoteDeviceList.size(); i++) {
                        Log.i("TAG", "There are" + remoteDeviceList.get(i).getName() + "-" + remoteDeviceList.get(i).getAddress() + "now");
                    }
                }
                //addtoview
                devicesAdapter.setDeviceList(remoteDeviceList);
                Log.i("TAG", "getDevice() setDeviceList is finished");
                devicesAdapter.notifyDataSetChanged();
                Log.i("TAG", "data is changed.");
            }
            if(ACTION_DISCOVERY_FINISHED.equals(action)){
                Log("搜尋完成");
            }

        }
    };
    public void Log(String string){
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
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
