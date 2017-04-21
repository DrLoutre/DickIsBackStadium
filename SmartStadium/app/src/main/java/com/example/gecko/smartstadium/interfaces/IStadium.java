package com.example.gecko.smartstadium.interfaces;

import com.example.gecko.smartstadium.classes.Athletic;
import com.example.gecko.smartstadium.classes.Credentials;
import com.example.gecko.smartstadium.classes.Lap;
import com.example.gecko.smartstadium.classes.Match;
import com.example.gecko.smartstadium.classes.OccupancyRate;
import com.example.gecko.smartstadium.classes.Refreshment;
import com.example.gecko.smartstadium.classes.Seat;
import com.example.gecko.smartstadium.classes.Spectator;
import com.example.gecko.smartstadium.classes.Tribune;

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

    @GET("athletics/{id}/races/last/laps/last")
    Call<Lap> getAthleticLastLapFromLastRace(@Field("id") String id);

    @GET("athletics/{id}/matchs/ended")
    Call<ArrayList<Match>> getAthleticMatchsNotEnded(@Field("id") String id);

    @POST("athletics/login")
    Call<Athletic> postLogin(@Body Credentials credentials);

    //REFRESHMENTS
    @GET("refreshments")
    Call<ArrayList<Refreshment>> getRefreshments();

    @GET("refreshments/{id}")
    Call<Refreshment> getRefreshment(@Field("id") int id);

    //SEATS
    @GET("tribunes/{id}")
    Call<Tribune> getTribune(@Field("id") int id);

    @GET("tribunes/{id}/seats")
    Call<ArrayList<Seat>> getTribuneSeats(@Field("id") int id);

    @GET("tribunes/{id}/rate")
    Call<OccupancyRate> getOccupancyRate(@Field("id") int id);

    @GET("tribunes/{id}/available")
    Call<ArrayList<Seat>> getTribuneSeatsAvailable(@Field("id") int id);

    //SPECTATORS
    @GET("spectators/id")
    Call<Spectator> getSpectator(@Field("id") int id);
}
