package com.example.victor.mertial_test.activities.member;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.victor.mertial_test.R;
import com.parse.Parse;


public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set Parse key
//        SharedPreferences pref = getSharedPreferences("wecare_member", MODE_PRIVATE);
//        l("isLogged now is " + pref.getBoolean("isLogged", false)+"");
//         if(!pref.getBoolean("isLogged",false)) {
             Parse.initialize(this, "t545IFUN8GXNUYSAdsuS8PYEtC29DtVn7IWq7p2s", "MnFAYZ1fZBeADUEPkrxYQSeslLap2fdZv1wkoqxQ");
//             l("isLogged is false");
//         }
        //}
        //change back button function
        SharedPreferences pref3 = getSharedPreferences("backbutton", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref3.edit();
        editor.putBoolean("inhibit_return", false);
        editor.commit();
        // change page

        Intent intent = new Intent();
        intent.setClass(MainActivity.this, login.class);
        startActivity(intent);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}