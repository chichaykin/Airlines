package com.mich.airlines.utils;

import android.util.Log;

public class L {

    private static final String TAG = "AirlineApp";

    public static void d(String msg, Object... args) {
        Log.d(TAG, String.format(msg, args));
    }

    @SuppressWarnings("unused")
    public static void e(Throwable t, String msg, Object... args) {
        Log.e(TAG, String.format(msg, args), t);
    }

    public static void e(String msg, Object... args) {
        Log.e(TAG, String.format(msg, args));
    }
}
