package com.mich.airlines;

import android.app.Application;
import android.content.Context;

import com.mich.airlines.di.components.AppComponent;
import com.mich.airlines.di.components.DaggerAppComponent;
import com.mich.airlines.di.modules.AppModule;
import com.mich.airlines.di.modules.ExternalApiModule;
import com.mich.airlines.utils.Utils;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;


public class App extends Application {
    private static final String BASE_URL = "https://www.kayak.com";
    private static final long CACHE_SIZE = 1024 * 1024 * 10;

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(new FlowConfig.Builder(this).build());

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .externalApiModule(new ExternalApiModule(BASE_URL, CACHE_SIZE, getApplicationContext()))
                .build();

        Utils.storeBaseUrl(appComponent.sharedPreferences(), BASE_URL);
    }

    public static App get(Context context) {
        return (App)context.getApplicationContext();
    }

    public AppComponent getComponent() {
        return appComponent;
    }
}