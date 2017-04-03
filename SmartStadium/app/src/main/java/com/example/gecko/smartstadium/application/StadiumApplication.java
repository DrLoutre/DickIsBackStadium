package com.example.gecko.smartstadium.application;

import android.app.Application;

import com.example.gecko.smartstadium.bus.BusProvider;
import com.example.gecko.smartstadium.manager.StadiumManager;
import com.squareup.otto.Bus;

public class StadiumApplication extends Application {

    private StadiumManager mStadiumManager;
    private Bus mBus = BusProvider.getInstance();

    @Override
    public void onCreate() {
        super.onCreate();
        mStadiumManager = new StadiumManager(this, mBus);
        mBus.register(mStadiumManager);
        mBus.register(this);
    }
}