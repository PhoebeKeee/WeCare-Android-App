package com.example.victor.mertial_test.Extras;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Victor on 2015/10/15.
 */
public class L {
    public static void m(Context context,String m){
        Toast.makeText(context,m,Toast.LENGTH_SHORT).show();
    }
    public static void l(String m){
        Log.i("TAG",m);
    }
}
