package com.mich.airlines.view.recycleview;


import android.util.Log;

import rx.subscriptions.CompositeSubscription;

public abstract class EndlessPresenterImpl implements EndlessPresenter {
    private final String TAG = getClass().getSimpleName();
    private EndlessView view;
    private CompositeSubscription compositeSubscription;
    private int page = 1;

    protected EndlessPresenterImpl() {
    }

    protected EndlessPresenterImpl(EndlessView view) {
        this.view = view;
    }

    protected EndlessView getView() {
        return view;
    }

    /**
     * @return
     */
    protected int getPage() {
        return page;
    }

    protected void setFirstPage() {
        page = 1;
    }

    protected CompositeSubscription getSubscription() {
        return compositeSubscription;
    }

    protected abstract void updateAdapter();

    /* EndlessPresenter interface implementation */

    public void init(EndlessView view) {
        this.view = view;
        compositeSubscription = new CompositeSubscription();
        view.init(new EndlessOnScrollListenerImpl.DataLoader() {

            @Override
            public void onLoadMore() {
                page++;
                Log.d(TAG, "Start loading page =" + page);
                updateAdapter();
            }
        });
        updateAdapter();
    }

    public void destroy() {
        view = null;
        compositeSubscription.unsubscribe();
        compositeSubscription = null;
    }
}
