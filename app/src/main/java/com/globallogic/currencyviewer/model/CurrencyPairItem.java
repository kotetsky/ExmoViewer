package com.globallogic.currencyviewer.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by oleh.kotetskyi on 2/10/2018.
 */

public class CurrencyPairItem {

    @SerializedName("trade_id")
    public String tradeIn;

    @SerializedName("type")
    public String pairType;

    @SerializedName("price")
    public String pairPrice;

    @SerializedName("quantity")
    public String quantity;

    @SerializedName("amount")
    public String amount;

    @SerializedName("date")
    public String pairDate;


}
