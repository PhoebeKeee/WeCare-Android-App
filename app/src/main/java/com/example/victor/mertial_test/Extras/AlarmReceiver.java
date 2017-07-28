package com.example.victor.mertial_test.Extras;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.parse.ParseObject;

import static com.example.victor.mertial_test.Extras.L.l;

/**
 * Created by Victorlin on 2015/8/29.
 */
public class AlarmReceiver extends BroadcastReceiver{
    int sum;
    SharedPreferences tmpPace;
    SharedPreferences.Editor tmpEditor;
    SharedPreferences zero;
    SharedPreferences.Editor zeroEditor;
    private Thread uploadThread = null;
    @Override
    public void onReceive(Context context, Intent intent) {
        tmpPace= context.getSharedPreferences("Pace", Context.MODE_PRIVATE);
        sum =  tmpPace.getInt("pace", -1);
        l("AlarmReceiver : " + sum);
        SharedPreferences pref2 = context.getSharedPreferences("wecare_member", context.MODE_PRIVATE);
        final String wecare_account=pref2.getString("account", null);
        uploadThread = new Thread(new new_digital(wecare_account,"計步器",String.valueOf(sum),context));
        uploadThread.start();

    }
    private class new_digital implements Runnable {
        String account,equi_name,data;
        Context context;
        public new_digital(String account,String equi_name,String data,Context context) {
            this.account=account;
            this.equi_name=equi_name;
            this.data=data;
            this.context = context;
        }
        @Override
        public void run() {
            l("uploadThread");
            ParseObject memberObject = new ParseObject("Pace");
            memberObject.put("account", String.valueOf(account));
            memberObject.put("equi_name", String.valueOf(equi_name));
            memberObject.put("data", String.valueOf(data).trim());
            memberObject.saveInBackground();
            zero = context.getSharedPreferences("turn_zero",context.MODE_PRIVATE);
            zeroEditor = zero.edit();
            zeroEditor.putBoolean("zero",true).commit();
        };}
    }

