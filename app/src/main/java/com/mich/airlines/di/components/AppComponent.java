package com.mich.airlines.di.components;


import android.content.SharedPreferences;

import com.mich.airlines.di.modules.AppModule;
import com.mich.airlines.di.modules.ExternalApiModule;
import com.mich.airlines.presenters.AirlinesPresenter;
import com.mich.airlines.service.AirlinesApi;
import com.squareup.otto.Bus;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;

@SuppressWarnings("unused")
@Singleton
@Component(modules={AppModule.class, ExternalApiModule.class})
public interface AppComponent {

    AirlinesApi airlineApi();

    SharedPreferences sharedPreferences();

    Bus bus();

    /***
     * Favorites airlines presenter.
     * @return presenter with cached data
     */
    @Named("Top")
    AirlinesPresenter airlineTopPresenter();

    /***
     * All airlines presenter.
     * @return presenter with cached data
     */
    @Named("All")
    AirlinesPresenter airlineAllPresenter();
}