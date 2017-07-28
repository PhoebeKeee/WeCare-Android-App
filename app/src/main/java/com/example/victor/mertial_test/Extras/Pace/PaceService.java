package com.example.victor.mertial_test.Extras.Pace;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.example.victor.mertial_test.Extras.AlarmReceiver;
import com.example.victor.mertial_test.R;
import com.example.victor.mertial_test.activities.SplashScreen;

import java.util.Calendar;

import static com.example.victor.mertial_test.Extras.L.l;

/**
 * Created by Victor on 2015/10/17.
 */
public class PaceService extends Service {

    private SensorManager sensorManager;
    private StepDetector mStepDetector;
    private Sensor mSensor;
    private StepDisplayer mStepDisplayer;
    private SharedPreferences isPaceAlive;
    private SharedPreferences.Editor editor;
    private boolean test;
    public int get_mSteps() {
        return mSteps;
    }
    public void set_mSteps(int mSteps) {
        mStepDisplayer.setSteps(mSteps);
    }
    private int mSteps;
    Intent intent;
    private SharedPreferences tmpPace;
    private SharedPreferences.Editor tmpEditor;
    private SharedPreferences zero;
    private SharedPreferences.Editor tmpZero;
    private Thread zeroThread = null;
    Calendar c = Calendar.getInstance();
    private MyBinder myBinder = new MyBinder();


    @Override
    public void onCreate() {
        super.onCreate();
        isPaceAlive = getSharedPreferences("Pacelife", Context.MODE_PRIVATE);
        editor = isPaceAlive.edit();
        editor.remove("Pacelife");
        editor.putBoolean("alive", true);
        editor.commit();
        test = isPaceAlive.getBoolean("alive",true);

        l("PaceService : onCreate : "+test);
        //Start detecting
        mStepDetector = new StepDetector();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        registerDetector();
        mStepDisplayer = new StepDisplayer();
        mStepDisplayer.setSteps(0);
        mStepDisplayer.addListener(mStepListener);
        mStepDetector.addStepListener(mStepDisplayer);

        c.add(c.DATE, 1);
        c.set(Calendar.HOUR_OF_DAY, 21);//hour+1
        c.set(Calendar.MINUTE,0);//0
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        intent = new Intent(this,AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this,1,intent,0);
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        long interval = 1000*60*60*24;
        am.setRepeating(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),interval,pi);
        zero = getSharedPreferences("turn_zero", MODE_PRIVATE);
        tmpZero = zero.edit();
        zeroThread = new Thread(new zero_detction());
        zeroThread.start();

    }

    @Override

    public int onStartCommand(Intent intent, int flags, int startId) {
        l("PaceService : onStartCommand");
        showNotification();

        return START_NOT_STICKY;
    }




    @Override
    public void onDestroy() {
        isPaceAlive = getSharedPreferences("Pacelife",Context.MODE_PRIVATE);
        editor = isPaceAlive.edit();
        editor.remove("Pacelife");
        editor.putBoolean("alive", false);
        editor.commit();
        test = isPaceAlive.getBoolean("alive",true);
        l("PaceService : onDestroy"+test);
        unregisterDetector();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }


    public class MyBinder extends Binder {
        public PaceService getService(){
            l("PaceService : getService");
            return PaceService.this;
        }
    }

    public void test(){
        l("test()");
    }
    private void registerDetector() {
        mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(mStepDetector, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void unregisterDetector() {
        sensorManager.unregisterListener(mStepDetector);
    }

    private StepDisplayer.Listener mStepListener = new StepDisplayer.Listener(){
        @Override
        public void stepsChanged(int value) {
            mSteps = value;
            passValue();
        }
        @Override
        public void passValue() {
            if (mCallback != null) {
                mCallback.stepsChanged(mSteps);
            }
        }

    };

    public interface ICallback{
        public void stepsChanged(int value);
    }

    private ICallback mCallback;

    public void registerCallback(ICallback cb){
        mCallback = cb;
    }

    private void showNotification() {
        CharSequence text = getText(R.string.app_name);
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notification_icon_pace)
                        .setContentTitle(text)
                        .setContentText("THE counter is running.");
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

    private class zero_detction implements Runnable {

        @Override
        public void run() {
            while(true){
                if(zero.getBoolean("zero",false)){
                    l("zero_detction : "+zero.getBoolean("zero",false)+"");
                    set_mSteps(0);
                    tmpZero.putBoolean("zero",false).commit();
                }
            }
        };}
}
