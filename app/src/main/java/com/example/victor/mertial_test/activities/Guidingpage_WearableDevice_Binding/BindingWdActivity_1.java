package com.example.victor.mertial_test.activities.Guidingpage_WearableDevice_Binding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.example.victor.mertial_test.R;
import com.example.victor.mertial_test.activities.Guidingpage_Monitor_Binding.BindingMtActivity_1;

import static com.example.victor.mertial_test.Extras.L.l;

public class BindingWdActivity_1 extends ActionBarActivity {

   private SharedPreferences go_to_next;
    private SharedPreferences.Editor editor_G;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bindingwd_1);
        SharedPreferences isServieAlive = getSharedPreferences("Servicelife",MODE_PRIVATE);
        SharedPreferences.Editor editor = isServieAlive.edit();
        editor.putBoolean("alive",false);
        editor.commit();
        Boolean b =isServieAlive.getBoolean("alive",false);
        l(b + "");
        go_to_next = getSharedPreferences("nextpage_check",MODE_PRIVATE);
        editor_G = go_to_next.edit();
        editor_G.putBoolean("next",true).commit();// go to wd2

    }


    public void start_bindingWD(View view){
        startActivity(new Intent(this,BindingWdActivity_2.class));
    };

    public void skip_bindingWD(View view){
        startActivity(new Intent(this, BindingMtActivity_1.class));
    };
}
