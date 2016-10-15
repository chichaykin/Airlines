package com.mich.airlines.view.list;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mich.airlines.App;
import com.mich.airlines.R;
import com.mich.airlines.data.Airline;
import com.mich.airlines.presenters.AirlinesPresenter;
import com.mich.airlines.view.AirlineActivity;
import com.mich.airlines.view.recycleview.EndlessOnScrollListenerImpl;
import com.mich.airlines.view.recycleview.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.ribot.easyadapter.EasyRecyclerAdapter;

@SuppressWarnings({"unused", "WeakerAccess"})
public class AirlineListFragment extends Fragment implements AirlinesView {

    private static final String ARG_FAVORITES = "favorites";

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout refreshLayout;

    private EasyRecyclerAdapter<Airline> adapter;

    private AirlinesPresenter presenter;

    public static AirlineListFragment newInstance(boolean isFavoritesList) {
        AirlineListFragment f = new AirlineListFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_FAVORITES, isFavoritesList);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        boolean isFavoritesList = false;
        if (args != null) {
            isFavoritesList = args.getBoolean(ARG_FAVORITES, false);
        }
        if (isFavoritesList)
            presenter = App.get(getContext()).getComponent().airlineTopPresenter();
        else
            presenter = App.get(getContext()).getComponent().airlineAllPresenter();
        presenter.setIsFavorite(isFavoritesList);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_airlines_list, container, false);
        ButterKnife.bind(this, rootView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EasyRecyclerAdapter<>(getContext(), AirlineViewHolder.class, new AirlineHolderCallbackImpl());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.updateData();
            }
        });
        presenter.init(this);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.destroy();
    }

    @Override
    public void init(EndlessOnScrollListenerImpl.DataLoader loader) {
        recyclerView.addOnScrollListener(new EndlessOnScrollListenerImpl(recyclerView, loader));
    }

    @Override
    public void add(List<Airline> airlines) {
        adapter.addItems(airlines);
    }

    @Override
    public void startDetailView(Airline airline) {
        AirlineActivity.startActivity(getContext(), airline);
    }

    @Override
    public void startProgress(boolean visible) {
        refreshLayout.setRefreshing(visible);
    }

    @Override
    public void clearList() {
        adapter.setItems(new ArrayList<Airline>());
    }

    final class AirlineHolderCallbackImpl implements AirlineViewHolder.AirlineHolderCallback {
        @Override
        public void onClicked(Airline airline) {
            presenter.onAirlineClick(airline);
        }
    }
}
