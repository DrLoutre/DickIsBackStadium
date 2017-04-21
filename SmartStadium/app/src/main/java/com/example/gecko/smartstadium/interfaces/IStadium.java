package com.example.gecko.smartstadium.interfaces;

import com.example.gecko.smartstadium.classes.Athletic;
import com.example.gecko.smartstadium.classes.Credentials;
import com.example.gecko.smartstadium.classes.Refreshment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IStadium {

    //ATHLETICS
    @GET("athletics/{id}")
    Call<Athletic> getAthletic(@Field("id") String id);

    @POST("athletics/login")
    Call<Athletic> postLogin(@Body Credentials credentials);

    //REFRESHMENTS
    @GET("refreshments")
    Call<ArrayList<Refreshment>> getRefreshments();

    @GET("refreshments/{id}")
    Call<Refreshment> getRefreshment(@Field("id") int id);
}
