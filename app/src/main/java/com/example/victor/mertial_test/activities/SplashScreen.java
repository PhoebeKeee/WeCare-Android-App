package com.example.victor.mertial_test.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.victor.mertial_test.Extras.L;
import com.example.victor.mertial_test.R;
import com.example.victor.mertial_test.activities.member.MainActivity;
import com.example.victor.mertial_test.activities.myCare.HomeActivity;

import static com.example.victor.mertial_test.Extras.L.l;


/**
 * Created by Phoebe on 15/9/3.
 */
public class SplashScreen extends Activity {

    private SharedPreferences isServiceAive;
    private SharedPreferences.Editor editor_H;
    private boolean heartbeat_is_running;

    private SharedPreferences isPaceAive;
    private SharedPreferences.Editor editor_P;
    private boolean pace_is_running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        isServiceAive = getSharedPreferences("Servicelife", Context.MODE_PRIVATE);
        heartbeat_is_running = isServiceAive.getBoolean("alive", false);
        L.l("heartbeat_is_running :" + heartbeat_is_running);
        isPaceAive = getSharedPreferences("Pacelife", Context.MODE_PRIVATE);
        pace_is_running = isPaceAive.getBoolean("alive", false);
        L.l("pace_is_running :" + pace_is_running);
        // delaying the hiding of the ActionBar 經紀人

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(2000);
                    //2000 => 2 seconds
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{

                    if(isServiceAive.getBoolean("alive", false) || isPaceAive.getBoolean("alive",false)) {
                        l("inside");
                        Intent intent = new Intent(SplashScreen.this,HomeActivity.class);
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(SplashScreen.this,MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        };
        timerThread.start();
    }


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }
}
