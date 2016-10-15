package com.mich.airlines.view.detail;


import android.content.Context;
import android.content.Intent;

public interface DetailsView {

    void startActivity(Intent callIntent);

    void requestPermission(String permission);

    Context getContext();

    void showMessage(int string);

    void onUpdateFavoriteImage(boolean isFavoriteAfterUpdate);
}
