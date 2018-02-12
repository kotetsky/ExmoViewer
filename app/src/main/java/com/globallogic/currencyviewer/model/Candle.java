package com.globallogic.currencyviewer.model;

import com.github.mikephil.charting.data.CandleEntry;
import com.google.gson.annotations.SerializedName;

/**
 * Created by oleh.kotetskyi on 1/3/2018.
 */

public class Candle {

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

    public CandleEntry toCandleEntry (){
        CandleEntry candleEntry = new CandleEntry(0, high, low, open, close);
        return candleEntry;
    }
}
