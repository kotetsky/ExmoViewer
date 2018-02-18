package com.globallogic.currencyviewer.model;

import java.util.List;

/**
 * TradePair
 */

public class TradePair {

    private String mName;
    private List<TradeItem> mTradeItems;

    public String getName (){
        return mName;
    }

    public void setName(String name){
        mName = name;
    }

    public List<TradeItem> getTradeItems(){
        return mTradeItems;
    }

    public void setTradeItems(List<TradeItem> tradeItems) {
        mTradeItems = tradeItems;
    }


}
