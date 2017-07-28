package com.example.victor.mertial_test.Extras.Pace;

import java.util.ArrayList;

/**
 * Created by Victor on 2015/10/6.
 */
public class StepDisplayer implements StepListener{

    private int mCount = 0;

    @Override
    public void onStep() {
        mCount++;
        notifyListener();
    }

    @Override
    public void passValue() {

    }

    public void setSteps(int steps){
        mCount = steps;
        notifyListener();
    }

    public interface Listener{
        public void stepsChanged(int value);
        public void passValue();
    }

    private ArrayList<Listener> mListeners = new ArrayList<Listener>();

    public void addListener(Listener L){
        mListeners.add(L);
    }

    public void notifyListener(){
        for (Listener listener : mListeners) {
            listener.stepsChanged((int)mCount);
        }
    }


}
