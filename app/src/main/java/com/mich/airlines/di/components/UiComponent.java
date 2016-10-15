package com.mich.airlines.di.components;


import com.mich.airlines.di.modules.UiModule;
import com.mich.airlines.di.scopes.PerActivity;
import com.mich.airlines.view.detail.DetailAirlineFragment;

import dagger.Component;

@SuppressWarnings("ALL")
@PerActivity
@Component(dependencies = AppComponent.class, modules = UiModule.class)
public interface UiComponent {

    void inject(DetailAirlineFragment detailAirlineFragment);
}
