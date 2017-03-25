package com.example.gecko.smartstadium.api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class StadiumClient {

    public final static String BASE_URL = "";

    private static StadiumClient mStadiumClient;
    private static Retrofit mRetrofit;

    public static StadiumClient getClient() {
        if (mStadiumClient == null)
            mStadiumClient = new StadiumClient();
        return mStadiumClient;
    }

    private StadiumClient() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(new OkHttpClient())
                .build();
    }
}
