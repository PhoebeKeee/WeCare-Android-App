package com.example.victor.mertial_test.activities.Message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.victor.mertial_test.R;

/**
 * Created by ipt810105 on 2015/12/3.
 */
public class messageadapter extends BaseAdapter {
    private LayoutInflater inflater;
    String [] key;
    String[]time;
    public messageadapter(Context c,String [] key,String []time){
        inflater = LayoutInflater.from(c);
        this.key = key;
        this.time=time;

    }
    @Override
    public int getCount() {
        return key.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.message,viewGroup,false);
        TextView key2;
        TextView time2;
        key2 = (TextView) view.findViewById(R.id.key);
        time2 = (TextView) view.findViewById(R.id.time);
        key2.setText(key[i]);
        time2.setText(time[i]);

        return view;
    }
}

