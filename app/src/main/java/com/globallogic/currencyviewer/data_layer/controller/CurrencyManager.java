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

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by oleh.kotetskyi on 1/3/2018.
 */

public class CurrencyManager {

    private static final String TAG = CurrencyManager.class.getSimpleName();
    private static final int DEFAULT_CANDLE_COUNT = 10;

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

    private PublishSubject<CurrencyExmoTicker> mCurrencyExmoTickerSubject = PublishSubject.create();

    private Callback<TradePair> mTradeItemsCallback = new Callback<TradePair>() {

        @Override
        public void onResponse(Call<TradePair> call, Response<TradePair> response) {
            int responseCode = response.code();
            TradePair tradePair = response.body();
            List<TradeItem> tradeItems = tradePair.getTradeItems();
            List<CandleEntry> candleEntries = fromTradeItemsToCandleList(tradeItems, DEFAULT_CANDLE_COUNT);
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

        // todo rewrite with rxJava
        Log.d(TAG, "in getExmoTicker(): ");
        /*
        Call<CurrencyExmoTicker> tickerCall = mExmoApiInterface.getTicker();
        tickerCall.enqueue(mExmoCallback);
        */


    }

    public Observable<CurrencyExmoTicker> getRxExmoTicker() {
        Log.d(TAG, "in getExmoTicker(): ");
        return mExmoApiInterface.getRxTicker()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void getTrades(String tickerItemName) {
        Log.d(TAG, "in getTrades()");
        String tickerItemPairName = tickerItemName;
        Log.d(TAG, "tickerItemPairName = " + tickerItemPairName);
        Call<TradePair> tradeItemsCall = mExmoApiInterface.getTrades(tickerItemPairName);
        tradeItemsCall.enqueue(mTradeItemsCallback);
    }

    public List<CandleEntry> fromTradeItemsToCandleList(List<TradeItem> tradeItems, int candleCount) {
        if (tradeItems.size() == 0) {
            return new ArrayList<>();
        }

        long endDate = tradeItems.get(0).date;
        long startDate = endDate;

        for (TradeItem eachTradeItem : tradeItems) {
            long eachItemDate = eachTradeItem.date;
            if (eachItemDate > endDate) {
                endDate = eachTradeItem.date;
            }
            if (eachItemDate < startDate) {
                startDate = eachTradeItem.date;
            }
        }

        long time = (endDate - startDate) / candleCount;
        List<CandleEntry> candles = new ArrayList<>();

        for (int position = 0; position < candleCount; ++position) {
            List<TradeItem> timeTradeItems = getInTimeItems(startDate, startDate + time, tradeItems);
            CandleEntry candle = makeCandle(timeTradeItems, position);
            candles.add(candle);
            startDate += time;
        }

        return candles;
    }

    private List<TradeItem> getInTimeItems(long startTime, long endTime, List<TradeItem> tradeItems) {
        List<TradeItem> resultTradeItems = new ArrayList<>();
        for (TradeItem tradeItem : tradeItems) {
            if (tradeItem.date >= startTime && tradeItem.date < endTime) {
                resultTradeItems.add(tradeItem);
            }
        }
        return resultTradeItems;
    }

    private CandleEntry makeCandle(List<TradeItem> tradeItemsForCandles, int position) {
        if (tradeItemsForCandles.size() == 0) {
            return null;
        }
        TradeItem firstTradeItem = tradeItemsForCandles.get(0);
        long timeOpen = firstTradeItem.date;
        long timeClose = timeOpen;

        float high = firstTradeItem.price;
        float low = high;
        float open = high;
        float close = high;

        for (TradeItem tradeItem : tradeItemsForCandles) {
            float price = tradeItem.price;
            if (price > high) {
                high = price;
            }
            if (price < low) {
                low = price;
            }
            if (timeOpen > tradeItem.date) {
                timeOpen = tradeItem.date;
                open = price;
            }

            if (timeClose < tradeItem.date) {
                timeClose = tradeItem.date;
                close = price;
            }
        }
        return new CandleEntry(position, high, low, open, close);
    }

    public interface TickerCallback {
        void onTickerReceived(CurrencyExmoTicker currencyEnqueueExmoTicker);
    }

    public interface CandleEntriesCallback {
        void onCandleEntriesReceived(List<CandleEntry> candleEntries);
    }

}
