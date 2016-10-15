package com.mich.airlines.view.recycleview;

/**
 * Introduce presenters life-cycle methods
 */
public interface EndlessPresenter {

    void init(EndlessView view);
    void destroy();
}
