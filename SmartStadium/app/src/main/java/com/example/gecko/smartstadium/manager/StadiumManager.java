package com.example.gecko.smartstadium.manager;

import android.content.Context;

import com.example.gecko.smartstadium.api.StadiumClient;
import com.example.gecko.smartstadium.classes.Athletic;
import com.example.gecko.smartstadium.classes.Lap;
import com.example.gecko.smartstadium.classes.Match;
import com.example.gecko.smartstadium.classes.Refreshment;
import com.example.gecko.smartstadium.classes.custom.MatchNotEnded;
import com.example.gecko.smartstadium.classes.custom.SeatsByTribune;
import com.example.gecko.smartstadium.events.AthleticEvent;
import com.example.gecko.smartstadium.events.GetLastMatchsNotEndedEvent;
import com.example.gecko.smartstadium.events.GetLastRaceAthleticEvent;
import com.example.gecko.smartstadium.events.GetRefreshmentsEvent;
import com.example.gecko.smartstadium.events.GetSeatsTribunesEvent;
import com.example.gecko.smartstadium.events.IdAthleticEvent;
import com.example.gecko.smartstadium.events.LastRaceAthleticEvent;
import com.example.gecko.smartstadium.events.LoginEvent;
import com.example.gecko.smartstadium.events.MatchsEvent;
import com.example.gecko.smartstadium.events.PostLoginEvent;
import com.example.gecko.smartstadium.events.RefreshmentsEvent;
import com.example.gecko.smartstadium.events.SeatsTribunesEvent;
import com.example.gecko.smartstadium.utils.ConnectionUtils;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StadiumManager {

    private Context mContext;
    private Bus mBus;
    private StadiumClient mStadiumClient;

    public StadiumManager(Context context, Bus bus) {
        this.mContext = context;
        this.mBus = bus;
        mStadiumClient = StadiumClient.getClient();
    }

    @Subscribe
    public void onLoginEvent(PostLoginEvent postLoginEvent) {
        if (!ConnectionUtils.isOnline(mContext)) {
            mBus.post(new LoginEvent(null));
            return;
        }

        Call<Athletic> call = mStadiumClient.postLogin(postLoginEvent.getCredentials());
        call.enqueue(new Callback<Athletic>() {
            @Override
            public void onResponse(Call<Athletic> call, Response<Athletic> response) {
                if (response.code() == HttpURLConnection.HTTP_CREATED) {
                    mBus.post(new LoginEvent(response.body()));
                } else {
                    mBus.post(new LoginEvent(null));
                }
            }

            @Override
            public void onFailure(Call<Athletic> call, Throwable t) {
                mBus.post(new LoginEvent(null));
            }
        });
    }

    @Subscribe
    public void onAthleticEvent(IdAthleticEvent idAthleticEvent) {
        if (!ConnectionUtils.isOnline(mContext)) {
            mBus.post(new AthleticEvent(null));
            return;
        }

        Call<Athletic> call = mStadiumClient.getAthletic(idAthleticEvent.getId());
        call.enqueue(new Callback<Athletic>() {
            @Override
            public void onResponse(Call<Athletic> call, Response<Athletic> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    mBus.post(new AthleticEvent(response.body()));
                } else {
                    mBus.post(new AthleticEvent(null));
                }
            }

            @Override
            public void onFailure(Call<Athletic> call, Throwable t) {
                mBus.post(new AthleticEvent(null));
            }
        });
    }

    @Subscribe
    public void onRefreshmentsEvent(GetRefreshmentsEvent getRefreshmentsEvent) {
        if (!ConnectionUtils.isOnline(mContext)) {
            mBus.post(new RefreshmentsEvent(null));
            return;
        }

        Call<ArrayList<Refreshment>> call = mStadiumClient.getRefreshments();
        call.enqueue(new Callback<ArrayList<Refreshment>>() {
            @Override
            public void onResponse(Call<ArrayList<Refreshment>> call, Response<ArrayList<Refreshment>> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    mBus.post(new RefreshmentsEvent(response.body()));
                } else {
                    mBus.post(new RefreshmentsEvent(null));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Refreshment>> call, Throwable t) {
                mBus.post(new RefreshmentsEvent(null));
            }
        });
    }

    @Subscribe
    public void onTribunesEvent(GetSeatsTribunesEvent getSeatsTribunesEvent) {
        if (!ConnectionUtils.isOnline(mContext)) {
            mBus.post(new SeatsTribunesEvent(null));
            return;
        }

        Call<ArrayList<SeatsByTribune>> call = mStadiumClient.getSeatsTribunesAvailable();
        call.enqueue(new Callback<ArrayList<SeatsByTribune>>() {
            @Override
            public void onResponse(Call<ArrayList<SeatsByTribune>> call, Response<ArrayList<SeatsByTribune>> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    mBus.post(new SeatsTribunesEvent(response.body()));
                } else {
                    mBus.post(new SeatsTribunesEvent(null));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SeatsByTribune>> call, Throwable t) {
                mBus.post(new SeatsTribunesEvent(null));
            }
        });
    }

    @Subscribe
    public void onGetLastRaceAthleticEvent(GetLastRaceAthleticEvent getLastRaceAthleticEvent) {
        if (!ConnectionUtils.isOnline(mContext)) {
            mBus.post(new LastRaceAthleticEvent(null));
            return;
        }

        Call<ArrayList<Lap>> call = mStadiumClient.getLastRaceAthletic(getLastRaceAthleticEvent.getId());
        call.enqueue(new Callback<ArrayList<Lap>>() {
            @Override
            public void onResponse(Call<ArrayList<Lap>> call, Response<ArrayList<Lap>> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    mBus.post(new LastRaceAthleticEvent(response.body()));
                } else {
                    mBus.post(new LastRaceAthleticEvent(null));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Lap>> call, Throwable t) {
                mBus.post(new LastRaceAthleticEvent(null));
            }
        });
    }

    @Subscribe
    public void onGetMatchNotEndedEvent(GetLastMatchsNotEndedEvent getLastMatchsNotEndedEvent) {
        if (!ConnectionUtils.isOnline(mContext)) {
            mBus.post(new MatchsEvent(null));
            return;
        }

        Call<ArrayList<MatchNotEnded>> call = mStadiumClient.getMatchsNotEndedAthletic(getLastMatchsNotEndedEvent.getId());
        call.enqueue(new Callback<ArrayList<MatchNotEnded>>() {
            @Override
            public void onResponse(Call<ArrayList<MatchNotEnded>> call, Response<ArrayList<MatchNotEnded>> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    mBus.post(new MatchsEvent(response.body()));
                } else {
                    mBus.post(new MatchsEvent(null));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<MatchNotEnded>> call, Throwable t) {
                mBus.post(new MatchsEvent(null));
            }
        });
    }
}
