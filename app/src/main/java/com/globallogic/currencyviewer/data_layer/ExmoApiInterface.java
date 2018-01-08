package com.globallogic.currencyviewer.data_layer;

import com.globallogic.currencyviewer.model.CurrencyExmoTicker;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by oleh.kotetskyi on 1/3/2018.
 */

public interface ExmoApiInterface {

    @GET("ticker")
    Call<CurrencyExmoTicker> getTicker();

}
