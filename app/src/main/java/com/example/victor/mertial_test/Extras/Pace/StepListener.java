package com.example.victor.mertial_test.Extras.Pace;

/**
 * Created by Victor on 2015/10/6.
 */
//Interface implemented by classes that can handle notifications about steps.
//These classes can be passed to StepDetector.
public interface StepListener {

    public void onStep();
    public void passValue();

}
