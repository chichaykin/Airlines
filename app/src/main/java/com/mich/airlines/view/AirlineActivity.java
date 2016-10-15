package com.mich.airlines.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mich.airlines.R;
import com.mich.airlines.data.Airline;
import com.mich.airlines.view.detail.DetailAirlineFragment;

public class AirlineActivity extends AppCompatActivity {

    private static final String PARAM_AIRLINE = "airline";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Airline airline = intent.getParcelableExtra(PARAM_AIRLINE);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, DetailAirlineFragment.newInstance(airline))
                .commit();
    }

    public static void startActivity(Context context, Airline airline) {
        Intent intent = new Intent(context, AirlineActivity.class);
        intent.putExtra(PARAM_AIRLINE, airline);
        context.startActivity(intent);
    }
}
