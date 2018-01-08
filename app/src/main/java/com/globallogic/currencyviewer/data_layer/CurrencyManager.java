package com.globallogic.currencyviewer.data_layer;


import android.util.Log;

import com.globallogic.currencyviewer.model.CryptoCurrency;
import com.globallogic.currencyviewer.model.CurrencyExmoTicker;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by oleh.kotetskyi on 1/3/2018.
 */

public class CurrencyManager {

    private static final String TAG = CurrencyManager.class.getSimpleName();

    private TickerCallback mTickerCallback;
    private ExmoApiInterface mExmoApiInterface;


    private Callback<CurrencyExmoTicker> mExmoCallback = new Callback<CurrencyExmoTicker>() {
        @Override
        public void onResponse(Call<CurrencyExmoTicker> call, Response<CurrencyExmoTicker> response) {
            int responseCode = response.code();
            CurrencyExmoTicker currencyEnqueueExmoTicker = response.body();
            Log.d(TAG, "in resultCode: " + responseCode);
            Log.d(TAG, "in currencyEnqueueExmoTicker: " + currencyEnqueueExmoTicker);
            if (responseCode > 300) {
                Log.d(TAG, "in resultCode: " + responseCode);
                String requestString = call.request().body().toString();
                Log.d(TAG, requestString);
            }
            List<CryptoCurrency> currencies = currencyEnqueueExmoTicker.createSimpleCurrencyList();
            mTickerCallback.onTickerReceived(currencies);
        }

        @Override
        public void onFailure(Call call, Throwable t) {
            Log.d(TAG, "error: " + t.getMessage());
        }
    };

    public CurrencyManager(TickerCallback tickerCallback) {
        mTickerCallback = tickerCallback;
        mExmoApiInterface = APIExmoClient.getClient()
                .create(ExmoApiInterface.class);
    }

    public void getExmoTicker() {
        Log.d(TAG, "in getExmoTicker(): ");
        Call<CurrencyExmoTicker> tickerCall = mExmoApiInterface.getTicker();

        tickerCall.enqueue(mExmoCallback);

    }

    public interface TickerCallback {
        void onTickerReceived(List<CryptoCurrency> currencyList);
    }

}
