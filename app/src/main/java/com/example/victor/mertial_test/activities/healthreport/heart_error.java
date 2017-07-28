package com.example.victor.mertial_test.activities.healthreport;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.victor.mertial_test.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.parse.ParseException;

import java.util.ArrayList;

public class heart_error extends ActionBarActivity {
    private Toolbar toolbar;
    private TextView l_1;
    private TextView l_2;
    private TextView l_3;
    private int count;
    private int personal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_error);
        //注意:X軸或Y軸有多少筆資料，相對應的就要有多少筆，X軸有8筆，Y軸就要有8筆，相反亦然。
        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent time = getIntent();
        count = time.getIntExtra("times",-1);
        personal = time.getIntExtra("personal",-1);

        LineChart Linechart = (LineChart)findViewById(R.id.linechart);
        LineData data1 = null;
        data1 = new LineData(xVals(), dataSetList()); //這一行在處理X軸和Y軸的資料，前面是X軸，後面是Y軸

        LineChart Linechart1 = (LineChart)findViewById(R.id.linechart1);
        LineData data2 = null;
        data2 = new LineData(xVals1(), dataSetList1()); //這一行在處理X軸和Y軸的資料，前面是X軸，後面是Y軸

        LineChart Linechart2 = (LineChart)findViewById(R.id.linechart2);
        LineData data3 = null;
        data3 = new LineData(xVals2(), dataSetList2()); //這一行在處理X軸和Y軸的資料，前面是X軸，後面是Y軸

        l_1 = (TextView)findViewById(R.id.l_1);
        l_2 = (TextView)findViewById(R.id.l_2);
        l_3 = (TextView)findViewById(R.id.l_3);
//下面這邊在設置圖表內的一些參數
        Linechart.setData(data1);
        Linechart.animateXY(2, 2);
        Linechart.setDescription("");
        Linechart.invalidate();

        Linechart1.setData(data2);
        Linechart1.animateXY(2, 2);
        Linechart1.setDescription("");
        Linechart1.invalidate();

        Linechart2.setData(data3);
        Linechart2.animateXY(2, 2);
        Linechart2.setDescription("");
        Linechart2.invalidate();

//下面這邊是在設置X軸的顯示參數呈現在圖表下方
        XAxis xVals = Linechart.getXAxis();
        xVals.setPosition(XAxis.XAxisPosition.BOTTOM);
        xVals.setDrawLabels(true);

        XAxis xVals1 = Linechart1.getXAxis();
        xVals1.setPosition(XAxis.XAxisPosition.BOTTOM);
        xVals1.setDrawLabels(true);

        XAxis xVals2 = Linechart2.getXAxis();
        xVals2.setPosition(XAxis.XAxisPosition.BOTTOM);
        xVals2.setDrawLabels(true);

//下面這個方法是在處理Y軸(也就是內部資料的部分)


