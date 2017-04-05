package com.example.gecko.smartstadium.interfaces;

import com.example.gecko.smartstadium.classes.Athletic;
import com.example.gecko.smartstadium.classes.Credentials;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IStadium {

    @POST("/login")
    Call<Athletic> postLogin(Credentials credentials);

    @GET("/athletics/{id}")
    Call<Athletic> getAthletic(@Field("id") String id);
}
