package com.globallogic.currencyviewer.data_layer.rest;

import com.globallogic.currencyviewer.model.CurrencyExmoTicker;
import com.globallogic.currencyviewer.model.TradeItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by oleh.kotetskyi on 1/3/2018.
 */

public interface ExmoApiInterface {

    @GET("ticker")
    Call<CurrencyExmoTicker> getTicker();

    @GET("trades")
    Call<List<TradeItem>> getTrades(@Query("pair") String pair);

}
