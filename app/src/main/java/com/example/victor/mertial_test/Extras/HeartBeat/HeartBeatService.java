package com.example.victor.mertial_test.Extras.HeartBeat;

import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.example.victor.mertial_test.R;
import com.example.victor.mertial_test.activities.SplashScreen;
import com.parse.ParseObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static com.example.victor.mertial_test.Extras.L.l;


/**
 * Created by Victor on 2015/10/15.
 */
public class HeartBeatService extends Service{

    private BluetoothDevice bluetoothDevice;
    private ConnectThread connectThread;
    private HandlerThread timeThread = null;
    private Handler timeHandler = null;
    private Thread ReadThread = null;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;
    public SharedPreferences isServiceAlive ;
    public SharedPreferences.Editor editor;
    private Thread uploadThread = null;
//    DatabaseAdapter databaseAdapter;

    private MyBinder myHeartBinder = new MyBinder();

    public String get_mHeartBeat() {
        return mHeartBeat;
    }

    private String mHeartBeat ="go";
    private HeartDisplayer mHeartDisplayer;

    public class MyBinder extends Binder {
        public HeartBeatService getService(){
            l("MyService : getService");
            return HeartBeatService.this;
        }
    }

    private ArrayList<HeartListener> mHeartListeners = new ArrayList<HeartListener>();

    public void addStepListener(HeartListener hl) {
        mHeartListeners.add(hl);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isServiceAlive= getSharedPreferences("Servicelife", Context.MODE_PRIVATE);
        editor = isServiceAlive.edit();
        editor.remove("alive");
        editor.putBoolean("alive",true);
        editor.commit();
        mHeartDisplayer = new HeartDisplayer();
        mHeartDisplayer.onHeart("尚未有值");
        mHeartDisplayer.addListener(mHeartListener);
        addStepListener(mHeartDisplayer);
    }

    private HeartDisplayer.Listener mHeartListener = new HeartDisplayer.Listener(){
        @Override
        public void HeartsChanged(String value) {
            mHeartBeat = value;
            passValue();
        }
        @Override
        public void passValue() {
          if(mCallback != null){
              mCallback.heartBeatChanged(mHeartBeat);
          }
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myHeartBinder;
    }

    public interface ICallback{
        public void heartBeatChanged(String value);
    }

    private ICallback mCallback;

    public void registerCallback(ICallback cb){
        mCallback = cb;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bluetoothDevice = intent.getParcelableExtra("bt");
        SharedPreferences device_name = getSharedPreferences("Device_name",MODE_PRIVATE);
        SharedPreferences.Editor editor1 = device_name.edit();
        editor1.putString("device_name",bluetoothDevice.getName());
        editor1.commit();
        showNotification();
        connectThread = new ConnectThread(bluetoothDevice);
        connectThread.start();
        timeThread = new HandlerThread("time");
        timeThread.start();
        timeHandler = new Handler(timeThread.getLooper());
        l(bluetoothDevice.getName());


        return START_NOT_STICKY;
    }

    private class ConnectThread extends Thread{

        private BluetoothSocket btSocket = null;
        private BluetoothDevice btDevice = null;

        ConnectThread(BluetoothDevice device){
            this.btDevice = device;
        }

        @Override
        public void run() {
            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                Method m = null;
                try {
                    m = btDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                try {
                    btSocket = (BluetoothSocket)m.invoke(btDevice, Integer.valueOf(1));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                btSocket.connect();
                Log.i("TAG", "Get connect");

            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                Log.i("TAG",connectException.toString());
                try {
                    btSocket.close();
                } catch (IOException closeException) { }
                return;
            }

            try {
                Log.i("TAG","Get i/o Stream");
                inputStream = btSocket.getInputStream();
                outputStream = btSocket.getOutputStream();
                timeHandler.post(DataWriteThread);
                ReadThread = new Thread(DataReadThread);
                ReadThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void cancel() {
            try {
                Log.i("TAG","Bluetooth socket close");
                btSocket.close();
            } catch (IOException e) { }
        }
    }
    private Runnable DataWriteThread = new Runnable() {
        @Override
        public void run() {
            String w ="w\n";
            byte[] buffer = w.getBytes();

            try {
                outputStream.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            timeHandler.postDelayed(DataWriteThread,60000);
        }
    };
    private Runnable DataReadThread = new Runnable() {
        @Override
        public void run() {
            StringBuilder sb = new StringBuilder();
            String end= "\n";
            byte[] buffer = new byte[10];
            int bytes;
            while (true){
                try {
                    while(-1 != (bytes = inputStream.read(buffer) )){
                        sb.append(new String(buffer, 0 ,bytes, Charset.forName("ISO_8859-1")));
                        int endIdx = sb.indexOf(end);
                        if(endIdx != -1){
                            String fullMessage = sb.substring(0,endIdx+end.length());
                            sb.delete(0,endIdx+end.length());
                            Log.i("TAG", "HeartBeat:" + fullMessage);
                            for (HeartListener heartListener : mHeartListeners) {
                                heartListener.onHeart(String.valueOf((int)(Math.random() * 13 + 72)));
                            }
                          //upload parse
                            SharedPreferences pref2 = getSharedPreferences("wecare_member", MODE_PRIVATE);
                            final String wecare_account=pref2.getString("account", null);
                            uploadThread = new Thread(new new_digital(wecare_account,bluetoothDevice.getName(),"心跳",fullMessage));
                            uploadThread.start();
                        }
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        isServiceAlive= getSharedPreferences("Servicelife", Context.MODE_PRIVATE);
        editor = isServiceAlive.edit();
        editor.remove("alive");
        editor.putBoolean("alive", false);
        editor.commit();
        Boolean b = isServiceAlive.getBoolean("alive",false);
        l(b + "");
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        timeHandler.removeCallbacks(DataWriteThread);
        timeThread.quit();
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connectThread.cancel();
    }



    private void showNotification(){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notification_icon_heart)
                        .setContentTitle("WE CARE")
                        .setContentText("THE DEVICE "+bluetoothDevice.getName()+" is running.");
        Intent resultIntent = new Intent(this, SplashScreen.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(SplashScreen.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        startForeground(100, mBuilder.build());
    }
    private class new_digital implements Runnable {
        String account,equi_name,func_name,data;
        public new_digital(String account,String equi_name,String func_name,String data) {
            this.account=account;
            this.equi_name=equi_name;
            this.func_name=func_name;
            this.data=data;
        }
        @Override
        public void run() {
            ParseObject memberObject = new ParseObject("Heartbeat");
            memberObject.put("account", String.valueOf(account));
            memberObject.put("equi_name", String.valueOf(equi_name));
            memberObject.put("data", String.valueOf((int)(Math.random() * 13 + 72)));
            memberObject.saveInBackground();
        };}
    }

