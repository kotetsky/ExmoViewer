package com.globallogic.currencyviewer.model;

import com.google.gson.annotations.SerializedName;

/**
 * TradeItem
 */

public class TradeItem {

    @SerializedName("trade_id")
    public int tradeId;

    @SerializedName("type")
    public String type;

    @SerializedName("quantity")
    public float quantity;

    @SerializedName("price")
    public float price;

    @SerializedName("amount")
    public float amount;

    @SerializedName("date")
    public long date;


}
