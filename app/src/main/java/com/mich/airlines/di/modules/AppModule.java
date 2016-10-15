package com.mich.airlines.di.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.mich.airlines.di.scopes.ApplicationContext;
import com.mich.airlines.presenters.AirlinesPresenter;
import com.mich.airlines.presenters.AirlinesPresenterImpl;
import com.mich.airlines.service.AirlinesApi;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private final Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @SuppressWarnings("unused")
    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

    @SuppressWarnings("unused")
    @Provides
    @ApplicationContext
    Context providesContext() {
        return mApplication;
    }

    @SuppressWarnings("unused")
    @Provides
    @Singleton
    public SharedPreferences providesPreferences() {
        return mApplication.getSharedPreferences("config", Context.MODE_PRIVATE);
    }

    @SuppressWarnings("unused")
    @Provides
    @Singleton
    public Bus providesBus() {
        return new Bus(ThreadEnforcer.ANY);
    }

    @SuppressWarnings("unused")
    @Provides
    @Singleton
    @Named("All")
    AirlinesPresenter providesAllPresenter(AirlinesApi airlineApi, SharedPreferences preferences, Bus bus) {
        return new AirlinesPresenterImpl(airlineApi, preferences, bus);
    }

    @SuppressWarnings("unused")
    @Provides
    @Singleton
    @Named("Top")
    AirlinesPresenter providesTopPresenter(AirlinesApi airlineApi, SharedPreferences preferences, Bus bus) {
        return new AirlinesPresenterImpl(airlineApi, preferences, bus);
    }
}
