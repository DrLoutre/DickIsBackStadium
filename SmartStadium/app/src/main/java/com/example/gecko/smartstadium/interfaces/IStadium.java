package com.example.gecko.smartstadium.interfaces;

import com.example.gecko.smartstadium.classes.Athletic;
import com.example.gecko.smartstadium.classes.Credentials;

import retrofit2.Call;
import retrofit2.http.POST;

public interface IStadium {

    @POST("/login")
    Call<Athletic> postLogin(Credentials credentials);
}
