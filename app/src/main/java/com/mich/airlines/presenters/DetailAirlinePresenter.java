package com.mich.airlines.presenters;


import com.mich.airlines.data.Airline;

public interface DetailAirlinePresenter {

    void init();

    void onFavoriteClick(Airline airline);

    void onPhoneClick(Airline airline);

    void onWebsiteClick(Airline airline);

    void onPermissionGranted(Airline airline, String[] permissions, int[] grantResults);
}
