package com.example.gecko.smartstadium.api;

import com.example.gecko.smartstadium.classes.Athletic;
import com.example.gecko.smartstadium.classes.Credentials;
import com.example.gecko.smartstadium.classes.Lap;
import com.example.gecko.smartstadium.classes.Refreshment;
import com.example.gecko.smartstadium.classes.custom.MatchNotEnded;
import com.example.gecko.smartstadium.classes.custom.SeatsByTribune;
import com.example.gecko.smartstadium.interfaces.IStadium;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StadiumClient {

    final static String BASE_URL = "http://10.0.2.2:8080/StadiumWebSite/api/v1/";

    private static StadiumClient mStadiumClient;
    private static Retrofit mRetrofit;

    /**
     * Initialize the client
     */
    private StadiumClient() {
        mRetrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(new OkHttpClient())
                .build();
    }

    /**
     * Return the instance of the client
     * @return
     */
    public static StadiumClient getClient() {
        if (mStadiumClient == null)
            mStadiumClient = new StadiumClient();
        return mStadiumClient;
    }

    /**
     * Ask the api for the call to login
     * @param credentials
     * @return
     */
    public Call<Athletic> postLogin(Credentials credentials) {
        IStadium iStadium = mRetrofit.create(IStadium.class);
        return iStadium.postLogin(credentials);
    }

    /**
     * Ask the api for the call to get an athletic
     * @param id
     * @return
     */
    public Call<Athletic> getAthletic(String id) {
        IStadium iStadium = mRetrofit.create(IStadium.class);
        return iStadium.getAthletic(id);
    }

    /**
     * Ask the api for the call to get all refreshments
     * @return
     */
    public Call<ArrayList<Refreshment>> getRefreshments() {
        IStadium iStadium = mRetrofit.create(IStadium.class);
        return iStadium.getRefreshments();
    }

    /**
     * Ask the api for the call to get seats available in all tribunes
     * @return
     */
    public Call<ArrayList<SeatsByTribune>> getSeatsTribunesAvailable() {
        IStadium iStadium = mRetrofit.create(IStadium.class);
        return iStadium.getSeatsTribunesAvailable();
    }

    /**
     * Ask the api for the call to get the last race of an athletic
     * @param id
     * @return
     */
    public Call<ArrayList<Lap>> getLastRaceAthletic(String id) {
        IStadium iStadium = mRetrofit.create(IStadium.class);
        return iStadium.getLastRace(id);
    }

    /**
     * Ask the api for the call to get matchs not ended for an athletic
     * @param id
     * @return
     */
    public Call<ArrayList<MatchNotEnded>> getMatchsNotEndedAthletic(String id) {
        IStadium iStadium = mRetrofit.create(IStadium.class);
        return iStadium.getAthleticMatchsNotEnded(id);
    }
}
