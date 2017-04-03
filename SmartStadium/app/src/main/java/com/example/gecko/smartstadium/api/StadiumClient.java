package com.example.gecko.smartstadium.api;

import com.example.gecko.smartstadium.classes.Athletic;
import com.example.gecko.smartstadium.classes.Credentials;
import com.example.gecko.smartstadium.interfaces.IStadium;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

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
                .addConverterFactory(JacksonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(new OkHttpClient())
                .build();
    }

    public Call<Athletic> postLogin(Credentials credentials) {
        IStadium iStadium = mRetrofit.create(IStadium.class);
        return iStadium.postLogin(credentials);
    }
}
