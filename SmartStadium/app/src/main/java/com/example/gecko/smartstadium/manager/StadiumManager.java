package com.example.gecko.smartstadium.manager;

import android.content.Context;

import com.example.gecko.smartstadium.api.StadiumClient;
import com.squareup.otto.Bus;

public class StadiumManager {

    private Context mContext;
    private Bus mBus;
    private StadiumClient mStadiumClient;

    public StadiumManager(Context context, Bus bus) {
        this.mContext = context;
        this.mBus = bus;
        mStadiumClient = StadiumClient.getClient();
    }
}
