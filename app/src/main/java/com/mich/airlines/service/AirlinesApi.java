package com.mich.airlines.service;


import com.mich.airlines.data.Airline;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

@SuppressWarnings("SpellCheckingInspection")
public interface AirlinesApi {

    @GET("/h/mobileapis/directory/airlines")
    Observable<List<Airline>> getAirlines();
}
