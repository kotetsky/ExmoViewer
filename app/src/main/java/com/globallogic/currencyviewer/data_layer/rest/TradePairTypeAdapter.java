package com.globallogic.currencyviewer.data_layer.rest;

import android.util.Log;

import com.globallogic.currencyviewer.model.TradeItem;
import com.globallogic.currencyviewer.model.TradePair;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * TradePairTypeAdapter
 */

public class TradePairTypeAdapter extends TypeAdapter<TradePair> {

    private final static String TAG = TradePairTypeAdapter.class.getSimpleName();

    @Override
    public void write(JsonWriter out, TradePair value) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public TradePair read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        TradePair pair = new TradePair();
        List<TradeItem> tradeItems = new ArrayList<>();
        TradeItem item = null;
        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            pair.setName(name);

            JsonToken token = in.peek();
            if (token == JsonToken.BEGIN_ARRAY) {
                in.beginArray();
                JsonToken tradeItemToken = in.peek();
                while (in.peek() == JsonToken.BEGIN_OBJECT) {
                    in.beginObject();
                    item = new TradeItem();
                    while (in.hasNext()) {
                        String nextPairItemField = in.nextName();
                        switch (nextPairItemField) {
                            case "trade_id":
                                item.tradeId = in.nextInt();
                                break;
                            case "type":
                                item.type = in.nextString();
                                break;
                            case "quantity":
                                item.quantity = (float) in.nextDouble();
                                break;
                            case "price":
                                item.price = (float) in.nextDouble();
                                break;
                            case "amount":
                                item.amount = (float) in.nextDouble();
                                break;
                            case "date":
                                item.date = (long) in.nextLong();
                                break;
                        }
                    }
                    in.endObject();
                    tradeItems.add(item);
                }
            }
            in.endArray();
        }
        in.endObject();
        pair.setTradeItems(tradeItems);
        Log.d(TAG, "size = " + pair.getTradeItems().size());
        return pair;
    }
}
