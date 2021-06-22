package com.example.arfashion.presentation.services;

import android.content.Context;

import com.example.arfashion.presentation.app.Constants;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class NetworkProvider {
    private static volatile NetworkProvider mInstance = null;

    private final Retrofit retrofit;

    public static NetworkProvider newInstance(Context context) {
        if (mInstance == null) {
            mInstance = new NetworkProvider(context);
        }
        return mInstance;
    }

    private NetworkProvider(Context context) {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_URL)
                .client(okhttpClient(context))
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();
    }

    public <T> T getService(Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }

    private OkHttpClient okhttpClient(Context context) {
        return new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(context))
                .readTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(2, TimeUnit.MINUTES)
                .build();
    }
}
