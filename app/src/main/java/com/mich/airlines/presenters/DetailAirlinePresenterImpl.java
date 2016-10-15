package com.mich.airlines.presenters;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;

import com.mich.airlines.R;
import com.mich.airlines.data.Airline;
import com.mich.airlines.repositories.AppDatabase;
import com.mich.airlines.repositories.Favorite;
import com.mich.airlines.view.detail.DetailsView;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;
import com.squareup.otto.Bus;

import javax.inject.Inject;

public class DetailAirlinePresenterImpl implements DetailAirlinePresenter {

    private DetailsView view;
    private Bus bus;

    @Inject
    public DetailAirlinePresenterImpl(DetailsView detailView, Bus bus) {
        this.view = detailView;
        this.bus = bus;
    }

    @Override
    public void onFavoriteClick(final Airline airline) {
        // insert or delete record
        final boolean[] isFavoriteAfterUpdate = new boolean[1];
        DatabaseDefinition database = FlowManager.getDatabase(AppDatabase.class);
        Transaction transaction = database.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                Favorite favorite = new Favorite(airline.getCode(), airline.getLogoURL(), airline.getSite(), airline.getPhone(), airline.getName());
                isFavoriteAfterUpdate[0] = !favorite.exists();
                if (favorite.exists()) {
                    favorite.delete(databaseWrapper);
                } else {
                    favorite.insert(databaseWrapper);
                }
            }
        })
        .success(new Transaction.Success() {
            @Override
            public void onSuccess(Transaction transaction) {
                airline.setFavorite(isFavoriteAfterUpdate[0]);
                // called post-execution on the UI thread.
                view.onUpdateFavoriteImage(airline.isFavorite());
                bus.post(airline); //tell subscribers that the airline is changed
            }
        })
        .error(new Transaction.Error() {
            @Override
            public void onError(Transaction transaction, Throwable error) {
                // call if any errors occur in the transaction.
                view.showMessage(R.string.error_on_access_db);
            }
        }).build();
        transaction.execute();
    }

    @Override
    public void onPhoneClick(Airline airline) {
        if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            view.requestPermission(Manifest.permission.CALL_PHONE);
        } else {
            startCallActivity(airline);
        }
    }

    private void startCallActivity(Airline airline) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + airline.getPhone()));
        view.startActivity(callIntent);
    }

    @Override
    public void onWebsiteClick(Airline airline) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        String url = airline.getSite();
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;
        i.setData(Uri.parse(url));
        view.startActivity(i);
    }

    @Override
    public void init() {
        //staff to init ...
    }

    @Override
    public void onPermissionGranted(Airline airline, String[] permissions, int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            // permission was granted, yay! Do the
            // contacts-related task you need to do.
            startCallActivity(airline);

        } else {
            // permission denied, boo! Disable the
            // functionality that depends on this permission.
            view.showMessage(R.string.error_cannot_make_call);
        }
    }
}
