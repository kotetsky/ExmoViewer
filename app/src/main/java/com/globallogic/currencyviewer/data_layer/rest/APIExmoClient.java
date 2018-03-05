package com.globallogic.currencyviewer.data_layer.rest;


import com.globallogic.currencyviewer.model.CurrencyExmoTicker;
import com.globallogic.currencyviewer.model.TradePair;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIExmoClient {

    public static final String EXMO_URL = "https://api.exmo.com/v1/";

    public static Retrofit getClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        return new Retrofit.Builder()
                .baseUrl(EXMO_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(getGsonConverterLibrary())
                .client(client)
                .build();
    }

    private static GsonConverterFactory getGsonConverterLibrary(){
        return GsonConverterFactory.create(
                new GsonBuilder()
                        .registerTypeAdapter(CurrencyExmoTicker.class, new ExmoTickerTypeAdapter())
                        .registerTypeAdapter(TradePair.class, new TradePairTypeAdapter())
                        .create());
    }

}
