package com.globallogic.currencyviewer.data_layer.controller;


import android.util.Log;

import com.github.mikephil.charting.data.CandleEntry;
import com.globallogic.currencyviewer.data_layer.rest.APIExmoClient;
import com.globallogic.currencyviewer.data_layer.rest.ExmoApiInterface;
import com.globallogic.currencyviewer.model.CurrencyExmoTicker;
import com.globallogic.currencyviewer.model.TradeItem;
import com.globallogic.currencyviewer.model.TradePair;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by oleh.kotetskyi on 1/3/2018.
 */

public class CurrencyManager {

    private static final String TAG = CurrencyManager.class.getSimpleName();
    private static final int DEFAULT_CANDLE_TIME_SECONDS = 10;

    private TickerCallback mTickerCallback;
    private ExmoApiInterface mExmoApiInterface;
    private CandleEntriesCallback mСandleEntriesCallback;

    private Callback<CurrencyExmoTicker> mExmoCallback = new Callback<CurrencyExmoTicker>() {
        @Override
        public void onResponse(Call<CurrencyExmoTicker> call, Response<CurrencyExmoTicker> response) {
            int responseCode = response.code();
            CurrencyExmoTicker currencyEnqueueExmoTicker = response.body();
            Log.d(TAG, "in resultCode: " + responseCode);
            Log.d(TAG, "in currencyEnqueueExmoTicker: " + currencyEnqueueExmoTicker);
            if (responseCode >= 300) {
                Log.d(TAG, "in resultCode: " + responseCode);
                String requestString = call.request().body().toString();
                Log.d(TAG, requestString);
            }
            mTickerCallback.onTickerReceived(currencyEnqueueExmoTicker);
        }

        @Override
        public void onFailure(Call call, Throwable t) {
            Log.d(TAG, "error: " + t.getMessage());
        }
    };

    private Callback<TradePair> mTradeItemsCallback = new Callback<TradePair>(){

        @Override
        public void onResponse(Call<TradePair> call, Response<TradePair> response) {
            int responseCode = response.code();
            TradePair tradePair = response.body();
            List<TradeItem> tradeItems = tradePair.getTradeItems();
            List<CandleEntry> candleEntries = fromTradeItemsToCandleList(tradeItems, DEFAULT_CANDLE_TIME_SECONDS);
            mСandleEntriesCallback.onCandleEntriesReceived(candleEntries);
        }

        @Override
        public void onFailure(Call<TradePair> call, Throwable t) {
            Log.d(TAG, "error: " + t.getMessage());
        }
    };

    public CurrencyManager() {
        mExmoApiInterface = APIExmoClient.getClient()
                .create(ExmoApiInterface.class);
    }

    public void setTickerCallback(TickerCallback tickerCallback) {
        mTickerCallback = tickerCallback;
    }

    public void setTradeItemsCallback(CandleEntriesCallback candleEntriesCallback) {
        mСandleEntriesCallback = candleEntriesCallback;
    }

    public void getExmoTicker() {
        Log.d(TAG, "in getExmoTicker(): ");
        Call<CurrencyExmoTicker> tickerCall = mExmoApiInterface.getTicker();

        tickerCall.enqueue(mExmoCallback);

    }

    public void getTrades(String tickerItemName) {
        Log.d(TAG, "in getTrades()");
        String tickerItemPairName = tickerItemName;
        Log.d(TAG, "tickerItemPairName = " + tickerItemPairName);
        Call<TradePair> tradeItemsCall = mExmoApiInterface.getTrades(tickerItemPairName);
        tradeItemsCall.enqueue(mTradeItemsCallback);
    }

    public List<CandleEntry> fromTradeItemsToCandleList(List<TradeItem> tradeItems, long stepTime) {
        // todo need to be rewritten in proper way

        List<CandleEntry> candles = new ArrayList<>();
        candles.add(new CandleEntry(0, 22, 12, 12, 14));
        candles.add(new CandleEntry(0, 21, 14, 12, 24));
        candles.add(new CandleEntry(0, 12, 16, 12, 04));
        candles.add(new CandleEntry(0, 13, 13, 12, 10));
        return candles;
    }

    public interface TickerCallback {
        void onTickerReceived(CurrencyExmoTicker currencyEnqueueExmoTicker);
    }

    public interface CandleEntriesCallback {
        void onCandleEntriesReceived(List<CandleEntry> candleEntries);
    }

}
