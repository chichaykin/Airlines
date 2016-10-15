package com.mich.airlines.view.recycleview;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


public class EndlessOnScrollListenerImpl extends RecyclerView.OnScrollListener {

    private static final int VISIBLE_THRESHOLD = 5;
    private final RecyclerView recyclerView;
    private final DataLoader dataLoader;
    private LinearLayoutManager layoutManager;
    private int previousTotal = 0;
    private boolean isLoading;

    public EndlessOnScrollListenerImpl(@NonNull RecyclerView recyclerView, @NonNull DataLoader loader) {
        this.recyclerView = recyclerView;
        dataLoader = loader;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(this);
        } else {
            throw new RuntimeException("Don't support another layout managers for a while");
        }
    }

    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = this.recyclerView.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

        if (!isLoading) {
            if (totalItemCount > previousTotal) {
                isLoading = true;
                previousTotal = totalItemCount;
            }
        }
        if (isLoading && (totalItemCount - visibleItemCount)
                <= (firstVisibleItem + VISIBLE_THRESHOLD)) {

            isLoading = false;
            dataLoader.onLoadMore();
        }
    }

    /***
     * Implement to load more data.
     */
    public interface DataLoader {
        void onLoadMore();
    }

}
