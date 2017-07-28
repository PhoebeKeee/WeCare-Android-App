package com.example.victor.mertial_test.Extras.HeartBeat;

import java.util.ArrayList;

/**
 * Created by Victor on 2015/10/15.
 */
public class HeartDisplayer implements HeartListener {

    private String mHeart="" ;

    @Override
    public void onHeart(String mHearts) {
        mHeart = mHearts;
        notifyListener();
    }

    @Override
    public void passValue() {

    }


    public interface Listener{
        public void HeartsChanged(String value);
        public void passValue();
    }

    private ArrayList<Listener> mListeners = new ArrayList<Listener>();

    public void addListener(Listener L){
        mListeners.add(L);
    }

    public void notifyListener(){
        for (Listener listener : mListeners) {
            listener.HeartsChanged(mHeart);
        }
    }

}
