package com.example.victor.mertial_test.activities.myCare;

import android.graphics.Bitmap;

/**
 * Created by Phoebe on 15/9/14.
 */
class Person {
    String name;
    String time;
    int photo;
    String familymember;
    String message;


    Person(String name,  String time, int photo,String familymember,String message) {
        this.name = name;
        this.time =time;
        this.photo=photo;
        this.familymember=familymember;
        this.message=message;
    }
}
