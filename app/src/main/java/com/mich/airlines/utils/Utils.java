package com.mich.airlines.utils;

import android.content.SharedPreferences;

import java.net.MalformedURLException;
import java.net.URL;

public class Utils {

    private static final String BASE_URL = "base_url";

    public static void storeBaseUrl(SharedPreferences preferences, String baseUrl) {
        preferences.edit().putString(BASE_URL, baseUrl).apply();
    }

    public static String readBaseUrl(SharedPreferences preferences) {
        return preferences.getString(BASE_URL, null);
    }

    public static String getFullUrl(String mBaseUrl, String file) {
        URL url1;
        try {
            url1 = new URL(mBaseUrl);
            URL url2 = new URL(url1.getProtocol(), url1.getHost(), url1.getPort(), file, null);
            return url2.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
