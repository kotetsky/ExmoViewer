package com.globallogic.currencyviewer.data_layer.rest;

import com.globallogic.currencyviewer.model.CurrencyExmoTicker;
import com.globallogic.currencyviewer.model.TradePair;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * ExmoApiInterface
 */

public interface ExmoApiInterface {

    @GET("ticker")
    Call<CurrencyExmoTicker> getTicker();

    @GET("trades")
    Call<TradePair> getTrades(@Query("pair") String pairName);

    @GET("ticker")
    Observable<CurrencyExmoTicker> getRxTicker();

    @GET("trades")
    Observable<TradePair> getRxTrades(@Query("pair") String pairName);
}