//下面這邊是X軸，顯示幾時。
        if(count == 1){
          Linechart1.setVisibility(View.VISIBLE);
            l_2.setVisibility(View.VISIBLE);
        }else if(count ==2){
            Linechart.setVisibility(View.VISIBLE);
            Linechart2.setVisibility(View.VISIBLE);
            l_1.setVisibility(View.VISIBLE);
            l_3.setVisibility(View.VISIBLE);
        }else if(count == 3){
            Linechart1.setVisibility(View.VISIBLE);
            Linechart.setVisibility(View.VISIBLE);
            Linechart2.setVisibility(View.VISIBLE);
            l_1.setVisibility(View.VISIBLE);
            l_2.setVisibility(View.VISIBLE);
            l_3.setVisibility(View.VISIBLE);
        }

    }


    private ArrayList<LineDataSet> dataSetList(){
        ArrayList<Entry> yVals1 = new ArrayList<>();

        ArrayList<LineDataSet> dataSetList = new ArrayList<>();
        if(count ==1) {

                yVals1.add(new Entry(72, 0)); //前面放的是資料的數據(48)，後面放的是位置(0)
                yVals1.add(new Entry(79, 1));
                yVals1.add(new Entry(100, 2));
                yVals1.add(new Entry(99, 3));
                yVals1.add(new Entry(105, 4));
                yVals1.add(new Entry(110, 5));
                yVals1.add(new Entry(104, 6));
                yVals1.add(new Entry(94, 7));
                yVals1.add(new Entry(80, 8));
                yVals1.add(new Entry(72, 9));

        }else if(count == 2){

                yVals1.add(new Entry(88, 0)); //前面放的是資料的數據(48)，後面放的是位置(0)
                yVals1.add(new Entry(70, 1));
                yVals1.add(new Entry(100, 2));
                yVals1.add(new Entry(101, 3));
                yVals1.add(new Entry(105, 4));
                yVals1.add(new Entry(110, 5));
                yVals1.add(new Entry(104, 6));
                yVals1.add(new Entry(94, 7));
                yVals1.add(new Entry(72, 8));
                yVals1.add(new Entry(72, 9));

        }else if(count == 3){
            yVals1.add(new Entry(70, 0)); //前面放的是資料的數據(48)，後面放的是位置(0)
            yVals1.add(new Entry(72, 1));
            yVals1.add(new Entry(75, 2));
            yVals1.add(new Entry(102, 3));
            yVals1.add(new Entry(100, 4));
            yVals1.add(new Entry(99, 5));
            yVals1.add(new Entry(88, 6));
            yVals1.add(new Entry(94, 7));
            yVals1.add(new Entry(72, 8));
            yVals1.add(new Entry(70, 9));
        }

        String dataset_label1 = "異常次數"; //這個數據的label，代表這數據的意義
        LineDataSet dataSet1 = new LineDataSet(yVals1, dataset_label1); //將y軸數據內容和數據意義放入

//下面這邊是在設置這個dataset的參數(也就是內部資料呈現方式的各項數值)
        dataSet1.setColor(Color.rgb(0, 155, 0));
        dataSet1.setLineWidth(3);
        dataSet1.setCircleSize(5);
        dataSet1.setValueTextSize(12);


        dataSetList.add(dataSet1);

        return dataSetList;
    }


    private ArrayList<LineDataSet> dataSetList1(){
        ArrayList<Entry> yVals1 = new ArrayList<>();

        ArrayList<LineDataSet> dataSetList1 = new ArrayList<>();
        if(count ==1) {
            yVals1.add(new Entry(68, 0)); //前面放的是資料的數據(48)，後面放的是位置(0)
            yVals1.add(new Entry(68, 1));
            yVals1.add(new Entry(80, 2));
            yVals1.add(new Entry(99, 3));
            yVals1.add(new Entry(110, 4));
            yVals1.add(new Entry(113, 5));
            yVals1.add(new Entry(101, 6));
            yVals1.add(new Entry(90, 7));
            yVals1.add(new Entry(80, 8));
            yVals1.add(new Entry(68, 9));
        }else if(count == 2){
            yVals1.add(new Entry(60, 0)); //前面放的是資料的數據(48)，後面放的是位置(0)
            yVals1.add(new Entry(65, 1));
            yVals1.add(new Entry(70, 2));
            yVals1.add(new Entry(88, 3));
            yVals1.add(new Entry(72, 4));
            yVals1.add(new Entry(99, 5));
            yVals1.add(new Entry(72, 6));
            yVals1.add(new Entry(94, 7));
            yVals1.add(new Entry(72, 8));
            yVals1.add(new Entry(72, 9));
        }else if(count == 3){
            yVals1.add(new Entry(85, 0)); //前面放的是資料的數據(48)，後面放的是位置(0)
            yVals1.add(new Entry(72, 1));
            yVals1.add(new Entry(79, 2));
            yVals1.add(new Entry(99, 3));
            yVals1.add(new Entry(100, 4));
            yVals1.add(new Entry(99, 5));
            yVals1.add(new Entry(76, 6));
            yVals1.add(new Entry(73, 7));
            yVals1.add(new Entry(72, 8));
            yVals1.add(new Entry(70, 9));
        }


        String dataset_label1 = "異常次數"; //這個數據的label，代表這數據的意義
        LineDataSet dataSet1 = new LineDataSet(yVals1, dataset_label1); //將y軸數據內容和數據意義放入

//下面這邊是在設置這個dataset的參數(也就是內部資料呈現方式的各項數值)
        dataSet1.setColor(Color.rgb(0, 155, 0));
        dataSet1.setLineWidth(3);
        dataSet1.setCircleSize(5);
        dataSet1.setValueTextSize(12);


        dataSetList1.add(dataSet1);

        return dataSetList1;
    }

    private ArrayList<LineDataSet> dataSetList2(){
        ArrayList<Entry> yVals1 = new ArrayList<>();

        ArrayList<LineDataSet> dataSetList2 = new ArrayList<>();
        if(count ==1) {
            yVals1.add(new Entry(68, 0)); //前面放的是資料的數據(48)，後面放的是位置(0)
            yVals1.add(new Entry(68, 1));
            yVals1.add(new Entry(80, 2));
            yVals1.add(new Entry(68, 3));
            yVals1.add(new Entry(111, 4));
            yVals1.add(new Entry(109, 5));
            yVals1.add(new Entry(101, 6));
            yVals1.add(new Entry(77, 7));
            yVals1.add(new Entry(80, 8));
            yVals1.add(new Entry(68, 9));
        }else if(count == 2){
            yVals1.add(new Entry(69, 0)); //前面放的是資料的數據(48)，後面放的是位置(0)
            yVals1.add(new Entry(70, 1));
            yVals1.add(new Entry(76, 2));
            yVals1.add(new Entry(78, 3));
            yVals1.add(new Entry(103, 4));
            yVals1.add(new Entry(108, 5));
            yVals1.add(new Entry(98, 6));
            yVals1.add(new Entry(94, 7));
            yVals1.add(new Entry(72, 8));
            yVals1.add(new Entry(72, 9));
        }else if(count == 3){
            yVals1.add(new Entry(60, 0)); //前面放的是資料的數據(48)，後面放的是位置(0)
            yVals1.add(new Entry(65, 1));
            yVals1.add(new Entry(63, 2));
            yVals1.add(new Entry(63, 3));
            yVals1.add(new Entry(75, 4));
            yVals1.add(new Entry(77, 5));
            yVals1.add(new Entry(99, 6));
            yVals1.add(new Entry(64, 7));
            yVals1.add(new Entry(70, 8));
            yVals1.add(new Entry(69, 9));
        }


        String dataset_label1 = "異常次數"; //這個數據的label，代表這數據的意義
        LineDataSet dataSet1 = new LineDataSet(yVals1, dataset_label1); //將y軸數據內容和數據意義放入

//下面這邊是在設置這個dataset的參數(也就是內部資料呈現方式的各項數值)
        dataSet1.setColor(Color.rgb(0, 155, 0));
        dataSet1.setLineWidth(3);
        dataSet1.setCircleSize(5);
        dataSet1.setValueTextSize(12);


        dataSetList2.add(dataSet1);

        return dataSetList2;
    }


    private ArrayList<String> xVals(){
        ArrayList<String> xVals = new ArrayList<>();
        for (int d2 = 10; d2 < 21; d2++) {
            xVals.add(d2+"分");
        }
        return xVals;
    }

    private ArrayList<String> xVals1(){
        ArrayList<String> xVals = new ArrayList<>();
        for (int d2 = 0; d2 < 10; d2++) {
            xVals.add(d2+"分");
        }
        return xVals;
    }

    private ArrayList<String> xVals2(){
        ArrayList<String> xVals = new ArrayList<>();
        for (int d2 = 35; d2 < 46; d2++) {
            xVals.add(d2+"分");
        }
        return xVals;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_heart_error, menu);
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

        return super.onOptionsItemSelected(item);
    }
}
