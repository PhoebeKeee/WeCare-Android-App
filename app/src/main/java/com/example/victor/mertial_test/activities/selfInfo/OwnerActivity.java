package com.example.victor.mertial_test.activities.selfInfo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.victor.mertial_test.Extras.Pace.PaceService;
import com.example.victor.mertial_test.R;
import com.example.victor.mertial_test.fragments.BloodPressureFragment;
import com.example.victor.mertial_test.fragments.HeartFragment;
import com.example.victor.mertial_test.fragments.PaceFragment;

public class OwnerActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private ViewPager mPager;
    private MyPagerAdapter adapter;
    private static final int HEART_PAGE =0;
    private static final int PACE_PAGE =1;
    private static final int BLOOD_PRESSURE_PAGE =2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences getmyaccount=getSharedPreferences("wecare_member", Context.MODE_PRIVATE);
        String account = getmyaccount.getString("account", null);
        String myname = account;
        SharedPreferences.Editor longdataname = getSharedPreferences("longdataname", 0).edit();
        longdataname.putString("name",myname);
        longdataname.commit();
        setContentView(R.layout.activity_owner);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPager = (ViewPager) findViewById(R.id.OwnerDataPager);
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(adapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
            }
        });
        Intent startIntent = new Intent(this,PaceService.class);
        startService(startIntent);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_owner, menu);
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
        if(id == R.id.long_info){
            startActivity(new Intent(this,UserDataActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {


        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch(position){
                case HEART_PAGE:
                    fragment = HeartFragment.newInstance(" ", " ");
                    break;
                case PACE_PAGE:
                    fragment = PaceFragment.newInstance(" ", " ");
                    break;
                case BLOOD_PRESSURE_PAGE:
                    fragment = BloodPressureFragment.newInstance(" ", " ");
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }


    }
}
