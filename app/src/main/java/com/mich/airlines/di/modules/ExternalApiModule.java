package com.mich.airlines.di.modules;


import android.content.Context;

import com.mich.airlines.BuildConfig;
import com.mich.airlines.di.scopes.ApplicationContext;
import com.mich.airlines.service.AirlinesApi;
import com.mich.airlines.utils.ConnectivityHelper;
import com.mich.airlines.utils.L;

import java.io.File;
import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public final class ExternalApiModule {
    private final String mBaseUrl;
    private final long mCashSize;
    private final Context mContext;
    private final ConnectivityHelper mHelper;

    @SuppressWarnings("SameParameterValue")
    public ExternalApiModule(String baseUrl, long cashSize,
                             @ApplicationContext Context context) {
        mBaseUrl = baseUrl;
        mCashSize = cashSize;
        mContext = context;
        mHelper = new ConnectivityHelper(context);
    }

    @SuppressWarnings("unused")
    @Provides
    @Singleton
    public AirlinesApi providesAirlinesApi() {

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR);
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.interceptors().add(logging);
        }

        File fileCache = new File(mContext.getCacheDir(), "cache");
        //noinspection ResultOfMethodCallIgnored
        fileCache.mkdir();
        L.d("Cache: %s, exist %b", fileCache.getAbsolutePath(), fileCache.exists());
        Cache cache = new Cache(fileCache, mCashSize);
        clientBuilder.cache(cache);

        return new Retrofit.Builder().client(clientBuilder.build())
                .baseUrl(mBaseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(AirlinesApi.class);

    }



    private final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            // Add Cache Control only for GET methods
            if (request.method().equals("GET")) {
                if (!mHelper.isNetworkAvailable()) {
                    // 4 weeks stale
                    request = request.newBuilder()
                            .header("Cache-Control", "public, max-stale=2419200")
                            .build();
                }
            }
            Response originalResponse = chain.proceed(request);
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, max-age=86400")
                    .build();
        }
    };
}
