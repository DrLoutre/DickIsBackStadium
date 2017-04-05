package com.example.gecko.smartstadium.api;

import com.example.gecko.smartstadium.classes.Athletic;
import com.example.gecko.smartstadium.classes.Credentials;
import com.example.gecko.smartstadium.interfaces.IStadium;

import retrofit2.Call;
import retrofit2.Retrofit;

public class StadiumClient {

    public final static String BASE_URL = "";

    private static StadiumClient mStadiumClient;
    private static Retrofit mRetrofit;

    private StadiumClient() {
        /* todo commenté pour compiler
        mRetrofit = new Retrofit.Builder()
                //.addConverterFactory(JacksonConverterFactory.create())
                //.baseUrl(BASE_URL)
                .client(new OkHttpClient())
                .build();
                */
    }

    public static StadiumClient getClient() {
        if (mStadiumClient == null)
            mStadiumClient = new StadiumClient();
        return mStadiumClient;
    }

    public Call<Athletic> postLogin(Credentials credentials) {
        IStadium iStadium = mRetrofit.create(IStadium.class);
        return iStadium.postLogin(credentials);
    }

    public Call<Athletic> getAthletic(String id) {
        IStadium iStadium = mRetrofit.create(IStadium.class);
        return iStadium.getAthletic(id);
    }
}
