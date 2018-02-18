package com.globallogic.currencyviewer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TickerItem implements Serializable{
    private static final long serialVersionUID = 1L;

    @Expose
    public String mName;

    @SerializedName("buy_price")
    public float buy_price;

    @SerializedName("sell_price")
    public float sell_price;

    @SerializedName("last_trade")
    public float last_trade;

    @SerializedName("high")
    public float high;

    @SerializedName("low")
    public float low;

    @SerializedName("avg")
    public float avg;

    @SerializedName("vol")
    public float vol;

    @SerializedName("vol_curr")
    public float vol_curr;

    @SerializedName("updated")
    public float updated;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    @Override
    public String toString() {
        String s = "last_trade = " + last_trade;
        return s;
    }

}