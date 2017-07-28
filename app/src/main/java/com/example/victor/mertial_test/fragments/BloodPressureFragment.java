package com.example.victor.mertial_test.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.victor.mertial_test.R;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import static com.example.victor.mertial_test.Extras.L.l;

public class BloodPressureFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

   private TextView textView;
    private TextView textView1;
   private String monitor_id;
    private String account;
    private  Thread thread2;
    private Handler handler;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BloodPressureFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BloodPressureFragment newInstance(String param1, String param2) {
        BloodPressureFragment fragment = new BloodPressureFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public BloodPressureFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        SharedPreferences pref2 = getActivity().getSharedPreferences("monitor_id", Context.MODE_PRIVATE);
        SharedPreferences pref3 = getActivity().getSharedPreferences("wecare_member", Context.MODE_PRIVATE);
        monitor_id =pref2.getString("monitor_id", null);
        account = pref3.getString("account",null);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout =  inflater.inflate(R.layout.fragment_blood_pressure, container, false);
        textView = (TextView)layout.findViewById(R.id.blood_pressure_1);
        textView1 = (TextView)layout.findViewById(R.id.blood_pressure_2);
        if(!monitor_id.equals("non_monitor")){
            thread2 = new Thread(new product(account,monitor_id));
            thread2.start();
            handler = new Handler(){
                @Override
            public void handleMessage(Message msg){
                    textView.setText("收縮壓Systolic  ： "+msg.arg1);
                    textView1.setText("舒張壓Diastolic ： "+msg.arg2);
                }
            };
        }
        else{
            textView.setText("目前暫無綁定雲端裝置");
            textView1.setText("");
        }
        return layout;
    }

    private class product implements Runnable {
        String account,id;
        public product(String account,String id) {
            this.account=account;
            this.id=id;
        }
        @Override
        public void run() {
            final ParseQuery<ParseObject> query = ParseQuery.getQuery("Bloodpressure");
            try {
                query.whereEqualTo("account", account);
                List<ParseObject> pList = query.find();
                int Systolic_pressure=  Integer.valueOf(pList.get(pList.size() - 1).getString("s_data"));
                int Diastolic_pressure= Integer.valueOf(pList.get(pList.size() - 1).getString("d_data"));
                l(Systolic_pressure+"");
                l(Diastolic_pressure + "");
                Message msg = Message.obtain();
                msg.arg1 = Systolic_pressure;
                msg.arg2 = Diastolic_pressure;
                handler.sendMessage(msg);
            }catch (com.parse.ParseException e1) {
                e1.printStackTrace();
            }
        };}
}
