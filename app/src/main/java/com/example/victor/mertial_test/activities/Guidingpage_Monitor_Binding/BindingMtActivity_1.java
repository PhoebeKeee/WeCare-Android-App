package com.example.victor.mertial_test.activities.Guidingpage_Monitor_Binding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.example.victor.mertial_test.R;
import com.example.victor.mertial_test.activities.myCare.HomeActivity;

public class BindingMtActivity_1 extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bindingmonitor);
        SharedPreferences pref = getSharedPreferences("monitor_id", MODE_PRIVATE);
        SharedPreferences.Editor editor1 = pref.edit();
        editor1.putString("monitor_id", "non_monitor");
        editor1.commit();
    }

    public void start_bindingMonitor(View view){
     startActivity(new Intent(this,BindingMtActivity_2.class));
    }

    public void skip_bindingMonitor(View view){
        startActivity(new Intent(this, HomeActivity.class));
    }


}
