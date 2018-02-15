package com.globallogic.currencyviewer.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by oleh.kotetskyi on 2/13/2018.
 */

public class TradeItem {

    @SerializedName("trade_id")
    private int tradeId;

    @SerializedName("type")
    private String type;

    @SerializedName("quantity")
    private float quantity;

    @SerializedName("price")
    private float price;

    @SerializedName("amount")
    private float amount;

    @SerializedName("date")
    private long date;


}
