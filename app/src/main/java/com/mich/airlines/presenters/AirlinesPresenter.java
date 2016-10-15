package com.mich.airlines.presenters;

import com.mich.airlines.data.Airline;
import com.mich.airlines.view.recycleview.EndlessPresenter;

public interface AirlinesPresenter extends EndlessPresenter {

    void setIsFavorite(boolean isFavorite);

    void onAirlineClick(Airline airline);

    void updateData();
}
