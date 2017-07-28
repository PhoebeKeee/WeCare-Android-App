package com.example.victor.mertial_test.Extras;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Victor on 2015/9/21.
 */
public class Message {
    public static void message(Context context,String content){
        Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
    }
}
