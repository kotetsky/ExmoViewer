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
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class CurrencyManager {

    private static final String TAG = CurrencyManager.class.getSimpleName();
    private static final int DEFAULT_CANDLE_COUNT = 10;

    private ExmoApiInterface mExmoApiInterface;
    private PublishSubject<List<CandleEntry>> mCandleEntriesPublisher = PublishSubject.create();

    public CurrencyManager() {
        mExmoApiInterface = APIExmoClient.getClient()
                .create(ExmoApiInterface.class);
    }

    public PublishSubject<List<CandleEntry>> getCandleEntriesPublisher() {
        return mCandleEntriesPublisher;
    }

    public void onTradePairReceivedForPublisherToInvoke(TradePair tradePair) {
        List<TradeItem> tradeItems = tradePair.getTradeItems();
        List<CandleEntry> candleEntries = fromTradeItemsToCandleList(tradeItems, DEFAULT_CANDLE_COUNT);
        mCandleEntriesPublisher.onNext(candleEntries);
    }

    public void onTradePairReceiveError(Throwable tradePairError) {
        Log.e(TAG, "tradePair is not received" + tradePairError.getMessage());
    }

    public Observable<CurrencyExmoTicker> getRxExmoTicker() {
        Log.d(TAG, "in getExmoTicker(): ");
        return mExmoApiInterface.getRxTicker()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
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

    private static List<TradeItem> getInTimeItems(long startTime, long endTime, List<TradeItem> tradeItems) {
        List<TradeItem> resultTradeItems = new ArrayList<>();
        for (TradeItem tradeItem : tradeItems) {
            if (tradeItem.date >= startTime && tradeItem.date < endTime) {
                resultTradeItems.add(tradeItem);
            }
        }
        return resultTradeItems;
    }

    public Disposable getRxTrades(String tickerItemName) {
        Log.d(TAG, "in rxJava get Trades");
        Disposable rxTradesDispocasble = mExmoApiInterface.getRxTrades(tickerItemName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onTradePairReceivedForPublisherToInvoke, this::onTradePairReceiveError);
        return rxTradesDispocasble;
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

}
