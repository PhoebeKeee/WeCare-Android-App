package com.example.victor.mertial_test.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.victor.mertial_test.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FragmentWeek extends android.support.v4.app.Fragment {
    ArrayList<String> function;
    ProgressDialog dialog;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentWeek.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentWeek newInstance(String param1, String param2) {
        FragmentWeek fragment = new FragmentWeek();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentWeek() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences getmyaccount,getotheraccount;
        getmyaccount = getActivity().getSharedPreferences("wecare_member", Context.MODE_PRIVATE);
        String account = getmyaccount.getString("account", null);
        String myname = account;
        getotheraccount = getActivity().getSharedPreferences("longdataname", Context.MODE_PRIVATE);
        String otheraccount = getotheraccount.getString("name", null);
        String othername = otheraccount;

        // Inflate the layout for this fragment

        View layout = inflater.inflate(R.layout.fragment_fragment_week, container, false);
        function = new ArrayList<>();
        if(othername == myname){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Function");
        query.whereEqualTo("account", myname );
        try {
            List<ParseObject> pList = query.find();
            int a = query.count();

            for (int i = 0; i < a; i++) {
                    function.add(pList.get(i).getString("func_name"));
            }

        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }
        }else{
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Function");
            query.whereEqualTo("account", othername );
            try {
                List<ParseObject> pList = query.find();
                int a = query.count();

                for (int i = 0; i < a; i++) {
                    if (pList.get(i).getString("share").equals("Y")) {
                        function.add(pList.get(i).getString("func_name"));
                    }
                }

            } catch (com.parse.ParseException e) {
                e.printStackTrace();
            }
        }

        for (int a = 0; a < function.size(); a++) {
            if (function.get(a).toString().equals("步行數")) {
                //--------------------下方為長條圖----------------------
                BarChart Barchart = (BarChart) layout.findViewById(R.id.barchart);
                BarData data = null;
                try {
                    data = new BarData(getXAxisValues(), getDataSet());
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (com.parse.ParseException e) {
                    e.printStackTrace();
                }
                Barchart.setData(data);
                Barchart.animateXY(1, 3000);
                Barchart.invalidate();
                Barchart.setDescription("");

                XAxis xAxis;
                //xAxis.setDrawGridLines(false);
                //YAxis leftYAxis = Barchart.getAxisLeft();
                //leftYAxis.setDrawGridLines(false);
                //YAxis RightYAxis =Barchart.getAxisRight();
                //RightYAxis.setEnabled(false);
                // 不顯示右側
                xAxis = Barchart.getXAxis();
                xAxis.setDrawGridLines(false);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawLabels(true);
                //(將月份顯示於下方)
                Barchart.setVisibility(View.VISIBLE);
            }
            if (function.get(a).toString().equals("血壓")) {
                //--------------------下方為折線圖----------------------
                LineChart Linechart = (LineChart) layout.findViewById(R.id.linechart);
                LineData data1 = null;
                try {
                    data1 = new LineData(xVals(), dataSetList());
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (com.parse.ParseException e) {
                    e.printStackTrace();
                }
                Linechart.setData(data1);
                Linechart.animateXY(2, 2);
                Linechart.setDescription("");
                Linechart.invalidate();
                Linechart.setVisibility(View.VISIBLE);
                XAxis xVals = Linechart.getXAxis();
                xVals.setPosition(XAxis.XAxisPosition.BOTTOM);
                xVals.setDrawLabels(true);

            }
            if (function.get(a).toString().equals("心跳")) {
                //--------------------下方為第二個折線圖----------------------
                LineChart Linechart2 = (LineChart) layout.findViewById(R.id.linechart2);
                LineData data2 = null;
                try {
                    data2 = new LineData(xVals2(), dataSetList2());
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (com.parse.ParseException e) {
                    e.printStackTrace();
                }
                Linechart2.setData(data2);
                Linechart2.animateXY(2, 2);
                Linechart2.setDescription("");
                Linechart2.invalidate();
                Linechart2.setVisibility(View.VISIBLE);

                XAxis xVals = Linechart2.getXAxis();
                xVals.setPosition(XAxis.XAxisPosition.BOTTOM);
                xVals.setDrawLabels(true);
            }
        }
        return layout;
    }


    //上面部分，設定圖表部分；下面部分，圖表數值取得

    //--------------------下方為計步-長條圖----------------------
    private ArrayList<BarDataSet> getDataSet ()throws com.parse.ParseException {
//        SharedPreferences getaccount;
//        getaccount = getActivity().getSharedPreferences("wecare_member", Context.MODE_PRIVATE);
//        String account = getaccount.getString("account", null);
//        String name = account;
//        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
//        Calendar calendar = Calendar.getInstance();//(取得現在時間)
//        int firstdayofweek = calendar.getFirstDayOfWeek();
//        calendar.set(Calendar.DAY_OF_WEEK, firstdayofweek);
//        Date dt = new Date(calendar.getTimeInMillis());
//        calendar.setTime(dt);
//        //-----------------------------------------------------------------------------------------------------------------------
        ArrayList<BarDataSet> dataSets = null;
//
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
//        //------------------------------------------------------------------------------------------------------------------------
//
//        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Week_searchdata");
//        int l = 0;
//        loop1:
//        for (int d2 = 1; d2 < 8; d2++) {
//            Date tdt1 = calendar.getTime();
//            String time1 = sdf.format(tdt1);
//            calendar.add(Calendar.DATE, 1);
//            query1.whereEqualTo("date", time1);
//            query1.whereEqualTo("account", name);
//            query1.whereEqualTo("func_name", "步行數");
//            List<ParseObject> pList1 = query1.find();
//            int a = query1.count();
//            if (a == 0) {
//                valueSet1.add(new BarEntry(0, l));
//                l++;
//            } else {
//                loop2:
//                for (int i = 0; i < a; i++) {
//                    valueSet1.add(new BarEntry(Float.parseFloat(pList1.get(i).getString("data")), l));
//                    l++;
//                    continue loop1;
//                }
//            }
//        }
        valueSet1.add(new BarEntry(4651, 0));
        valueSet1.add(new BarEntry(1535, 1));
        valueSet1.add(new BarEntry(1538, 2));
        valueSet1.add(new BarEntry(783, 3));
        valueSet1.add(new BarEntry(7413, 4));
        valueSet1.add(new BarEntry(4230, 5));
        valueSet1.add(new BarEntry(0, 6));


        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "走路步數");
        barDataSet1.setColor(Color.rgb(155, 0, 0));
        barDataSet1.setValueTextSize(13);
        barDataSet1.setValueTextColor(Color.rgb(0, 155, 0));


        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);


        return dataSets;
    }

    private ArrayList<String> getXAxisValues ()throws ParseException {
        //定義好時間字串的格式
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
        Calendar calendar = Calendar.getInstance();//(取得現在時間)
        int firstdayofweek = calendar.getFirstDayOfWeek();
        calendar.set(Calendar.DAY_OF_WEEK, firstdayofweek);
        Date dt = new Date(calendar.getTimeInMillis());
        calendar.setTime(dt);
        ArrayList<String> xAxis = new ArrayList<>();
        for (int d2 = 1; d2 < 8; d2++) {
            Date tdt1 = calendar.getTime();//取得加減過後的Date
            //依照設定格式取得字串
            String time1 = sdf.format(tdt1);
            calendar.add(Calendar.DATE, 1);
            xAxis.add(time1);
        }
        return xAxis;
    }


    //--------------------下方為血壓-折線圖----------------------

    private ArrayList<LineDataSet> dataSetList ()throws com.parse.ParseException {
//        SharedPreferences getaccount;
//        getaccount = getActivity().getSharedPreferences("wecare_member", Context.MODE_PRIVATE);
//        String account = getaccount.getString("account", null);
//        String name = account;
//        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
//        Calendar calendar = Calendar.getInstance();//(取得現在時間)
//        int firstdayofweek = calendar.getFirstDayOfWeek();
//        calendar.set(Calendar.DAY_OF_WEEK, firstdayofweek);
//        Date dt = new Date(calendar.getTimeInMillis());
//        calendar.setTime(dt);
//        //-----------------------------------------------------------------------------------------------------------------------
        ArrayList<Entry> yVals1 = new ArrayList<>();

        ArrayList<LineDataSet> dataSetList = new ArrayList<>();
//        //------------------------------------------------------------------------------------------------------------------------
//
//        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Week_searchdata");
//        int l = 0;
//        loop1:
//        for (int d2 = 1; d2 < 8; d2++) {
//            Date tdt1 = calendar.getTime();
//            String time1 = sdf.format(tdt1);
//            query2.whereEqualTo("date", time1);
//            query2.whereEqualTo("account", name);
//            query2.whereEqualTo("func_name", "血壓");
//            calendar.add(Calendar.DATE, 1);
//            List<ParseObject> pList1 = query2.find();
//            int a = query2.count();
//            if (a == 0) {
//                yVals1.add(new Entry(0, l));
//                l++;
//            } else {
//                loop2:
//                for (int i = 0; i < a; i++) {
//
//                    yVals1.add(new Entry(Float.parseFloat(pList1.get(i).getString("data")), l));
//                    l++;
//
//                }
//            }
//        }

        yVals1.add(new Entry(1, 0));
        yVals1.add(new Entry(2, 1));
        yVals1.add(new Entry(3, 2));
        yVals1.add(new Entry(0, 3));
        yVals1.add(new Entry(2, 4));
        yVals1.add(new Entry(2, 5));
        yVals1.add(new Entry(0, 6));

        String dataset_label1 = "異常次數";
        LineDataSet dataSet1 = new LineDataSet(yVals1, dataset_label1);
        dataSet1.setColor(Color.rgb(0, 155, 0));
        dataSet1.setLineWidth(3);
        dataSet1.setCircleSize(5);
        dataSet1.setValueTextSize(12);


        dataSetList.add(dataSet1);

        return dataSetList;
    }

    private ArrayList<String> xVals ()throws ParseException {
        //定義好時間字串的格式
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
        Calendar calendar = Calendar.getInstance();//(取得現在時間)
        int firstdayofweek = calendar.getFirstDayOfWeek();
        calendar.set(Calendar.DAY_OF_WEEK, firstdayofweek);
        Date dt = new Date(calendar.getTimeInMillis());
        calendar.setTime(dt);
        ArrayList<String> xVals = new ArrayList<>();
        for (int d2 = 1; d2 < 8; d2++) {
            Date tdt1 = calendar.getTime();//取得加減過後的Date
            //依照設定格式取得字串
            String time1 = sdf.format(tdt1);
            calendar.add(Calendar.DATE, 1);
            xVals.add(time1);
        }
        return xVals;
    }
    //--------------------下方為心跳-折線圖----------------------

    private ArrayList<LineDataSet> dataSetList2 ()throws com.parse.ParseException {
//        SharedPreferences getaccount;
//        getaccount = getActivity().getSharedPreferences("wecare_member", Context.MODE_PRIVATE);
//        String account = getaccount.getString("account", null);
//        String name = account;
//        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
//        Calendar calendar = Calendar.getInstance();//(取得現在時間)
//        int firstdayofweek = calendar.getFirstDayOfWeek();
//        calendar.set(Calendar.DAY_OF_WEEK, firstdayofweek);
//        Date dt = new Date(calendar.getTimeInMillis());
//        calendar.setTime(dt);
//        //-----------------------------------------------------------------------------------------------------------------------
        ArrayList<Entry> yVals1 = new ArrayList<>();

        //------------------------------------------------------------------------------------------------------------------------

//        ParseQuery<ParseObject> query3 = ParseQuery.getQuery("Week_searchdata");
//        int l = 0;
//        loop1:
//        for (int d2 = 1; d2 < 8; d2++) {
//            Date tdt1 = calendar.getTime();
//            String time1 = sdf.format(tdt1);
//            query3.whereEqualTo("date", time1);
//            query3.whereEqualTo("account",name);
//            query3.whereEqualTo("func_name", "心跳");
//            calendar.add(Calendar.DATE, 1);
//            List<ParseObject> pList1 = query3.find();
//            int a = query3.count();
//            if (a == 0) {
//                yVals1.add(new Entry(0, l));
//                l++;
//            } else {
//                loop2:
//                for (int i = 0; i < a; i++) {
//
//                    yVals1.add(new Entry(Float.parseFloat(pList1.get(i).getString("data")), l));
//                    l++;
//                }
//            }
//        }

        yVals1.add(new Entry(1, 0));
        yVals1.add(new Entry(0, 1));
        yVals1.add(new Entry(2, 2));
        yVals1.add(new Entry(1, 3));
        yVals1.add(new Entry(1, 4));
        yVals1.add(new Entry(2, 5));
        yVals1.add(new Entry(0, 6));


        String dataset_label1 = "異常次數";
        ArrayList<LineDataSet> dataSetList = new ArrayList<>();
        LineDataSet dataSet1 = new LineDataSet(yVals1, dataset_label1);
        dataSet1.setColor(Color.rgb(0, 0, 155));
        dataSet1.setLineWidth(3);
        dataSet1.setCircleSize(5);
        dataSet1.setValueTextSize(12);
        dataSetList.add(dataSet1);

        return dataSetList;
    }

    private ArrayList<String> xVals2 ()throws ParseException {
        //定義好時間字串的格式
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
        Calendar calendar = Calendar.getInstance();//(取得現在時間)
        int firstdayofweek = calendar.getFirstDayOfWeek();
        calendar.set(Calendar.DAY_OF_WEEK, firstdayofweek);
        Date dt = new Date(calendar.getTimeInMillis());
        calendar.setTime(dt);
        ArrayList<String> xVals = new ArrayList<>();
        for (int d2 = 1; d2 < 8; d2++) {
            Date tdt1 = calendar.getTime();//取得加減過後的Date
            //依照設定格式取得字串
            String time1 = sdf.format(tdt1);
            calendar.add(Calendar.DATE, 1);
            xVals.add(time1);
        }
        return xVals;
    }
}
