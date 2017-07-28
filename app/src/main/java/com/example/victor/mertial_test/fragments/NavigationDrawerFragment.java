package com.example.victor.mertial_test.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.victor.mertial_test.R;
import com.example.victor.mertial_test.activities.bindDivce.BindAndUnbindActivity;
import com.example.victor.mertial_test.activities.selfInfo.OwnerActivity;
import com.example.victor.mertial_test.activities.familymember.FamilyActivity;
import com.example.victor.mertial_test.activities.healthreport.Healthpersonal;
import com.example.victor.mertial_test.activities.setting.SettingActivity;
import com.example.victor.mertial_test.apapters.DrawerListAdapter;
import com.example.victor.mertial_test.pojo.Information;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment {

    private RecyclerView recyclerView;
    public static final String PREF_FILE_NAME="testpref";
    public static final String KEY_USER_LEARNED_DRAWER="user_learned_drawer";
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private DrawerListAdapter adapter;
    private boolean mUserLearnedDrawer;//frament exist or not
    private boolean mFromSavedInstanceState;//the first time or not
    private View containerView;
    public boolean b =false;

    public NavigationDrawerFragment() {
        // Required empty public constructor


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer=Boolean.valueOf(readFromPreference(getActivity(),KEY_USER_LEARNED_DRAWER,"false"));//user has never open the drawer
        if(savedInstanceState!=null){
           mFromSavedInstanceState=true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        recyclerView =(RecyclerView)layout.findViewById(R.id.drawerList);
        adapter = new DrawerListAdapter(getActivity(),getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
               switch (position){
                   case 0:
                       b=true;
                       if(b){
                           Log.i("TAG","come in");
                           mDrawerLayout.closeDrawer(containerView);
                           b = false;
                           mDrawerLayout.setDrawerListener(mDrawerToggle);
                           mDrawerLayout.post(new Runnable() {
                               @Override
                               public void run() {
                                   mDrawerToggle.syncState();
                               }
                           });
                       }
                    break;
                   case 1:
                        startActivity(new Intent(getActivity(),OwnerActivity.class));
                        break;
                   case 2:
                       startActivity(new Intent(getActivity(),FamilyActivity.class));
                       break;
                   case 3:
                       startActivity(new Intent(getActivity(),Healthpersonal.class));
                       break;
                   case 4:
                       startActivity(new Intent(getActivity(), BindAndUnbindActivity.class));
                       break;
                   case 5:
                       startActivity(new Intent(getActivity(), SettingActivity.class));
                       break;

               }

            }

            @Override
            public void onLongClick(View view, int position) {
               // Toast.makeText(getActivity(),"onLongClick "+position,Toast.LENGTH_SHORT).show();
            }
        }));
        return layout;
    }

    public static List<Information> getData(){
        List<Information> data = new ArrayList<>();
        int[] icons={R.drawable.home1,R.drawable.personal2,R.drawable.adults3,R.drawable.business133,R.drawable.bindingwd_icon,R.drawable.settings6};
        String [] titles={"首頁","個人健康資訊","健康家庭","個人每日報表","綁定裝置","設定"};
        for(int i =0;i<icons.length&&i<titles.length;i++){
          Information current = new Information();
            current.iconId=icons[i];
            current.title=titles[i];
            data.add(current);
        }
        return data;
    }

    public void setUp(int fragmentId,DrawerLayout drawerLayout, Toolbar toolbar){
        containerView=getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if(!mUserLearnedDrawer){
                   mUserLearnedDrawer=true;
                    saveToPreference(getActivity(),KEY_USER_LEARNED_DRAWER,mUserLearnedDrawer+"");
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }
        };

        if(!mUserLearnedDrawer&& !mFromSavedInstanceState){
            mDrawerLayout.openDrawer(containerView);
        }



        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public void saveToPreference(Context context,String preferenceName,String preferenceValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName,preferenceValue);
        editor.apply();//faster than commit
    }
    public static String readFromPreference(Context context,String preferenceName,String defaultValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName,defaultValue);
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private GestureDetector gestureDetector;
        private ClickListener clickListener;
        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener){
            Log.i("TAG","constructor invoked ");
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
        public void onClick(View view,int position);
        public void onLongClick(View view,int position);
    }

}
