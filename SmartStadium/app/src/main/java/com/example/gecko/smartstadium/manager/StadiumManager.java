package com.example.gecko.smartstadium.manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import com.example.gecko.smartstadium.api.StadiumClient;
import com.example.gecko.smartstadium.classes.Athletic;
import com.example.gecko.smartstadium.events.LoginEvent;
import com.example.gecko.smartstadium.events.PostLoginEvent;
import com.squareup.otto.Bus;

import java.net.HttpURLConnection;

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

    public void onLoginEvent(PostLoginEvent postLoginEvent) {
        if (!isOnline()) {
            Toast.makeText(mContext, "Pas de connexion internet", Toast.LENGTH_SHORT).show();
        }

        Call<Athletic> call = mStadiumClient.postLogin(postLoginEvent.getCredentials());
        call.enqueue(new Callback<Athletic>() {
            @Override
            public void onResponse(Call<Athletic> call, Response<Athletic> response) {
                if (response.code() == HttpURLConnection.HTTP_CREATED) {
                    mBus.post(new LoginEvent(response.body()));
                }
            }

            @Override
            public void onFailure(Call<Athletic> call, Throwable t) {
                Toast.makeText(mContext, "Un problème inattendu est survenu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
