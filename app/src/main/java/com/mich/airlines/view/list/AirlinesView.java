package com.mich.airlines.view.list;

import com.mich.airlines.data.Airline;
import com.mich.airlines.view.recycleview.EndlessView;

import java.util.List;

public interface AirlinesView extends EndlessView {

    void add(List<Airline> movies);

    void startDetailView(Airline airline);

    void startProgress(boolean b);

    void clearList();
}
