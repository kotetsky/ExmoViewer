package com.globallogic.currencyviewer.data_layer;


import com.globallogic.currencyviewer.data_layer.rest.ExmoTickerTypeAdapter;
import com.globallogic.currencyviewer.model.CurrencyExmoTicker;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIExmoClient {

    public static final String EXMO_URL = "https://api.exmo.com/v1/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        retrofit = new Retrofit.Builder()
                .baseUrl(EXMO_URL)
                .addConverterFactory(getGsonConverterLibrary())
                .client(client)
                .build();

        return retrofit;
    }

    private static GsonConverterFactory getGsonConverterLibrary(){
        return GsonConverterFactory.create(
                new GsonBuilder().registerTypeAdapter(
                        CurrencyExmoTicker.class, new ExmoTickerTypeAdapter()).create());
    }

}
