package com.globallogic.currencyviewer.data_layer.rest;

import com.globallogic.currencyviewer.model.CurrencyExmoTicker;
import com.globallogic.currencyviewer.model.TickerItem;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleh.kotetskyi on 1/8/2018.
 */

public class ExmoTickerTypeAdapter extends TypeAdapter<CurrencyExmoTicker> {
    @Override
    public void write(JsonWriter out, CurrencyExmoTicker value) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CurrencyExmoTicker read(JsonReader in) throws IOException {

        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        CurrencyExmoTicker ticker = new CurrencyExmoTicker();
        List<TickerItem> tickerItemList = new ArrayList<>();

        in.beginObject();
        while (in.hasNext()) {
            String tickerItemName = in.nextName();
            TickerItem item = new TickerItem();
            item.setName(tickerItemName);
            JsonToken token = in.peek();
            if (token == JsonToken.BEGIN_OBJECT) {
                in.beginObject();
                while (in.hasNext()) {
                    String nextTickerItemField = in.nextName();

                    switch (nextTickerItemField) {
                        case "buy_price":
                            item.buy_price = (float) in.nextDouble();
                            break;
                        case "sell_price":
                            item.sell_price = (float) in.nextDouble();
                            break;
                        case "last_trade":
                            item.last_trade = (float) in.nextDouble();
                            break;
                        case "high":
                            item.high = (float) in.nextDouble();
                            break;
                        case "low":
                            item.low = (float) in.nextDouble();
                            break;
                        case "avg":
                            item.avg = (float) in.nextDouble();
                            break;
                        case "vol":
                            item.vol = (float) in.nextDouble();
                            break;
                        case "vol_curr":
                            item.vol_curr = (float) in.nextDouble();
                            break;
                        case "updated":
                            item.updated = (float) in.nextDouble();
                            break;
                    }
                }
                in.endObject();
                tickerItemList.add(item);
            }
        }
        in.endObject();
        ticker.setTickerItems(tickerItemList);
        return ticker;
    }


}
