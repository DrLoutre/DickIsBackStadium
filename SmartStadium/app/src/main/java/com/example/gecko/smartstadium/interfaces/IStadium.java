package com.example.gecko.smartstadium.interfaces;

import com.example.gecko.smartstadium.classes.Athletic;
import com.example.gecko.smartstadium.classes.Credentials;
import com.example.gecko.smartstadium.classes.Lap;
import com.example.gecko.smartstadium.classes.Match;
import com.example.gecko.smartstadium.classes.custom.MatchNotEnded;
import com.example.gecko.smartstadium.classes.custom.OccupancyRate;
import com.example.gecko.smartstadium.classes.Refreshment;
import com.example.gecko.smartstadium.classes.Seat;
import com.example.gecko.smartstadium.classes.Spectator;
import com.example.gecko.smartstadium.classes.Tribune;
import com.example.gecko.smartstadium.classes.custom.SeatsByTribune;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * This is all the method available in the RestFul API that the application can call
 */
public interface IStadium {

    //ATHLETICS
    @GET("athletics/{id}")
    Call<Athletic> getAthletic(@Path("id") String id);

    @GET("athletics/{id}/races/last/laps/last")
    Call<Lap> getAthleticLastLapFromLastRace(@Path("id") String id);

    @GET("athletics/{id}/matchs/notended")
    Call<ArrayList<MatchNotEnded>> getAthleticMatchsNotEnded(@Path("id") String id);

    @POST("athletics/login")
    Call<Athletic> postLogin(@Body Credentials credentials);

    //LAPS
    @GET("athletics/{id}/races/last/laps")
    Call<ArrayList<Lap>> getLastRace(@Path("id") String id);

    //REFRESHMENTS
    @GET("refreshments")
    Call<ArrayList<Refreshment>> getRefreshments();

    @GET("refreshments/{id}")
    Call<Refreshment> getRefreshment(@Path("id") int id);

    //TRIBUNES
    @GET("tribunes/{id}")
    Call<Tribune> getTribune(@Path("id") int id);

    @GET("tribunes")
    Call<ArrayList<Tribune>> getTribunes();

    //SEATS
    @GET("tribunes/seats/available")
    Call<ArrayList<SeatsByTribune>> getSeatsTribunesAvailable();

    @GET("tribunes/{id}/seats")
    Call<ArrayList<Seat>> getTribuneSeats(@Path("id") int id);

    @GET("tribunes/{id}/rate")
    Call<OccupancyRate> getOccupancyRate(@Path("id") int id);

    @GET("tribunes/{id}/available")
    Call<ArrayList<Seat>> getTribuneSeatsAvailable(@Path("id") int id);

    //SPECTATORS
    @GET("spectators/id")
    Call<Spectator> getSpectator(@Path("id") int id);
}
