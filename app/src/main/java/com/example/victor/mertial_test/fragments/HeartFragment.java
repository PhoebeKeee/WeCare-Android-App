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

import com.example.victor.mertial_test.Extras.HeartBeat.HeartBeatService;
import com.example.victor.mertial_test.R;
import com.example.victor.mertial_test.activities.bindDivce.BindAndUnbindActivity;

import static com.example.victor.mertial_test.Extras.L.l;


public class HeartFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView heartText;
    private HeartBeatService myService;
    private HeartBeatService.MyBinder myBinder;
    public SharedPreferences isServiceAlive ;
    public SharedPreferences.Editor editor;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HeartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HeartFragment newInstance(String param1, String param2) {
        HeartFragment fragment = new HeartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public HeartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        isServiceAlive = getActivity().getSharedPreferences("Servicelife",Context.MODE_PRIVATE);
        editor = isServiceAlive.edit();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_heart, container, false);
        heartText = (TextView)layout.findViewById(R.id.heart);
        if(isServiceAlive.getBoolean("alive", false)==true){
            Intent bindIntent = new Intent(getActivity(),HeartBeatService.class);
            getActivity().bindService(bindIntent, connection, Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);
        }else{
            heartText.setText("未有裝置連接");
        }
        return layout;
    }
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            l("in the onServiceConnected");
            myBinder = (HeartBeatService.MyBinder)service;
            myService = myBinder.getService();
            myService.registerCallback(mCallback);
            heartText.setText(myService.get_mHeartBeat());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myService = null;
        }
    };

    private HeartBeatService.ICallback mCallback = new HeartBeatService.ICallback(){

        @Override
        public void heartBeatChanged(String value) {
            Message msg = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putString("heart",value);
            msg.setData(bundle);
            mHandler.sendMessage(msg);
        }
    };

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle b=msg.getData();
            heartText.setText(b.getString("heart"));
            super.handleMessage(msg);
        }
    };

    private BindAndUnbindActivity.ICallback rCallback = new BindAndUnbindActivity.ICallback(){
        @Override
        public void stop() {

        }

        @Override
        public void stoph() {
            getActivity().unbindService(connection);
        }
    };


}
