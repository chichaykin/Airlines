package com.mich.airlines.presenters;

import android.content.SharedPreferences;

import com.mich.airlines.data.Airline;
import com.mich.airlines.repositories.Favorite;
import com.mich.airlines.repositories.Favorite_Table;
import com.mich.airlines.service.AirlinesApi;
import com.mich.airlines.utils.L;
import com.mich.airlines.utils.Utils;
import com.mich.airlines.view.list.AirlinesView;
import com.mich.airlines.view.recycleview.EndlessPresenterImpl;
import com.mich.airlines.view.recycleview.EndlessView;
import com.raizlabs.android.dbflow.sql.language.From;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class AirlinesPresenterImpl extends EndlessPresenterImpl
        implements AirlinesPresenter {

    private final String mBaseUrl;
    private final AirlinesApi mAirlinesApi;
    private boolean mIsFavorite;
    private List<Airline> mCacheList;

    public AirlinesPresenterImpl(AirlinesApi airlinesApi,
                                 SharedPreferences preferences, Bus bus) {
        mAirlinesApi = airlinesApi;
        mBaseUrl = Utils.readBaseUrl(preferences);
        bus.register(this);
    }

    @Override
    public void setIsFavorite(boolean isFavorite) {
        mIsFavorite = isFavorite;
    }

    /***
     * Init presenter.
     * @param view It expects descendant of AirlinesView.
     */
    @Override
    public void init(EndlessView view) {
        super.init(view);
    }

    @Override
    public void destroy() {
        super.destroy();
        setFirstPage();
    }

    @Subscribe
    public void onDataChanged(Airline airline) {
        L.d("Data has changed %s", airline.toString() );

        //TODO: not to reload all data but update mCacheList.
        setFirstPage();
        mCacheList = null;
    }

    @Override
    protected void updateAdapter() {
        if (getPage() != 1) {
            return; // remove when there is paging support in kayak service.
        }

        if (mCacheList == null || mCacheList.isEmpty()) {
            getAirlineView().startProgress(true);
            getSubscription()
                    .add((mIsFavorite ? getFavorites() : mAirlinesApi.getAirlines())
                            .timeout(5, TimeUnit.SECONDS)
                            .retry(2)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .flatMap(new Func1<List<Airline>, Observable<List<Airline>>>() {
                                @Override
                                public Observable<List<Airline>> call(List<Airline> airlines) {
                                    if (!mIsFavorite) {
                                        From<Favorite> select = new Select().from(Favorite.class);
                                        for (Airline airline : airlines) {
                                            String url = Utils.getFullUrl(mBaseUrl, airline.getLogoURL());
                                            airline.setLogoURL(url);

                                            if (select.where(Favorite_Table.code.is(airline.getCode()))
                                                    .queryList().size() > 0) {
                                                airline.setFavorite(true);
                                            }
                                        }
                                    }
                                    return Observable.just(airlines);
                                }
                            })
                            .subscribe(new Subscriber<List<Airline>>() {

                                @Override
                                public void onCompleted() {
                                    AirlinesView view = ((AirlinesView) getView());
                                    if(view != null) {
                                        view.startProgress(false);
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    L.e("Service error: %s", e.toString());
                                }

                                @Override
                                public void onNext(List<Airline> airlines) {
                                    mCacheList = airlines; // store to reuse it when activity recreated
                                    AirlinesView view = ((AirlinesView) getView());
                                    if(view != null) {
                                        view.add(airlines);
                                    }
                                }
                            }));
        } else {
            ((AirlinesView) getView()).add(mCacheList);
        }
    }

    private Observable<List<Airline>> getFavorites() {
        return Observable.defer(new Func0<Observable<List<Airline>>>() {
            @Override
            public Observable<List<Airline>> call() {
                List<Favorite> list = new Select().from(Favorite.class).queryList();
                L.d("Favorites: %s", list.toString());
                return Observable.just(convert(list));
            }
        });
    }

    private List<Airline> convert(List<Favorite> list) {
        List<Airline> airlines = new ArrayList<>(list.size());
        for (Favorite f:list) {
            airlines.add(new Airline(f.getName(), f.getCode(), f.getLogoURL(), f.getPhone(), f.getSite(), true));
        }
        return airlines;
    }


    public void onAirlineClick(Airline airline) {
        getAirlineView().startDetailView(airline);
    }

    @Override
    public void updateData() {
        setFirstPage();
        mCacheList = null;
        getAirlineView().clearList();
        updateAdapter();
    }

    private AirlinesView getAirlineView() {
        return (AirlinesView) getView();
    }
}
