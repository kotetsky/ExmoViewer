package com.globallogic.currencyviewer.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by oleh.kotetskyi on 1/3/2018.
 */

public class Candel {

    @SerializedName("date")
    public Integer date;

    @SerializedName("high")
    public float high;

    @SerializedName("low")
    public float low;

    @SerializedName("open")
    public float open;

    @SerializedName("close")
    public float close;

    @SerializedName("volume")
    public float volume;

    @SerializedName("quoteVolume")
    public float quoteVolume;

    @SerializedName("weightedAverage")
    public float weightedAverage;
}
