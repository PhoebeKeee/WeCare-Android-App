package com.example.victor.mertial_test.Extras.Pace;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import java.util.ArrayList;

import static com.example.victor.mertial_test.Extras.L.l;

/**
 * Created by Victor on 2015/10/6.
 */
public class StepDetector implements SensorEventListener {

    float currentY,previousY;
    int threshold = 2;

    private ArrayList<StepListener> mStepListeners = new ArrayList<StepListener>();

    public void addStepListener(StepListener sl) {
        mStepListeners.add(sl);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            currentY = y;

        if(Math.abs(currentY - previousY) > threshold){
            l("step");
            for (StepListener stepListener : mStepListeners) {
                stepListener.onStep();
            }
            l(String.valueOf(mStepListeners.size()));
        }
        previousY = y;
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
