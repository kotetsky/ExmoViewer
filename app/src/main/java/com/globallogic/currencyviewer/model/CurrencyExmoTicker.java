package com.globallogic.currencyviewer.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CurrencyExmoTicker implements Serializable {

    private List<TickerItem> mTickerItems;

    public void setTickerItems(List<TickerItem> items) {
        mTickerItems = items;
    }

    public List<TickerItem> getTickerItems() {
        return mTickerItems;
    }

    public List<CryptoCurrency> createSimpleCurrencyList() {
        List<CryptoCurrency> currencies = new ArrayList<>();

        for (TickerItem tickerItem : mTickerItems) {
            currencies.add(new CryptoCurrency(tickerItem.getName(), tickerItem.last_trade));
        }
        return currencies;
    }

}
