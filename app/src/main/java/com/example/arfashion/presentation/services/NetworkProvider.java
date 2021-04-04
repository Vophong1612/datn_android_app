package com.example.arfashion.presentation.services;

import com.example.arfashion.presentation.app.Constants;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class NetworkProvider {
    private static volatile NetworkProvider mInstance = null;

    private Retrofit retrofit;

    public static NetworkProvider newInstance() {
        if (mInstance == null) {
            mInstance = new NetworkProvider();
        }
        return mInstance;
    }

    private NetworkProvider() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_URL)
                .client(new OkHttpClient.Builder().build())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();
    }

    public <T> T getService(Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }

}
