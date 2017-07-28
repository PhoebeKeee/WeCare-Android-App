package com.example.victor.mertial_test.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.victor.mertial_test.Extras.Pace.PaceService;
import com.example.victor.mertial_test.R;
import com.example.victor.mertial_test.activities.bindDivce.BindAndUnbindActivity;

import java.util.Calendar;

import static com.example.victor.mertial_test.Extras.L.l;


public class PaceFragment extends android.support.v4.app.Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView textViewSteps;
    private int numSteps;
    private PaceService.MyBinder myBinder ;
    private PaceService myService;
    private int mSteps;
    private int sum = 0;

    Calendar c1 = Calendar.getInstance();
    private SharedPreferences tmpPace;
    private SharedPreferences.Editor tmpEditor;
    private SharedPreferences zero;
    private SharedPreferences.Editor tmpZero;
    Intent intent;
    private Thread zeroThread = null;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PaceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PaceFragment newInstance(String param1, String param2) {
        PaceFragment fragment = new PaceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PaceFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent bindIntent = new Intent(getActivity(),PaceService.class);
        getActivity().bindService(bindIntent, connection, Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);
        tmpPace = getActivity().getSharedPreferences("Pace", Context.MODE_PRIVATE);
        tmpEditor = tmpPace.edit();
        zero = getActivity().getSharedPreferences("turn_zero", getActivity().MODE_PRIVATE);
//        c.add(c.DATE,1);
//        c.set(Calendar.HOUR_OF_DAY, 21);//hour+1
//        c.set(Calendar.MINUTE,0);//0
//        c.set(Calendar.SECOND, 0);
//        c.set(Calendar.MILLISECOND, 0);
//        intent = new Intent(getActivity(),AlarmReceiver.class);
//        PendingIntent pi = PendingIntent.getBroadcast(getActivity(),0,intent,0);
//        AlarmManager am = (AlarmManager)getActivity().getSystemService(getActivity().ALARM_SERVICE);
//        long interval = 1000*60*60*24;
//        am.setRepeating(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),interval,pi);
//        zero = getActivity().getSharedPreferences("turn_zero", getActivity().MODE_PRIVATE);
//        zeroThread = new Thread(new zero_detction());
//        zeroThread.start();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_pace, container, false);
        textViewSteps = (TextView) layout.findViewById(R.id.textStep);
        return layout;
    }



    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            l("in the onServiceConnected");
            myBinder = (PaceService.MyBinder)service;
            myService = myBinder.getService();
            myService.registerCallback(mCallback);
            textViewSteps.setText(myService.get_mSteps()+"");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myService = null;
        }
    };


    private PaceService.ICallback mCallback = new PaceService.ICallback(){

        @Override
        public void stepsChanged(int value) {
            Message msg = Message.obtain();
            msg.arg1 = value;
            mHandler.sendMessage(msg);
        }
    };


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mSteps = msg.arg1;
            textViewSteps.setText(mSteps + "");
            l(mSteps + "");
            if(!zero.getBoolean("zero",false))
            tmpEditor.putInt("pace",mSteps).commit();
            super.handleMessage(msg);
        }
    };

    private BindAndUnbindActivity.ICallback rCallback = new BindAndUnbindActivity.ICallback(){
        @Override
        public void stop() {
            getActivity().unbindService(connection);
        }

        @Override
        public void stoph() {

        }
    };




}
