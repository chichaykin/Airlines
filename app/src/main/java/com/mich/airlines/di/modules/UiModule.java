package com.mich.airlines.di.modules;

import com.mich.airlines.di.scopes.PerActivity;
import com.mich.airlines.presenters.DetailAirlinePresenter;
import com.mich.airlines.presenters.DetailAirlinePresenterImpl;
import com.mich.airlines.view.detail.DetailsView;
import com.mich.airlines.view.list.AirlinesView;
import com.squareup.otto.Bus;

import dagger.Module;
import dagger.Provides;

@Module
public class UiModule {
    private final AirlinesView airlinesView;
    private final DetailsView detailView;

    public UiModule(AirlinesView view) {
        airlinesView = view;
        detailView = null;
    }

    public UiModule(DetailsView view) {
        detailView = view;
        airlinesView = null;
    }

    @SuppressWarnings("unused")
    @Provides
    @PerActivity
    DetailAirlinePresenter providesDetailPresenter(Bus bus) {
        return new DetailAirlinePresenterImpl(detailView, bus);
    }
}